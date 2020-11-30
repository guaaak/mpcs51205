package com.example.biddingservice.biz;

import com.example.biddingservice.dao.domain.BidRecord;
import com.example.biddingservice.dao.domain.CountDownRecord;
import com.example.biddingservice.model.RequestInitBidBody;
import com.example.biddingservice.model.UpdateBuyNow;
import java.util.List;

/**
 * @author lukewwang
 * @time 2020/11/25 5:02 PM
 */
public interface BidProcessService {

    void initBidProcess(RequestInitBidBody bidBody);

    void startBidProcess(String itemId);

    void closeBidProcess(String itemId);

    boolean closeBidByAdmin(String itemID);

    void placeNewBid(String itemId, String bidderId, double newPrice);

    void updateBuyNowOption(String itemId, UpdateBuyNow updateBuyNow);

    BidRecord getBidRecord(String itemId);

    List<BidRecord> getActiveBidsList();

    List<BidRecord> getClosedBidsList();

    void createCountDown(CountDownRecord countDownRecord);

    List<CountDownRecord> getCountDownByUserId(String userID);

    List<BidRecord> getBidRecordsBySellerId(String sellerId);


}
