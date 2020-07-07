package com.jh.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class SearchInfo implements Serializable {

    private  String keyword;
    private  String beginTime;
    private  String endTime;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String cityName;
    private  double lat;
    private  double lon;
    private  Integer sortType = 1;




}
