package com.fu.mybatisplusdemo.entity;

import lombok.Data;

import java.util.List;

@Data
public class EntityA {
    private Integer id;
    private String name;
    private List<EntityB> bList;
}
