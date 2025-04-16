package com.fu.basedemo.algorithm;

import org.junit.jupiter.api.Test;

/**
 * 递归
 */
public class RecursionTest {
    @Test
    public void test() {
        //递归：计算1~100的和
        System.out.println(recursion(100));
    }

    private static int recursion(int n) {
        //1.定义跳出循环的条件
        if(n==1){
            return 1;
        }
        //2.递归方法需要做的事，一定是越做越少的，否则就会死循环。
        return n + recursion(n-1);
    }
}
