package com.jh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jh.entity.RoomPrice;

import java.util.Date;
import java.util.List;

public interface IRoomPriceService  extends IService<RoomPrice> {

    /**
     * 根据房间编号查询客房价格列表
     * @param rid
     * @return
     */
    List<RoomPrice> selectRoomPriceListByRid(Integer rid);

    /**
     * 修改酒店客房价格表中，已预订的客房数量
     * @param rid
     * @param rNumber
     * @param beginTime
     * @param endTime
     * @return int
     */
    int updateRoomNumber(Integer rid, Integer rNumber, Date beginTime, Date endTime);
}
