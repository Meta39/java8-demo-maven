package com.fu.basedemo.utils;

import java.util.UUID;

public final class UuidUtils {
    private  UuidUtils() {}

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getUUIDReplace() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
