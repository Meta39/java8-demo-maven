package com.fu.springbootdemo.global;

import com.fu.springbootdemo.annotation.ReturnMeta;
import com.fu.springbootdemo.exception.CommonException;
import com.fu.springbootdemo.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * 全局返回和异常处理类
 */
@RestControllerAdvice
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private static final Logger log = LoggerFactory.getLogger(GlobalResponseBodyAdvice.class);

    /**
     * 自定义异常
     */
    @ExceptionHandler(value = CommonException.class)
    public Res<Object> forbiddenException(CommonException e) {
        int code = e.getCode();
        String message = e.getMessage();
        log.error(code + ":" + message, e);
        return Res.err(code, message);
    }

    /**
     * 服务器异常
     */
    @ExceptionHandler(value = Exception.class)
    public Res<Object> exception(Exception e) {
        String message = e.getMessage();
        log.error(message, e);
        return Res.err(message);
    }

    /**
     * RequestParam注解请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Res<Object> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return Res.err(e.getMessage());
    }

    /**
     * validation参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Res<Object> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Res.err(e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";")));
    }

    //-----------------------------------------------有新的异常在上面加--------------------------------------------------------

    //-----------------------------------------------下面是全局返回-----------------------------------------------------------

    /**
     * 是否把返回内容存放到Res返回给前端
     *
     * @param returnType    返回类型
     * @param converterType 转换器类型
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        //如果没有获取到@ReturnMeta自定义注解，则返回全局返回类
        AnnotatedElement annotatedElement = returnType.getAnnotatedElement();
        ReturnMeta returnMeta = AnnotationUtils.findAnnotation(annotatedElement, ReturnMeta.class);
        return returnMeta == null;
    }

    /**
     * 把返回内容存放到Res返回给前端
     *
     * @param body       原始数据
     * @param returnType 原始返回给前端的数据类型
     * @param request    请求
     * @param response   响应
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String) {//String类型要特殊处理
            return JacksonUtils.writeValueAsStringJson(Res.ok(body));
        } else if (body instanceof Res) {//本身是Res直接返回即可。例如：全局异常处理，返回的就是Res
            return body;
        } else if (body instanceof LinkedHashMap) {//解决404、500等spring没有捕获的异常问题，只能放到最后的判断条件去判断
            LinkedHashMap map = (LinkedHashMap) body;//强转
            //如果LinkedHashMap包含status状态码的key，则抛出异常。
            if (map.containsKey("status") && map.containsKey("error") && map.containsKey("message")) {
                log.error("全局返回异常捕获到LinkedHashMap：{}", map);
                String errorMessage = "error：" + map.get("error") + ",message：" + map.get("message") + ",path：" + map.get("path");
                return Res.err((Integer) map.get("status"), errorMessage);
            }
        }
        return Res.ok(body);
    }

}
