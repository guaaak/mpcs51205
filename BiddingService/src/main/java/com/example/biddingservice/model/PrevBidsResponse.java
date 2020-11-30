package com.example.biddingservice.model;

import com.example.biddingservice.dao.domain.OfferRecord;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/30 3:57 PM
 */
@Data
@ToString
public class PrevBidsResponse {

    List<OfferRecord> prevBidsList = new ArrayList<>();
}
