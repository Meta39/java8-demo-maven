package com.fu.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fu.springbootdemo.entity.Authorize;
import com.fu.springbootdemo.entity.Role;
import com.fu.springbootdemo.entity.RoleAuthorize;
import com.fu.springbootdemo.entity.User;
import com.fu.springbootdemo.exception.UnauthorizedException;
import com.fu.springbootdemo.global.*;
import com.fu.springbootdemo.mapper.AuthorizeMapper;
import com.fu.springbootdemo.mapper.LoginMapper;
import com.fu.springbootdemo.mapper.RoleAuthorizeMapper;
import com.fu.springbootdemo.mapper.UserMapper;
import com.fu.springbootdemo.service.LoginService;
import com.fu.springbootdemo.util.CurrentLoginUserUtil;
import com.fu.springbootdemo.util.RSAUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private static final int NODE_TYPE = 3;//节点类型为按钮：1、文件夹 2、页面 3、按钮
    private static final int ADMIN_ROLE_ID = 1;//超级管理员角色ID
    private final GlobalAuthenticationFilter globalAuthenticationFilter;
    private final LoginMapper loginMapper;
    private final UserMapper userMapper;
    private final RoleAuthorizeMapper roleAuthorizeMapper;
    private final AuthorizeMapper authorizeMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 登录
     */
    @Override
    public TokenInfo login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPwd();
        User user = this.userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new RuntimeException("登录用户名不存在");
        }
        if (user.getIsBan() == 1) {
            throw new RuntimeException("登录用户被禁用");
        }
        //前端RSA加密字符串解密后和数据库的密码解密进行匹配
        if (!Objects.equals(RSAUtil.decrypt(GlobalVariable.RSA_TOKEN_PRIVATE_KEY,password),RSAUtil.decrypt(GlobalVariable.RSA_TOKEN_PRIVATE_KEY,user.getPwd()))) {
            throw new RuntimeException("密码错误");
        }
        // TODO 验证码
        TokenInfo tokenInfo = new TokenInfo();
        Set<Role> roles = this.loginMapper.selectUserRoleInfo(user.getId());
        Set<Integer> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
        //Redis存储当前登录用户的角色Id集合
        if (!roleIds.isEmpty()){//判断当前登录用户是否有角色
            tokenInfo.setRoleIds(roleIds);
            //判断是否包含超级管理员角色
            if (roleIds.stream().anyMatch(roleId -> roleId == ADMIN_ROLE_ID)) {
                //超级管理员直接获取所有权限
                Set<String> authorizes = this.authorizeMapper.selectList(null).stream().filter(a -> a.getNodeType() == NODE_TYPE).map(Authorize::getAuthorizeName).collect(Collectors.toSet());
                tokenInfo.setAuthorizes(authorizes);
            } else {
                //不是超级管理员，则获取所有角色所拥有的权限
                Set<String> authorizes = this.loginMapper.selectUserAuthorizes(roleIds);
                tokenInfo.setAuthorizes(authorizes);
            }
        }
        //用UUID + 用户ID作为token
        String token = UUID.randomUUID().toString().replaceAll("-","") + user.getId();
        //把登录用户信息存入Redis。无需存储menus，因为用不上还会增加Redis的压力。
        this.redisTemplate.opsForValue().set(GlobalVariable.getTokenRedisKey(token), tokenInfo, Duration.ofSeconds(globalAuthenticationFilter.getTokenTimeout()));
        //存储进Redis以后再往TokenInfo对象设置菜单返回给前端。返回的菜单不包含按钮，如果包含按钮，前端展示菜单树会错误。
        List<Authorize> menus = new ArrayList<>();
        if (!roleIds.isEmpty()) {
            //超级管理员直接获取全部菜单集合
            if (roleIds.stream().anyMatch(roleId -> roleId == ADMIN_ROLE_ID)) {
                menus = this.authorizeMapper.selectList(new LambdaQueryWrapper<Authorize>().orderByAsc(Authorize::getSort));
            } else {
                for (Integer roleId : roleIds) {//根据角色ID获取角色权限ID集合
                    List<RoleAuthorize> roleAuthorizes = this.roleAuthorizeMapper.selectList(new LambdaQueryWrapper<RoleAuthorize>().eq(RoleAuthorize::getRoleId, roleId));
                    for (RoleAuthorize roleAuthorize : roleAuthorizes) {//获取其中一个角色的所有权限列表
                        List<Authorize> oneRoleAuthorizes = this.authorizeMapper.selectList(new LambdaQueryWrapper<Authorize>().eq(Authorize::getId, roleAuthorize.getAuthorizeId()).orderByAsc(Authorize::getSort));
                        menus.addAll(oneRoleAuthorizes);
                    }
                }
            }
            //递归权限树只获取菜单文件夹和页面，不获取按钮
            List<Authorize> currentUserMenus = menus;
            menus = menus.stream().filter(authorize -> authorize.getPId() == 0).peek(authorize -> authorize.setChildAuthorize(menuTree(authorize, currentUserMenus))).collect(Collectors.toList());
        }
        //设置只有前端需要的内容
        tokenInfo.setToken(token);
        tokenInfo.setRoleIds(null);//不用返回给前端角色Id集合
        tokenInfo.setNickname(user.getNickname());//昵称
        tokenInfo.setRoleNames(roles.stream().map(Role::getRoleName).collect(Collectors.toSet()));//角色名称集合
        tokenInfo.setMenus(new HashSet<>(menus));//菜单集合
        return tokenInfo;
    }

    /**
     * 登出
     */
    @Override
    public Boolean logout(HttpServletRequest request) {
        String tokenRedisKey = GlobalVariable.getTokenRedisKey(request.getHeader(GlobalVariable.TOKEN));
        if (Boolean.TRUE.equals(this.redisTemplate.hasKey(tokenRedisKey))) {
            return this.redisTemplate.delete(tokenRedisKey);
        }
        throw new UnauthorizedException();
    }

    /**
     * 修改当前登录用户密码
     */
    @Override
    public Boolean updatePwd(UpdatePwdDTO updatePwdDTO) {
        int userId = CurrentLoginUserUtil.getUserId();
        User userInfo = this.userMapper.selectById(userId);
        if (!Objects.equals(RSAUtil.decrypt(GlobalVariable.RSA_TOKEN_PRIVATE_KEY,userInfo.getPwd()),RSAUtil.decrypt(GlobalVariable.RSA_TOKEN_PRIVATE_KEY,updatePwdDTO.getPwd()))){
            throw new RuntimeException("旧密码错误！");
        }
        //加密密码
        LambdaUpdateWrapper<User> setUserPwd = new LambdaUpdateWrapper<User>()
                .eq(User::getId,userId)
                .set(User::getPwd,updatePwdDTO.getNewPwd());
        this.userMapper.update(null,setUserPwd);
        return true;
    }

    /**
     * 续期token
     */
    @Override
    public Boolean token(HttpServletRequest request) {
        String tokenRedisKey = GlobalVariable.getTokenRedisKey(request.getHeader(GlobalVariable.TOKEN));
        if (Boolean.TRUE.equals(this.redisTemplate.hasKey(tokenRedisKey))) {
            return this.redisTemplate.expire(tokenRedisKey, Duration.ofSeconds(globalAuthenticationFilter.getTokenTimeout()));
        }
        throw new UnauthorizedException();
    }

    //-----------------------------------------内部方法------------------------------------------------------

    /**
     * 递归菜单树
     */
    private static List<Authorize> menuTree(Authorize authorize, List<Authorize> authorizes) {
        return authorizes.stream()
                .filter(a ->
                        // NodeType：1是文件夹、2是页面、3是按钮，这里准确的说是返回菜单给前端展示。按钮权限放到另外的集合。
                        (a.getPId().equals(authorize.getId())) && a.getNodeType() != NODE_TYPE)
                .peek(a -> a.setChildAuthorize(menuTree(a, authorizes)))
                .collect(Collectors.toList());
    }

}
