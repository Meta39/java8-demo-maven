package com.fu.mybatisplusdemo.entity;

import lombok.Data;

import java.util.List;

@Data
public class EntityB {
    private Integer id;
    private String name;
    private List<EntityC> cList;
}
