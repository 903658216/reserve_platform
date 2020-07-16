package com.jh.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderPriceDetails implements Serializable {


    @TableId(type = IdType.AUTO)
    private Integer id;
    private  String oid;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;
    private BigDecimal price;
    private  Date createTime=new Date();
    private  Integer status = 0;
}
