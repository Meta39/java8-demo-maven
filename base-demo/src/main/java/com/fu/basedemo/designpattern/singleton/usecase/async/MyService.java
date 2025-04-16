package com.fu.basedemo.designpattern.singleton.usecase.async;

/**
 * @since 2024-07-25
 */
public interface MyService {
    @Async
    void asyncMethod();

    void normalMethod();
}
