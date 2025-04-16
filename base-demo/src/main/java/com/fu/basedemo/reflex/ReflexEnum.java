package com.fu.basedemo.reflex;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReflexEnum {
    ReflexA,
    ReflexB,
    ;

    public static String value(ReflexEnum reflexEnum){
        for (ReflexEnum reflex : ReflexEnum.values()){
            if (reflex == reflexEnum){
                return reflexEnum.toString();
            }
        }
        return "";
    }

}
