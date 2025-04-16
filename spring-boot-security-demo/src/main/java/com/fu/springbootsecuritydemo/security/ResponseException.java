package com.fu.springbootsecuritydemo.security;


import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseException {
    private static final ObjectMapper om = new ObjectMapper();

    public static void returnException(HttpServletResponse response, Object obj) throws IOException {
        //这里很重要，否则页面获取不到正常的JSON数据集
        response.setContentType("application/json;charset=UTF-8");
        //跨域设置
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Method", "*");
        //输出JSON
        PrintWriter out = response.getWriter();
        out.write(om.writeValueAsString(obj));
        out.flush();
        out.close();
    }

}
