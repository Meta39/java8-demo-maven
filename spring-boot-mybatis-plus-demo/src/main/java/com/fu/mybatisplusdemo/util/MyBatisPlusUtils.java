package com.fu.mybatisplusdemo.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * MyBatis-Plus 工具类
 */
public final class MyBatisPlusUtils {
    public static final int DEFAULT_PAGE_SIZE_INT = 1000;
    public static final long DEFAULT_PAGE_SIZE = 1000L;

    private MyBatisPlusUtils() {

    }

    /**
     * 分页查询一批数据
     * @param <T>
     */
    @FunctionalInterface
    public interface MyBatisPlusPageFunctionInterface<T> {
        Page<T> selectPage(Page<T> page);
    }

    /**
     * 处理每一批次的业务数据
     * @param <T>
     */
    @FunctionalInterface
    public interface MyBatisPlusFunctionInterface<T> {
        void execute(List<T> tList);
    }

    /**
     * 无条件分页查询N条（N由调用方决定）
     *
     * @param extendsBaseMapperClass       继承了BaseMapper的Mapper接口
     * @param myBatisPlusFunctionInterface 业务逻辑
     */
    public static <T> void findByPage(BaseMapper<T> extendsBaseMapperClass, long pageSize, MyBatisPlusFunctionInterface<T> myBatisPlusFunctionInterface) {
        findByPage(extendsBaseMapperClass, null, pageSize, myBatisPlusFunctionInterface);
    }

    /**
     * 无条件分页查询（默认每次取1000条，直到取完为止）
     *
     * @param extendsBaseMapperClass       继承了BaseMapper的Mapper接口
     * @param myBatisPlusFunctionInterface 业务逻辑
     */
    public static <T> void findByPage(BaseMapper<T> extendsBaseMapperClass, MyBatisPlusFunctionInterface<T> myBatisPlusFunctionInterface) {
        findByPage(extendsBaseMapperClass, null, DEFAULT_PAGE_SIZE, myBatisPlusFunctionInterface);
    }

    /**
     * 有条件分页查询（默认每次取1000条，直到取完为止）
     *
     * @param extendsBaseMapperClass       继承了BaseMapper的Mapper接口
     * @param myBatisPlusFunctionInterface 业务逻辑
     */
    public static <T> void findByPage(BaseMapper<T> extendsBaseMapperClass, LambdaQueryWrapper<T> lambdaQueryWrapper, MyBatisPlusFunctionInterface<T> myBatisPlusFunctionInterface) {
        findByPage(extendsBaseMapperClass, lambdaQueryWrapper, DEFAULT_PAGE_SIZE, myBatisPlusFunctionInterface);
    }

    /**
     * 后端分页查询所有数据
     *
     * @param extendsBaseMapperClass       继承了BaseMapper的Mapper接口
     * @param myBatisPlusFunctionInterface 业务逻辑
     */
    public static <T> void findByPage(BaseMapper<T> extendsBaseMapperClass, LambdaQueryWrapper<T> lambdaQueryWrapper, long pageSize, MyBatisPlusFunctionInterface<T> myBatisPlusFunctionInterface) {
        findByPage((Page<T> page) -> extendsBaseMapperClass.selectPage(page, lambdaQueryWrapper), pageSize, myBatisPlusFunctionInterface);
    }

    /**
     * 自定义继承了 BaseMapper 分页方法（默认每次取1000条，直到取完为止）
     */
    public static <T> void findByPage(MyBatisPlusPageFunctionInterface<T> functionInterface, MyBatisPlusFunctionInterface<T> myBatisPlusFunctionInterface) {
        findByPage(functionInterface, DEFAULT_PAGE_SIZE, myBatisPlusFunctionInterface);
    }

    /**
     * 通用分页查询方法
     *
     * @param functionInterface            获取分页数据的方法
     * @param pageSize                     每次获取的数据量
     * @param myBatisPlusFunctionInterface 对获取的分页数据进行处理
     */
    public static <T> void findByPage(MyBatisPlusPageFunctionInterface<T> functionInterface, long pageSize, MyBatisPlusFunctionInterface<T> myBatisPlusFunctionInterface) {
        // 参数校验
        Assert.isTrue(pageSize > 0, "每页大小必须大于 0");
        Assert.notNull(myBatisPlusFunctionInterface, "数据处理逻辑不能为 null");
        int currentPage = 1;
        while (true) {
            Page<T> tPage = functionInterface.selectPage(Page.of(currentPage, pageSize));
            long pages = tPage.getPages();
            List<T> records = tPage.getRecords();
            //数据为空
            if (CollectionUtils.isEmpty(records)) {
                return;
            }

            // 业务逻辑
            myBatisPlusFunctionInterface.execute(records);
            //最后一页
            if (pages <= currentPage) {
                return;
            }
            currentPage++;
        }
    }

}

