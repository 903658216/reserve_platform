package com.jh.controller;


import com.jh.bloom.BloomUtil;
import com.jh.entity.Hotel;
import com.jh.entity.ResultData;
import com.jh.event.constant.EventConstant;
import com.jh.event.util.EventUtil;
import com.jh.service.IHotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hotel")
@Slf4j
public class HotelController {

    @Autowired
    private IHotelService iHotelService;

    @Autowired
    private EventUtil eventUtil;

    @Autowired
    private BloomUtil bloomUtil;


    /**
     * 添加酒店信息
     * @param hotel
     * @return
     */
    @RequestMapping("/save")
    ResultData<Boolean> insertHotel(@RequestBody Hotel hotel){

        boolean flag = iHotelService.save(hotel);
        return new ResultData<Boolean>().setData(flag);

    }

    /**
     * 查询酒店信息
     * @return
     */
    @RequestMapping("/list")
    ResultData<List<Hotel>> hotelList(){

        List<Hotel> list = iHotelService.list();
        return new  ResultData<List<Hotel>>().setData(list);
    }

    /**
     * 根据编号查询酒店信息
     * @param id
     * @return
     */
    @RequestMapping("/selectHotelById")
    ResultData<Hotel> selectHotelById(@RequestBody Integer id){

        Hotel hotel = iHotelService.getById(id);
        return new  ResultData<Hotel>().setData(hotel);
    }

    /**
     * 根据编号查询酒店信息
     * @param hid
     * @return
     */
    @RequestMapping("/getHotelById")
    ResultData<Hotel> getHotelById(Integer hid){

        //查询酒店信息
        Hotel hotel = iHotelService.getById(hid);
        return new  ResultData<Hotel>().setData(hotel);
    }

     /**
     * 根据编号查询酒店信息(点击率计算)
     * @param hid
     * @return
     */
    @RequestMapping("/getHotelById1")
    ResultData<Hotel> getHotelById1(Integer hid,String devId){

        //查询酒店信息
        Hotel hotel = iHotelService.getById(hid);
        //TODO 每当根据酒店编号查询一次酒店信息，算一次点击率
        if (!bloomUtil.isExists("djl",devId+"-"+hid)){
            //之前为未点击过，算一次点击率
            System.out.println(devId + "-" + hid + "算一次点击率");
            //发布点击酒店事件
            eventUtil.publishEvent(EventConstant.EVENT_HOTEL_CLICK,hotel);
            //不存在，就添加一次
            bloomUtil.addBloom("djl",devId+"-"+hid);
        }else {
            System.out.println(devId+ "-"+ hid +"已经点击过了");
        }

        return new  ResultData<Hotel>().setData(hotel);
    }



    /**
     * 根据酒店编号修改酒店信息
     * @param hotel
     * @return
     */
    @RequestMapping("/updateHotelById")
    ResultData<Boolean> updateHotelById(@RequestBody Hotel hotel,@RequestParam Integer ocid){

        boolean flag = iHotelService.updateById1(hotel,ocid);
        return new ResultData<Boolean>().setData(flag);
    }

    /**
     * 根据酒店编号删除酒店信息
     * @param id
     * @return
     */
    @RequestMapping("/deleteHotelById")
    ResultData<Boolean> deleteHotelById(@RequestParam Integer id){

        boolean flag = iHotelService.removeById(id);
        return new ResultData<Boolean>().setData(flag);
    }








}
