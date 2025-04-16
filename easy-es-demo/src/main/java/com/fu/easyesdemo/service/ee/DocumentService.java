package com.fu.easyesdemo.service.ee;

import cn.easyes.core.biz.EsPageInfo;
import com.fu.easyesdemo.entity.Document;

import java.util.List;

/**
 * 虽然官方没说但还是建议和mp分开
 */
public interface DocumentService {

    Integer insert(Document document);

    Document selectById(String id);

    Integer updateById(Document document);

    Integer deleteById(String id);

    List<Document> selectList();

    EsPageInfo<Document> pageQuery(Integer pageIndex, Integer pageSize);

    Integer deleteBatchIds(List<String> ids);

}

