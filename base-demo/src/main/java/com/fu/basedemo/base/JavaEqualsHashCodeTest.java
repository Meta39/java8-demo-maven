package com.fu.basedemo.base;

/**
 * ==号与equals的区别:
 * 对于基本类型，== 判断两个值是否相等，基本类型没有 equals() 方法。
 * 对于引用类型，== 判断两个变量是否引用同一个对象，而 equals() 判断引用的对象是否等价。
 * 如：Err未重写equals和hashCode方法，因此equals输出是false，Hash值不一致HashSet长度是2。
 * 如：Apple使用@Data注解，重写equals和hashCode方法，因此输出equals是true，由于HashSet无序，不允许重复数据的特性，添加到HashSet再输出长度是1，证明了2个新建的对象的Hash值也是一致的。
 */
public class JavaEqualsHashCodeTest {

}
