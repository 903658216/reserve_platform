package com.jh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jh.entity.RoomPrice;

import java.util.List;

public interface IRoomPriceService  extends IService<RoomPrice> {

    /**
     * 根据房间编号查询客房价格列表
     * @param rid
     * @return
     */
    List<RoomPrice> selectRoomPriceListByRid(Integer rid);
}
