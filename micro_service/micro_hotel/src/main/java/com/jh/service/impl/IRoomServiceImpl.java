package com.jh.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jh.dao.RoomMapper;
import com.jh.entity.Room;
import com.jh.entity.RoomPrice;
import com.jh.event.constant.EventConstant;
import com.jh.event.util.EventUtil;
import com.jh.service.IRoomPriceService;
import com.jh.service.IRoomService;
import com.jh.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class IRoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private IRoomPriceService iRoomPriceService;

    @Autowired
    private EventUtil eventUtil;

    @Override
    @Transactional
    public boolean save(Room room) {

        Boolean flag = super.save(room);
        List<RoomPrice> roomPrices =new ArrayList<>();

        //同时生成最近一个月的价格列表
        for (int i =0; i<30;i++){
            RoomPrice roomPrice = new RoomPrice()
                    .setRid(room.getId())
                    .setType(0)//非普通房价
                    .setPrice(room.getDefaultPrice())
                    .setNumber(0)
                    .setHasNumber(room.getNumber())
                    .setDate(DateUtil.getNextDate(i));
            roomPrices.add(roomPrice);
        }

        iRoomPriceService.saveBatch(roomPrices);
        List<RoomPrice> roomPrices1 = iRoomPriceService.selectRoomPriceListByRid(room.getId());

        room.setRoomPriceList(roomPrices1);

        //发布新增酒店客房事件
        eventUtil.publishEvent(EventConstant.EVENT_HOTEL_ROOM_INSERT,room);
        return flag;
    }



    /**
     * 根据酒店编号查询酒店客房类型列表
     * @param hid
     * @return
     */
    @Override
    public List<Room> selectRoomListByHid(Integer hid) {

        List<Room> roomList = roomMapper.selectRoomListByHid(hid);

        return roomList;
    }


    /**
     * 重写mybatis-plus的根据条件构造器查询客房列表的方法
     * @param queryWrapper
     * @return
     */
    @Override
    public List<Room> list(Wrapper<Room> queryWrapper) {

        List<Room> roomList = super.list(queryWrapper);
        //将各个客房的编号，查询其价格信息
        roomList.stream().forEach(room -> {

            List<RoomPrice> roomPriceList = iRoomPriceService.list(new QueryWrapper<RoomPrice>().eq("rid",room.getId()));

            room.setRoomPriceList(roomPriceList);
        });

        return roomList;


    }
}
