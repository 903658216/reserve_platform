package com.jh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jh.entity.Dictionaries;
import com.jh.entity.DictionariesContent;
import com.jh.entity.ResultData;
import com.jh.service.IDictionariesContentService;
import com.jh.service.IDictionariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/couponDictionaries")
public class DictionariesController {

    @Autowired
    private IDictionariesService iDictionariesService;

    @Autowired
    private IDictionariesContentService iDictionariesContentService;

    /**
     * 查询字典列表
     * @return ResultData<List<Dictionaries>>
     */
    @CrossOrigin
    @RequestMapping("/getDictionariesList")
    public ResultData<List<Dictionaries>> getDictionariesList(){
        List<Dictionaries> dictionariesList = iDictionariesService.list();

        return new ResultData().setData(dictionariesList);
    }

    /**
     * 根据字典编号查询字典(内容)数据实体类信息
     * @param did
     * @return
     */
    @CrossOrigin
    @RequestMapping("/getDictionariesContentsByDId")
    public ResultData<List<DictionariesContent>> getDictionariesContentsByDId(@RequestParam Integer did){
        List<DictionariesContent> dictionariesContentList = iDictionariesContentService.list(new QueryWrapper<DictionariesContent>().eq("did", did));
        return new ResultData().setData(dictionariesContentList);
    }

//    public ResultData<List<>>


    /**
     * 新增字典
     * @param dictionaries
     * @return
     */
    @RequestMapping("/insertDictionaries")
    public ResultData<Boolean> insertDictionaries(@RequestBody Dictionaries dictionaries){

        boolean flag = iDictionariesService.save(dictionaries);
        return new ResultData().setData(flag);
    }

    /**
     * 根据字典的id删除字典
     * @param id
     * @return
     */
    @RequestMapping("/deleteDictionariesById")
    ResultData<Boolean> deleteDictionariesById(@RequestParam Integer id){

        boolean flag = iDictionariesService.removeById(id);
        return  new ResultData().setData(flag);
    }
    /**
     * 新增字典数据内容
     * @param content
     * @return
     */
    @RequestMapping("/insertDictionariesContent")
    public ResultData<Boolean> insertDictionariesContent(@RequestBody DictionariesContent content){
        boolean flag = iDictionariesContentService.save(content);

        return new ResultData().setData(flag);
    }

}
