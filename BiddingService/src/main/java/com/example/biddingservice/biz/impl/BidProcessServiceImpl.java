package com.example.biddingservice.biz.impl;

import com.example.biddingservice.biz.BidProcessService;
import com.example.biddingservice.constants.BidStatus;
import com.example.biddingservice.dao.domain.BidRecord;
import com.example.biddingservice.dao.domain.CountDownRecord;
import com.example.biddingservice.dao.repository.BidRecordRepository;
import com.example.biddingservice.dao.repository.CountDownRecordRepository;
import com.example.biddingservice.dao.repository.OfferRecordRepository;
import com.example.biddingservice.model.RequestInitBidBody;
import com.example.biddingservice.model.UpdateBuyNow;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author lukewwang
 * @time 2020/11/25 7:46 PM
 */
@Service
@Scope("prototype")
public class BidProcessServiceImpl implements BidProcessService {

    @Autowired
    private BidRecordRepository bidRecordRepository;

    @Autowired
    private OfferRecordRepository offerRecordRepository;

    @Autowired
    private CountDownRecordRepository countDownRecordRepository;

    @Override
    public void initBidProcess(RequestInitBidBody bidBody) {
        bidRecordRepository.saveBid(bidBody);
    }

    @Override
    public void startBidProcess(String itemId) {
        bidRecordRepository.updateBidStatus(itemId, BidStatus.Open);
    }

    @Override
    public void closeBidProcess(String itemId) {
        bidRecordRepository.updateBidStatus(itemId, BidStatus.Closed);
    }

    @Override
    public boolean closeBidByAdmin(String itemID) {
        bidRecordRepository.updateBidStatus(itemID, BidStatus.Closed);
        return true;
    }

    @Override
    public void placeNewBid(String itemId, String bidderId, double newPrice) {
        bidRecordRepository.addNewOffer(itemId, bidderId, newPrice);

        offerRecordRepository.updateBidStatusByBidder(itemId, bidderId, "bidding", newPrice);
        offerRecordRepository.updateBidStatusByOthers(itemId, "bidding", newPrice);
    }

    @Override
    public void updateBuyNowOption(String itemId, UpdateBuyNow updateBuyNow) {
        bidRecordRepository.updateBuyNowOption(itemId, updateBuyNow);
    }

    @Override
    public BidRecord getBidRecord(String itemId) {
        return bidRecordRepository.getBid(itemId);
    }

    @Override
    public List<BidRecord> getActiveBidsList() {
        return bidRecordRepository.getActiveBids();
    }

    @Override
    public List<BidRecord> getClosedBidsList() {
        return bidRecordRepository.getClosedBids();
    }

    @Override
    public void createCountDown(CountDownRecord countDownRecord) {
        countDownRecordRepository.saveRecord(countDownRecord);
    }

    @Override
    public List<CountDownRecord> getCountDownByUserId(String userID) {
        return countDownRecordRepository.getRecordByUserId(userID);
    }

    @Override
    public List<BidRecord> getBidRecordsBySellerId(String sellerId) {
        return bidRecordRepository.getBidBySeller(sellerId);
    }
}
