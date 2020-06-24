package com.jh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jh.entity.City;

public interface ICityService extends IService<City> {
    /**
     *
     * @param cId
     * @param number
     * @return
     */
    boolean updateHotelNumById(Integer cId, Integer number);
}
