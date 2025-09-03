package com.fu.mybatisplusgeneratordemo.config;


import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.type.ITypeConvertHandler;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OracleNumberTypeConverter implements ITypeConvertHandler {
    private static final String NUMBER = "NUMBER";

    @Override
    public IColumnType convert(GlobalConfig globalConfig, TypeRegistry typeRegistry, TableField.MetaInfo metaInfo) {
        //输出表结构信息
        log.debug("metaInfo:{}", metaInfo);
        String typeName = metaInfo.getTypeName();//Oracle 数据库类型。NUMBER、VARCHAR2等
        int length = metaInfo.getLength();//字段长度
        int scale = metaInfo.getScale();//字段精度。0表示无小数位，1表示只有1个小数位
        //处理数值类型
        if (NUMBER.equals(typeName)) {
            return handleNumber(length, scale);
        }
        //其它类型
        return typeRegistry.getColumnType(metaInfo);
    }

    /**
     * 处理 NUMBER 类型
     */
    private IColumnType handleNumber(int length, int scale) {
        //整型(-127表示没设置小数位，也表示整型)
        if (scale == 0 || scale == -127) {
            //整型小于等于8用 Integer
            if (length <= 8) {
                return DbColumnType.INTEGER;
            }
            //整型大于8用 Long
            return DbColumnType.LONG;
        }
        //非整型全部返回BigDecimal
        return DbColumnType.BIG_DECIMAL;
    }

}
