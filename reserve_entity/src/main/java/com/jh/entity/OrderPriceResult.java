package com.jh.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain =true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderPriceResult implements Serializable {

    private  double allPrice;

    private List<List<String>> priceDetails;


}
