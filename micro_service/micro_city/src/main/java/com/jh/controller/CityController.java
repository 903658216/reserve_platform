package com.jh.controller;

import com.jh.entity.City;
import com.jh.entity.ResultData;
import com.jh.service.ICityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/city")
@Slf4j
public class CityController {

    @Autowired
    private ICityService cityService;

    /**
     * 添加城市接口
     * @return
     */
    @RequestMapping("/save")
    public ResultData<Boolean> citySave(@RequestBody City city){
        log.info("添加城市：" + city);
        boolean flag = cityService.save(city);
        return new ResultData<Boolean>().setData(flag);
    }

    /**
     * 城市列表
     * TODO
     * @return
     */
//    @IsLogin(mustLogin = true)
    @RequestMapping("/list")
    public ResultData<List<City>> cityList(){
        List<City> list = cityService.list();
        return new ResultData<List<City>>().setData(list);
    }

    /**
     * 根据城市的编号查询城市信息
     * @param id 城市id
     * @return
     */
    @RequestMapping("/selectCityById")
    public  ResultData<City> selectCityById(@RequestBody Integer id){

        City city = cityService.getById(id);
        return new ResultData<City>().setData(city);
    }

      /**
     * 根据城市的编号查询城市信息
     * @param city 城市信息
     * @return
     */
      @RequestMapping("/cityUpdateById")
      public ResultData<Boolean> cityUpdateById(@RequestBody City city){

          Boolean flag = cityService.updateById(city);
          return new ResultData<Boolean>().setData(flag);

      }

    /**
     * 根据城市的编号删除城市信息
     * @param id
     * @return
     */
    @RequestMapping("/cityDeleteById")
    public ResultData<Boolean> cityDeleteById(@RequestBody Integer id){
        boolean flag = cityService.removeById(id);
        return new ResultData<Boolean>().setData(flag);

    }

    @RequestMapping("/cityUpdateHoterNumberById/{cId}/{number}")
    ResultData<Boolean> cityUpdateHoterNumberById(@PathVariable Integer cId, @PathVariable Integer number){

        System.out.println("城市微服务接到的城市id和添加数量"+cId+","+number);
        boolean flag = cityService.updateHotelNumById(cId,number);
        return new ResultData<Boolean>().setData(flag);

    }


}
