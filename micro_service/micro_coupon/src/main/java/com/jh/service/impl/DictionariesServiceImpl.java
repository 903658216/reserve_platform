package com.jh.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jh.dao.DictionariesMapper;
import com.jh.entity.Dictionaries;
import com.jh.entity.DictionariesContent;
import com.jh.service.IDictionariesContentService;
import com.jh.service.IDictionariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
public class DictionariesServiceImpl extends ServiceImpl<DictionariesMapper, Dictionaries> implements IDictionariesService {

    @Autowired
    private DictionariesMapper dictionariesMapper;

    @Autowired
    private IDictionariesContentService iDictionariesContentService;

    @Override
    @Transactional
    public boolean removeById(Serializable id) {
        boolean flag = super.removeById(id);

        boolean did = iDictionariesContentService.remove(new UpdateWrapper<DictionariesContent>().eq("did", id));
        return flag;
    }

    @Override
    @Transactional
    public List<Dictionaries> list() {
        List<Dictionaries> list = super.list();
        list.stream().forEach(dictionaries -> {

            List<DictionariesContent> dictionariesContentList = iDictionariesContentService.list(new QueryWrapper<DictionariesContent>().eq("did", dictionaries.getId()));
            //TODO 将字典的内容设置到字典实体类中
            dictionaries.setDictionariesContents(dictionariesContentList);
        });
        return list;
    }

    @Override
    @Transactional
    public List<Dictionaries> list(Wrapper<Dictionaries> queryWrapper) {
        List<Dictionaries> dictionariesList = super.list(queryWrapper);

        dictionariesList.stream().forEach(dictionaries -> {

            List<DictionariesContent> dictionariesContentList = iDictionariesContentService.list(new QueryWrapper<DictionariesContent>().eq("did", dictionaries.getId()));
            dictionaries.setDictionariesContents(dictionariesContentList);
        });
        return dictionariesList;
    }
}
