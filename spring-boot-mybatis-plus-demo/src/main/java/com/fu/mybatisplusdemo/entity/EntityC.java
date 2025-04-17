package com.fu.mybatisplusdemo.entity;

import lombok.Data;

import java.util.List;

@Data
public class EntityC {
    private Integer id;
    private String name;
    private List<EntityD> dList;
}
