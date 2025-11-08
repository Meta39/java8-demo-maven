package com.fu.springbootdynamicservicedemo;

import com.fu.springbootdynamicservicedemo.dynamicserviceregistry.DynamicInvoker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DynamicInvokerTest {

    @Autowired
    private DynamicInvoker invoker;

    //不存在的 DynamicService（报错，则校验通过）
    @Test
    void testNonexistentBean() throws Throwable {
        Object result = invoker.invoke("nonexistentService", null, null);
    }

    //不存在的 DynamicMethod（报错，则校验通过）
    @Test
    void testNonexistentMethod() throws Throwable {
        Object result = invoker.invoke("calcService", "nonexistentMethod", null);
    }

    // ① 单参数是集合（body 传数组）
    @Test
    void testCollectionBodyArray() throws Throwable {
        Object result = invoker.invoke("calcService", "sumList", "[1,2,3]");
        Assertions.assertEquals(6, result);
    }

    // ② 单参数是集合（body 传标量）
    @Test
    void testCollectionBodyScalar() throws Throwable {
        Object result = invoker.invoke("calcService", "sumList", "5");
        Assertions.assertEquals(5, result);
    }

    // ⑤ 多参数按顺序（body 是数组）
    @Test
    void testMultiParamsArrayOrder() throws Throwable {
        String json = "[\"A\",\"B\"]";
        Object result = invoker.invoke("calcService", "concat", json);
        Assertions.assertEquals("AB", result);
    }

    // ⑥ 空 body返回nullnull
    @Test
    void testEmptyBody() throws Throwable {
        Object result = invoker.invoke("calcService", "concat", null);
    }

    // ⑦ 参数校验失败（报错，则校验通过）
    @Test
    void testValidationFailure() throws Throwable {
        String json = "{\"userDTO\":null}";
        invoker.invoke("calcService", "requireNotNull", json);
    }

    // ⑧ 标量包集合
    @Test
    void testScalarWrappedAsCollection() throws Throwable {
        String json = "[\"X\",[\"Y\"]]";
        Object result = invoker.invoke("calcService", "pair", json);
        Assertions.assertEquals("X:[Y]", result);
    }

    // ⑨ 数组类型参数
    @Test
    void testArrayType() throws Throwable {
        String json = "[\"a\",\"b\",\"c\"]";
        Object result = invoker.invoke("calcService", "echoArray", json);
        Assertions.assertEquals("a,b,c", result);
    }

}

