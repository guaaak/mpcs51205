package com.example.biddingservice.dao.domain;

import com.example.biddingservice.constants.BidStatus;
import java.util.LinkedHashMap;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author lukewwang
 * @time 2020/11/25 4:32 PM
 */
@Data
@ToString
@Document(value = "bid-records-coll")
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
