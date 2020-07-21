package com.jh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jh.entity.Room;

import java.util.List;

//@Repository
public interface RoomMapper extends BaseMapper<Room> {


    /**
     * 根据酒店编号查询酒店客房类型列表
     * @param hid
     * @return
     */
    List<Room> selectRoomListByHid(Integer hid);
}
