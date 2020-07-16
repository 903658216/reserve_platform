package com.jh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jh.dao.OrderPriceDetailMapper;
import com.jh.entity.OrderPriceDetails;
import com.jh.service.IOrderPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IOrderPriceServiceImpl extends ServiceImpl<OrderPriceDetailMapper,OrderPriceDetails>  implements IOrderPriceService {

    @Autowired
    private  OrderPriceDetailMapper orderPriceDetailMapper;

}
