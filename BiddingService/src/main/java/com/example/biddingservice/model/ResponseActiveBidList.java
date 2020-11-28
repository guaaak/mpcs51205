package com.example.biddingservice.model;

import com.example.biddingservice.dao.domain.BidRecord;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/25 9:53 PM
 */
@Data
@ToString
public class ResponseActiveBidList {

    List<BidRecord> activeBidList = new ArrayList<>();

}
