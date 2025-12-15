package com.fu.springbootdynamicservicedemo.dynamicservice;

import com.fu.springbootdynamicservicedemo.annotation.DynamicMethod;
import com.fu.springbootdynamicservicedemo.annotation.DynamicService;
import com.fu.springbootdynamicservicedemo.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DynamicService
@RequiredArgsConstructor
public class CalcService {
    private final TestDynamicService testDynamicService;

    //同名方法，使 invokeNoCache 调用报错。因为出现了同名方法。
    public int sumList() {
        return 1;
    }

    @DynamicMethod("sumList")
    @Transactional(rollbackFor = Exception.class)
    public int sumList(List<Integer> nums) {
        testDynamicService.noParamMethod();
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
