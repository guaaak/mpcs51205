package com.example.itemservice.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/28 10:25 PM
 */
@Data
@ToString
public class UpdateItemBody {

    Integer quantity = 0;
    String description = "";
    Double shippingCost = 0.0D;
    String canBuyNow = "false";
    Double buyNowPrice = 0.0D;


}
