package com.jh.controller;


import com.jh.entity.Hotel;
import com.jh.entity.ResultData;
import com.jh.entity.SearchInfo;
import com.jh.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ISearchService iSearchService;


    @RequestMapping("/searchHotel")
    public ResultData<List<Hotel>> searchHotel(SearchInfo searchInfo){

        System.out.println("搜索服务接收到客服端的搜索请求:"+ searchInfo);

        List<Hotel> hotelList = iSearchService.searchByKeyword(searchInfo);
        System.out.println("搜索到的结果总共有："+ hotelList.size());
        hotelList.stream().forEach(hotel -> {
            System.out.println(hotel);
        });
        return new ResultData<List<Hotel>>().setData(hotelList);

    }
}
