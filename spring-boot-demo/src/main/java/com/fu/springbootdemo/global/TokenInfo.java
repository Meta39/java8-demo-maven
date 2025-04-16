package com.fu.springbootdemo.global;

import com.fu.springbootdemo.entity.Authorize;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 当前登录用户token信息
 */
@Data
public class TokenInfo implements Serializable {
    private static final long serialVersionUID = 6558796578827818462L;
    private String token; //token（前端）
    private Set<String> authorizes;//权限集合(前后端)
    private Set<Integer> roleIds;//当前登录用户所拥有的角色Id集合(后端)
    private String nickname; //昵称（前端，Redis无需存储）
    private Set<String> roleNames; //当前登录用户所拥有的角色名称集合（前端用，Redis无需存储）
    private Set<Authorize> menus; //前端菜单（前端用，Redis无需存储）
}
