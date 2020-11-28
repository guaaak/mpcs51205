package com.example.biddingservice.biz;

import com.example.biddingservice.dao.domain.OfferRecord;
import java.util.List;

/**
 * @author lukewwang
 * @time 2020/11/28 4:41 PM
 */
public interface OfferProcessService {

    List<OfferRecord> getPrevBidsByUser(String userId);

    void updateBidStatusByOthers(String itemId, String bidStatus, Double currHighestPrice);

    void updateBidStatusByBidder(String itemId, String bidderId, String bidStatus,
            Double bidderOfferPrice);

}
