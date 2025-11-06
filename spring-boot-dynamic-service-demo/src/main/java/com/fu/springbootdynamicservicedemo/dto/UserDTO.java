package com.fu.springbootdynamicservicedemo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {
    private String id;
    @NotBlank
    private String name;
}
