package com.fu.mybatisplusdemo.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MyBatis-Plus 工具类
 */
public final class MyBatisPlusUtils {
    public static final int BATCH_SIZE = 999;
    public static final int INT_1000 = 1000;
    public static final long LONG_1000 = 1000L;

    private MyBatisPlusUtils() {

    }

    /**
     * 分页查询一批数据
     *
     * @param <T>
     */
    @FunctionalInterface
    public interface PageFunctionInterface<T> {
        Page<T> findPageLogic(Page<T> page);
    }

    /**
     * 处理每一批次的业务数据
     *
     * @param <T>
     */
    @FunctionalInterface
    public interface BusinessLogicFunctionInterface<T> {
        void businessLogic(List<T> tList);
    }

    /**
     * IN SQL 查询逻辑
     * @param <T> 记录对象类型
     * @param <E> IN 条件类型
     */
    @FunctionalInterface
    public interface InSqlFunctionInterface<T, E> {
        List<T> inSqlLogic(List<E> batchIds);
    }

    /**
     * 无条件分页查询N条（N由调用方决定）
     *
     * @param extendsBaseMapperClass         继承了BaseMapper的Mapper接口
     * @param businessLogicFunctionInterface 业务逻辑
     */
    public static <T> void findAllByPage(BaseMapper<T> extendsBaseMapperClass, long pageSize, BusinessLogicFunctionInterface<T> businessLogicFunctionInterface) {
        findAllByPage(extendsBaseMapperClass, null, pageSize, businessLogicFunctionInterface);
    }

    /**
     * 无条件分页查询（默认每次取1000条，直到取完为止）
     *
     * @param extendsBaseMapperClass         继承了BaseMapper的Mapper接口
     * @param businessLogicFunctionInterface 业务逻辑
     */
    public static <T> void findAllByPage(BaseMapper<T> extendsBaseMapperClass, BusinessLogicFunctionInterface<T> businessLogicFunctionInterface) {
        findAllByPage(extendsBaseMapperClass, null, LONG_1000, businessLogicFunctionInterface);
    }

    /**
     * 有条件分页查询（默认每次取1000条，直到取完为止）
     *
     * @param extendsBaseMapperClass         继承了BaseMapper的Mapper接口
     * @param businessLogicFunctionInterface 业务逻辑
     */
    public static <T> void findAllByPage(BaseMapper<T> extendsBaseMapperClass, LambdaQueryWrapper<T> lambdaQueryWrapper, BusinessLogicFunctionInterface<T> businessLogicFunctionInterface) {
        findAllByPage(extendsBaseMapperClass, lambdaQueryWrapper, LONG_1000, businessLogicFunctionInterface);
    }

    /**
     * 后端分页查询所有数据
     *
     * @param extendsBaseMapperClass         继承了BaseMapper的Mapper接口
     * @param businessLogicFunctionInterface 业务逻辑
     */
    public static <T> void findAllByPage(BaseMapper<T> extendsBaseMapperClass, LambdaQueryWrapper<T> lambdaQueryWrapper, long pageSize, BusinessLogicFunctionInterface<T> businessLogicFunctionInterface) {
        findAllByPage((Page<T> page) -> extendsBaseMapperClass.selectPage(page, lambdaQueryWrapper), pageSize, businessLogicFunctionInterface);
    }

    /**
     * 自定义继承了 BaseMapper 分页方法（默认每次取1000条，直到取完为止）
     */
    public static <T> void findAllByPage(PageFunctionInterface<T> functionInterface, BusinessLogicFunctionInterface<T> businessLogicFunctionInterface) {
        findAllByPage(functionInterface, LONG_1000, businessLogicFunctionInterface);
    }

    /**
     * 通用分页查询方法
     *
     * @param functionInterface              获取分页数据的方法
     * @param pageSize                       每次获取的数据量
     * @param businessLogicFunctionInterface 对获取的分页数据进行处理
     */
    public static <T> void findAllByPage(PageFunctionInterface<T> functionInterface, long pageSize, BusinessLogicFunctionInterface<T> businessLogicFunctionInterface) {
        // 参数校验
        Assert.isTrue(pageSize > 0, "每页大小必须大于 0");
        Assert.notNull(businessLogicFunctionInterface, "数据处理逻辑不能为 null");
        int currentPage = 1;
        while (true) {
            Page<T> tPage = functionInterface.findPageLogic(Page.of(currentPage, pageSize));
            long pages = tPage.getPages();
            List<T> records = tPage.getRecords();
            //数据为空
            if (CollectionUtils.isEmpty(records)) {
                return;
            }

            // 业务逻辑
            businessLogicFunctionInterface.businessLogic(records);
            //最后一页
            if (pages <= currentPage) {
                return;
            }
            currentPage++;
        }
    }

    /**
     * 通用 IN 查询超过 1000 条方法
     */
    public static <T, E> List<T> findAllByIds(List<E> idList, InSqlFunctionInterface<T, E> inSqlFunctionInterface) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        int totalIds = idList.size();
        //预分配结果列表容量（减少扩容开销）
        List<T> tList = new ArrayList<>(totalIds);
        for (int i = 0; i < totalIds; i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, totalIds);
            List<E> batchIds = idList.subList(i, end);
            //获取N条 IN 查询结果
            List<T> dataList = inSqlFunctionInterface.inSqlLogic(batchIds);
            tList.addAll(dataList);
        }
        return tList;
    }

}

