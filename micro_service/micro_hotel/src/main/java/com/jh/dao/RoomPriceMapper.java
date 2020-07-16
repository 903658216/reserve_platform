package com.jh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jh.entity.RoomPrice;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

//@Repository
public interface RoomPriceMapper extends BaseMapper<RoomPrice> {

    List<RoomPrice> selectRoomPriceListByRid(Integer rid);

    /**
     * 修改酒店客房价格表中，已预订的客房数量
     * @param rid
     * @param rNumber
     * @param beginTime
     * @param endTime
     * @return int
     */
    int updateRoomNumber(@Param("rid") Integer rid,@Param("rNumber") Integer rNumber, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
}
