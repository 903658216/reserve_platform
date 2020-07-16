package com.jh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jh.dao.RoomPriceMapper;
import com.jh.entity.RoomPrice;
import com.jh.event.constant.EventConstant;
import com.jh.event.util.EventUtil;
import com.jh.service.IRoomPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IRoomPriceServiceImpl extends ServiceImpl<RoomPriceMapper, RoomPrice> implements IRoomPriceService {

    @Autowired
    private RoomPriceMapper roomPriceMapper;

    @Autowired
    private EventUtil eventUtil;

    /**
     * 根据房间编号查询客房价格列表
     * @param rid
     * @return
     */
    @Override
    public List<RoomPrice> selectRoomPriceListByRid(Integer rid) {

        return roomPriceMapper.selectRoomPriceListByRid(rid);
    }

    /**
     * 修改酒店客房价格表中，已预订的客房数量
     * @param rid
     * @param rNumber
     * @param beginTime
     * @param endTime
     * @return int
     */
    @Override
    public int updateRoomNumber(Integer rid, Integer rNumber, Date beginTime, Date endTime) {

        return roomPriceMapper.updateRoomNumber(rid,rNumber,beginTime,endTime);
    }

    /**
     * 修改了酒店客房价格表的信息 -- 调整了价格
     * @param roomPrice
     * @return
     */
    @Override
    public boolean updateById(RoomPrice roomPrice) {
        boolean flag = super.updateById(roomPrice);

        //发布一个修改酒店客房价格的事件
        eventUtil.publishEvent(EventConstant.EVENT_HOTEL_ROOM_PRICE_UPDATE,roomPrice);
        return flag;
    }



}
