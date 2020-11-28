package com.example.itemservice.model;

import com.example.itemservice.dao.domain.Item;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/28 9:59 PM
 */
@Data
@ToString
public class ResponseSearchOnItem {

    Item item;

    String sellerId;

    String startTime = "";

    String endTime = "";

    BidStatus bidStatus = BidStatus.Ready;

    Integer bidCount = 0;

    boolean canBuyNow;

    Double buyNowPrice = 0.0D;

}
