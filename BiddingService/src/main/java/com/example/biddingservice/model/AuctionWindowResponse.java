package com.example.biddingservice.model;

import com.example.biddingservice.dao.domain.CountDownRecord;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/29 3:05 PM
 */
@Data
@ToString
public class AuctionWindowResponse {

    List<CountDownRecord> windowList = new ArrayList<>();

}
