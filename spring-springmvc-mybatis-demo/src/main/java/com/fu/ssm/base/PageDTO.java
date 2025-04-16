package com.fu.ssm.base;

import com.github.pagehelper.IPage;
import lombok.Data;

@Data
public class PageDTO implements IPage {
    private Integer page;//起始页
    private Integer size;//每页数量
    private String orderBy;//排序

    /**
     * 初始化的时候就设置默认参数，如果不传则默认起始页为1，条数为10。
     */
    public PageDTO(){
        this.page=1;
        this.size=10;
    }

    @Override
    public Integer getPageNum() {
        return page;
    }

    @Override
    public Integer getPageSize() {
        return size;
    }

    @Override
    public String getOrderBy() {
        return orderBy;
    }

}