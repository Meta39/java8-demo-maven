package com.fu.basedemo.entity;

import com.fu.basedemo.annotation.IgnoreColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    @IgnoreColumn
    private Integer sex;
}
