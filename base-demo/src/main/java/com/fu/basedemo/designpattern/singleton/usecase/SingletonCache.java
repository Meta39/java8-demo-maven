package com.fu.basedemo.designpattern.singleton.usecase;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * 单例模式缓存
 */
public class SingletonCache {
    private final Map<String, Object> cacheMap;

    private SingletonCache() {
        cacheMap = new ConcurrentHashMap<>();
    }

    private static class SingletonHelper {
        private static final SingletonCache INSTANCE = new SingletonCache();
    }

    private static SingletonCache getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public static void put(String key, Object value) {
        getInstance().cacheMap.put(key, value);
    }

    public static Object get(String key) {
        return getInstance().cacheMap.get(key);
    }
}