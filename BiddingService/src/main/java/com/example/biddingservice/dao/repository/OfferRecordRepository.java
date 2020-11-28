package com.example.biddingservice.dao.repository;

import com.example.biddingservice.dao.domain.OfferRecord;
import java.util.List;

/**
 * @author lukewwang
 * @time 2020/11/28 2:58 PM
 */
public interface OfferRecordRepository {

    void saveRecord(OfferRecord offerRecord);

    void updateBidStatusByOthers(String itemId, String bidStatus, Double currHighestPrice);

    void updateBidStatusByBidder(String itemId, String bidderId, String bidStatus,
            Double bidderOfferPrice);

    List<OfferRecord> getPrevOffersByUserId(String userId);

}
