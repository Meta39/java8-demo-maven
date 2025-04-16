package com.fu.easyesdemo.entity;

import cn.easyes.annotation.HighLight;
import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.annotation.rely.FieldType;
import cn.easyes.annotation.rely.IdType;
import lombok.Data;

@Data
@IndexName(value = "document")
public class Document {
    /**
     * es中的唯一id,如果你想自定义es中的id为你提供的id,比如MySQL中的id,请将注解中的type指定为customize,如此id便支持任意数据类型)
     */
    @IndexId(type = IdType.NONE)
    private String id;

    /**
     * 文档标题,不指定类型默认被创建为keyword类型,可进行精确查询
     */
    @IndexField(value = "title")
    private String title;

    /**
     * 文档内容,指定了类型及存储/查询分词器
     */
    @HighLight(mappingField = "highlightContent") //对应下面的highlightContent变量名
    @IndexField(value = "content", fieldType = FieldType.TEXT)
    private String content;

    /**
     * 创建时间
     */
    @IndexField(value = "create_time", fieldType = FieldType.DATE, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    /**
     * 不存在的字段
     */
    @IndexField(exist = false)
    private String notExistsField;

    /**
     * 地理位置经纬度坐标 例如: "40.13933715136454,116.63441990026217"
     */
    @IndexField(fieldType = FieldType.GEO_POINT)
    private String location;

    /**
     * 图形(例如圆心,矩形)
     */
    @IndexField(fieldType = FieldType.GEO_SHAPE)
    private String geoLocation;

    /**
     * 自定义字段名称
     */
    @IndexField(value = "wu-la")
    private String customField;

    /**
     * 高亮返回值被映射的字段
     */
    private String highlightContent;

}

