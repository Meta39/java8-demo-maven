package com.fu.basedemo.jdk8;

import com.fu.basedemo.entity.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stream API
 * 执行流程：Stream实例化》》》一系列中间操作》》》执行终止操作
 * 说明：1不会存储元素 2不会改变源对象，返回的是一个新Stream。 3操作是延迟的，等到需要结果的时候才执行 4执行终止操作，不能再执行中间操作或终止操作
 * 1、筛选与切片
 * 2、映射
 * 3、排序
 */
public class StreamTest {

    private static List<User> getData(){
        return new ArrayList<>();
    }

    // 筛选与切片
    @Test
    public void filter() {
        List<Integer> list = Arrays.asList(1,1,22,31,14,65,14,72,81,91,22);
        System.out.println("获取集合中大于50的数据");
        list.stream().filter(i -> i > 50).forEach(System.out::println);
        //截断流：limit(n)。
        System.out.println("获取集合中大于5的前2条数据");
        list.stream().filter(i -> i > 50).limit(2).forEach(System.out::println);
        //跳过元素：skip(n)，返回一个扔掉了前面n个元素的流。不足则返回空流。
        System.out.println("跳过前8个元素");
        list.stream().skip(8).forEach(System.out::println);
        //去重
        System.out.println("去除重复的数据");
        list.stream().distinct().forEach(System.out::println);

    }

    // 映射
    @Test
    public void mapper() {
        List<String> list = Arrays.asList("aa","bb","cc","dd","abc","def","jkl");
        System.out.println("把集合里面的字符串全部转为大写");
        //lambda写法：list.stream().map(str -> str.toUpperCase()).forEach(System.out::println);
        //方法引用
        list.stream().map(String::toUpperCase).forEach(System.out::println);
        System.out.println("获取用户名称长度大于3的姓名");
        //lambda写法：getData().stream().map(user -> user.getName()).filter(name -> name.length() >3).forEach(System.out::println);
        getData().stream().map(User::getName).filter(name -> name.length() >3).forEach(System.out::println);
    }

    // 排序
    @Test
    public void sort() {
        List<Integer> list = Arrays.asList(1,1,22,31,14,65,14,72,81,91,22);
        System.out.println("自然排序（升序）");
        list.stream().sorted().forEach(System.out::println);
        System.out.println("定制排序（降序）");
        list.stream().sorted((i1,i2) -> -i1 - i2).forEach(System.out::println);
    }

    //----------------------------------- Stream API --------------------------------------------------

    //终止操作
    @Test
    public void stop(){
        List<Integer> list = Arrays.asList(1,1,22,31,14,65,14,72,81,91,22);
        System.out.println("终止操作：allMatch（boolean）完全匹配");
        System.out.println(list.stream().allMatch(i -> i > 90));
        System.out.println("终止操作：anyMatch（boolean）至少匹配一个");
        System.out.println(list.stream().anyMatch(i -> i > 90));
        System.out.println("终止操作：findFirst返回第一个元素");
        System.out.println(list.stream().findFirst().get());
        System.out.println("终止操作：count统计。如：统计大于50的数字");
        System.out.println(list.stream().filter(i -> i > 50).count());
        System.out.println("终止操作：max获取最大的值。得到的是个Optional集合");
//        System.out.println(list.stream().max((i1, i2) -> Integer.compare(i1, i2)));
        //如果是对象则可以先通过map映射拿到值，再去获取最大的值。map就是为了拿到具体的值。
//        list.stream().map(user -> user.getAge()).max(Integer::compare));
        System.out.println(list.stream().max(Integer::compare));
        //get()从Optional获取值
        System.out.println(list.stream().max(Integer::compare).get());
        System.out.println("终止操作：min获取最小的值。得到的是个Optional集合");
        System.out.println(list.stream().min(Integer::compare));
        System.out.println(list.stream().min(Integer::compare).get());
        System.out.println("终止操作：forEach遍历");
//        list.stream().forEach(System.out::println);
        list.forEach(System.out::println);
        //针对List来说，遍历的方式：一、使用Iterator 二、增强for 三、一般for 四、forEach()

        //----------------------------------------------------------------------------------------------

        /*
            终止操作：归约
         */
        System.out.println("终止操作：reduce归约。计算集合的和");
//        System.out.println(list.stream().reduce(0, (i1, i2) -> i1 + i2));
        //0是初始值
        System.out.println(list.stream().reduce(0, Integer::sum));
        //如果是10，表示10再加上list的和
        System.out.println(list.stream().reduce(10, Integer::sum));
        //计算对象集合里面的对象的变量的值的和，先通过map映射获取对象的值，再reduce归约
//        list.stream().map(user -> user.getAge()).reduce(0,Integer::sum);

        //----------------------------------------------------------------------------------------------

        /*
            终止操作：收集
         */
        System.out.println("终止操作：collect收集。如：把大于60的值重新整理成一个新的List");
        List<Integer> list1 = list.stream().filter(i -> i > 60).collect(Collectors.toList());
        list1.forEach(System.out::println);
    }

}
