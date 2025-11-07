package com.fu.springbootdynamicservicedemo.dynamicservice;

import com.fu.springbootdynamicservicedemo.annotation.DynamicMethod;
import com.fu.springbootdynamicservicedemo.annotation.DynamicService;
import com.fu.springbootdynamicservicedemo.dto.UserDTO;

import java.util.List;

@DynamicService
public class CalcService {

    //同名方法，使 invokeNoCache 调用报错。因为出现了同名方法。
    public int sumList() {
        return 1;
    }

    @DynamicMethod("sumList")
    public int sumList(List<Integer> nums) {
        return nums.stream().mapToInt(Integer::intValue).sum();
    }

    @DynamicMethod("concat")
    public String concat(String a, String b) {
        return a + b;
    }

    @DynamicMethod("pair")
    public String pair(String left, List<String> right) {
        return left + ":" + right;
    }

    @DynamicMethod("requireNotNull")
    public UserDTO requireNotNull(UserDTO userDTO) {
        return userDTO;
    }

    @DynamicMethod("echoArray")
    public String echoArray(String[] arr) {
        return String.join(",", arr);
    }
}
