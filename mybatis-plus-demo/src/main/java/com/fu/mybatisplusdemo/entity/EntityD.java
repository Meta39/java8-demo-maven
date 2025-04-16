package com.fu.mybatisplusdemo.entity;

import lombok.Data;

import java.util.List;

@Data
public class EntityD {
    private Integer id;
    private String name;
    private List<EntityE> eList;
}
