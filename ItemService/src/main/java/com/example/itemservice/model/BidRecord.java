package com.example.itemservice.model;

import java.util.LinkedHashMap;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/28 9:27 PM
 */
@Data
@ToString
public class BidRecord {

    String itemId;

    String sellerId;

    String startTime = "";

    String endTime = "";

    // key is userId, value is the highest offer from that user
    LinkedHashMap<String, Double> prevBid = new LinkedHashMap<>();

    BidStatus bidStatus = BidStatus.Ready;

    String winnerId = "";

    Double finalOffer = 0.0D;

    Integer bidCount = 0;

    boolean canBuyNow;

    Double buyNowPrice = 0.0D;


}
