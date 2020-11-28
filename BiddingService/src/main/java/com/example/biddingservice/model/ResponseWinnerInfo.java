package com.example.biddingservice.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/25 9:08 PM
 */
@Data
@ToString
public class ResponseWinnerInfo {

    String winnerId = "";
    double finalOffer = 0.0D;
    // swap to item object
    String itemId = "";


}
