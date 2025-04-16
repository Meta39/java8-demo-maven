package com.fu.velocitygeneratecodedemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表的相关信息
 */
@Data
public class Tables {
    private String tableCatalog;//表目录
    private String tableSchema;//表归属的库
    private String tableName;//表名
    private String tableType;//表类型
    private String engine;//引擎
    private Integer version;//版本
    private String rowFormat;//行格式
    private Long tableRows;//表的总记录数
    private Long avgRowLength;//行平均长度
    private Long dataLength;//数据长度
    private Long maxDataLength;//数据最大长度
    private Long indexLength;//索引长度
    private Long dataFree;//数据自由
    private Long autoIncrement;//表自增主键当前值
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//修改时间
    private LocalDateTime checkTime;//校验时间
    private String tableCollation;//表字符集
    private String checksum;//校验和
    private String createOptions;//创建选项
    private String tableComment;//表备注
}