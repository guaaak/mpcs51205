package com.example.biddingservice.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/25 5:07 PM
 */
@Data
@ToString
public class RequestInitBidBody {

    String itemId = "";
    String sellerId = "";
    String startTime = "";
    String endTime = "";
    double initPrice = 0.0D;
    String canBuyNow = "";
    double buyNowPrice = 0.0D;


}
