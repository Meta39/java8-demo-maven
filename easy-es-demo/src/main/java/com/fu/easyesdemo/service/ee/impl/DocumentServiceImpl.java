package com.fu.easyesdemo.service.ee.impl;

import cn.easyes.core.biz.EsPageInfo;
import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import com.fu.easyesdemo.entity.Document;
import com.fu.easyesdemo.mapper.ee.DocumentMapper;
import com.fu.easyesdemo.service.ee.DocumentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 虽然官方没说但还是建议和mp分开
 */
@Service
public class DocumentServiceImpl implements DocumentService {
    @Resource
    private DocumentMapper documentMapper;

    @Override
    public Integer insert(Document document) {
        return this.documentMapper.insert(document);
    }

    @Override
    public Document selectById(String id) {
        return this.documentMapper.selectById(id);
    }

    @Override
    public Integer updateById(Document document) {
        return this.documentMapper.updateById(document);
    }

    @Override
    public Integer deleteById(String id) {
        return this.documentMapper.deleteById(id);
    }

    @Override
    public List<Document> selectList() {
        LambdaEsQueryWrapper<Document> leqw = new LambdaEsQueryWrapper<>();
        return this.documentMapper.selectList(leqw);
    }

    @Override
    public EsPageInfo<Document> pageQuery(Integer pageIndex, Integer pageSize) {
        LambdaEsQueryWrapper<Document> leqw = new LambdaEsQueryWrapper<>();
        return this.documentMapper.pageQuery(leqw, pageIndex, pageSize);
    }

    @Override
    public Integer deleteBatchIds(List<String> ids) {
        return this.documentMapper.deleteBatchIds(ids);
    }

}

