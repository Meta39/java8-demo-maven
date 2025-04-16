package com.fu.ssm.dto;

import com.fu.ssm.base.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * test数据传输对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TestDTO extends PageDTO {
    private String name;//名称
}