package com.example.biddingservice.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/25 7:39 PM
 */
@Data
@ToString
public class RequestNewOfferBody {

    String itemId = "";
    String userId = "";
    Double newBidPrice = 0.0D;

}
