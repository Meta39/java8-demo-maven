package com.fu.basedemo;

import com.fu.basedemo.designpattern.adapterpattern.AdapterPattern;
import com.fu.basedemo.designpattern.adapterpattern.NewInterface;
import com.fu.basedemo.designpattern.adapterpattern.OldImpl;
import com.fu.basedemo.designpattern.adapterpattern.OldInterface;
import org.junit.jupiter.api.Test;

/**
 * 适配器模式
 *
 * @since 2024-07-26
 */
public class AdapterPatternTests {

    /**
     * 将一个类的接口转换成客户希望的另一个接口，使得原本由于接口不兼容而不能一起工作的类可以一起工作。
     */
    @Test
    void testAdapterPattern() {
        OldInterface oldInterface = new OldImpl();
        NewInterface adapter = new AdapterPattern(oldInterface);

        // 调用新接口的方法，实际上是调用旧接口的方法
        adapter.newMethod();
    }

}
