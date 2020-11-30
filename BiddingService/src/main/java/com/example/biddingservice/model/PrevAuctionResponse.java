package com.example.biddingservice.model;

import com.example.biddingservice.dao.domain.BidRecord;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/30 3:59 PM
 */
@Data
@ToString
public class PrevAuctionResponse {

    List<BidRecord> prevAuctionList = new ArrayList<>();

}
