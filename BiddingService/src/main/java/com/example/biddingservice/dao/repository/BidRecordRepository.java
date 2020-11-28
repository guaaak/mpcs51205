package com.example.biddingservice.dao.repository;

import com.example.biddingservice.constants.BidStatus;
import com.example.biddingservice.dao.domain.BidRecord;
import com.example.biddingservice.model.RequestInitBidBody;
import com.example.biddingservice.model.UpdateBuyNow;
import java.util.List;

/**
 * @author lukewwang
 * @time 2020/11/25 5:00 PM
 */
public interface BidRecordRepository {

    void saveBid(RequestInitBidBody bidInfo);

    BidRecord getBid(String itemId);

    void updateBidStatus(String itemId, BidStatus bidStatus);

    void updateBuyNowOption(String itemId, UpdateBuyNow updateBuyNow);

    void addNewOffer(String itemId, String userId, double offer);

    void addWinnerAndFinalOffer(String itemId, String userId, double offer);

    List<BidRecord> getActiveBids();

}
