package com.example.biddingservice.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/28 10:38 PM
 */
@ToString
@Data
public class UpdateBuyNow {

    String canBuyNow = "";
    Double buyNowPrice = 0.0D;

}
