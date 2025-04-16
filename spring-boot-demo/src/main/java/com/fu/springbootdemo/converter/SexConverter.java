package com.fu.springbootdemo.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 导入Excel数据性别字符串转数字
 */
public class SexConverter implements Converter<Integer> {
    private static final String MAN = "男";
    private static final String WOMAN = "女";

    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 从Cell中读取数据
        String gender = cellData.getStringValue();
        // 判断Excel中的值，将其转换为预期的数值
        if (MAN.equals(gender)) {
            return 1;
        } else if (WOMAN.equals(gender)) {
            return 0;
        }
        return null;
    }

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 判断实体类中获取的值，转换为Excel预期的值，并封装为WriteCellData对象
        if (value == null) {
            return new WriteCellData<>("");
        } else if (value == 0) {
            return new WriteCellData<>(MAN);
        } else if (value == 1) {
            return new WriteCellData<>(WOMAN);
        }
        return new WriteCellData<>("");
    }
}
