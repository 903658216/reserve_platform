package com.jh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jh.entity.City;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface CityMapper extends BaseMapper<City> {

    boolean updateHotelNumById(Integer cId, Integer number);
}
