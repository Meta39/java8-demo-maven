package com.fu.springbootdemo.global;

import com.fu.springbootdemo.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.fu.springbootdemo.global.GlobalVariable.TOKEN;

/**
 * 全局认证过滤器
 */
@Component
@WebFilter(urlPatterns = "/*", filterName = "globalAuthenticationFilter")
@ConfigurationProperties("fu.authentication")
public class GlobalAuthenticationFilter implements Filter {

    /**
     * token默认过期时间单位秒s
     */
    private int tokenTimeout = 4 * 60 * 60;//默认4小时

    /**
     * 默认不需要认证的URI：'POST:/login'。
     * 建议：开发环境推荐全部接口不认证也不鉴权，设置为'**'。
     * 警告：生产环境不要设置为'**'
     */
    private List<String> notAuthentication = Arrays.asList("POST:/login", "GET:/getPasswordPublicKey"); //不要求认证的uri

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //设置允许跨域访问
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");

        String token = request.getHeader(TOKEN);//请求头的token
        String redisTokenKey = GlobalVariable.getTokenRedisKey(token);//redis存放token的key
        String methodAndURI = request.getMethod() + ":" + request.getRequestURI();//请求方法和请求URI
        AntPathMatcher antPathMatcher = new AntPathMatcher();//这个比equals好使！
        boolean matchURI = this.notAuthentication.stream().anyMatch(ymlMethodAndURI -> antPathMatcher.match(ymlMethodAndURI, methodAndURI));//判断请求Method和URI是否在不需要认证的集合里面
        boolean checkToken = StringUtils.hasLength(token) && Boolean.TRUE.equals(this.redisTemplate.hasKey(redisTokenKey));
        if (checkToken){//续期token
            this.redisTemplate.expire(redisTokenKey, Duration.ofSeconds(tokenTimeout));
        }
        if (matchURI || checkToken) {//认证白名单或这认证通过
            filterChain.doFilter(request, response);//放行
        } else {
            //未认证
            this.resolver.resolveException(request, response, null, new UnauthorizedException());
        }
    }

    //---------------------------get/set--------------------------------------

    public int getTokenTimeout() {
        return tokenTimeout;
    }

    public void setTokenTimeout(int tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }

    public List<String> getNotAuthentication() {
        return notAuthentication;
    }

    public void setNotAuthentication(List<String> notAuthentication) {
        this.notAuthentication = notAuthentication;
    }
}
