package com.example.biddingservice.dao.repository.impl;

import com.example.biddingservice.constants.BidStatus;
import com.example.biddingservice.dao.domain.BidRecord;
import com.example.biddingservice.dao.repository.BidRecordRepository;
import com.example.biddingservice.model.RequestInitBidBody;
import com.example.biddingservice.model.UpdateBuyNow;
import com.example.biddingservice.util.TimeHandler;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author lukewwang
 * @time 2020/11/25 5:28 PM
 */
@Repository
@Scope("prototype")
public class BidRecordRepositoryImpl implements BidRecordRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public BidRecordRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveBid(RequestInitBidBody bidInfo) {
        BidRecord bidRecord = new BidRecord();
        bidRecord.setItemId(bidInfo.getItemId());
        bidRecord.setSellerId(bidInfo.getSellerId());
        bidRecord.setCanBuyNow(bidInfo.getCanBuyNow().equals("true"));
        bidRecord.setBuyNowPrice(bidInfo.getBuyNowPrice());
        bidRecord.setInitPrice(bidInfo.getInitPrice());

        Date currDate = TimeHandler.getFixedCurrentTime();
        Date startDate = TimeHandler.toDate(bidInfo.getStartTime());
        if (currDate.compareTo(startDate) >= 0) {
            bidRecord.setBidStatus(BidStatus.Open);
        } else {
            bidRecord.setBidStatus(BidStatus.Ready);
        }

        bidRecord.setStartTime(bidInfo.getStartTime());
        bidRecord.setEndTime(bidInfo.getEndTime());

        mongoTemplate.save(bidRecord);
    }

    @Override
    public BidRecord getBid(String itemId) {
        Query query = new Query();
        Criteria criteria = new Criteria().where("itemId").is(itemId);
        query.addCriteria(criteria);

        return mongoTemplate.findOne(query, BidRecord.class);
    }

    @Override
    public void updateBidStatus(String itemID, BidStatus bidStatus) {

        Query query = new Query();
        Criteria criteria = new Criteria().where("itemId").is(itemID);
        query.addCriteria(criteria);

        Update update = new Update();
        update.set("bidStatus", bidStatus);

        mongoTemplate.upsert(query, update, BidRecord.class);
    }

    @Override
    public void updateBuyNowOption(String itemId, UpdateBuyNow updateBuyNow) {

        Query query = new Query();
        Criteria criteria = new Criteria().where("itemId").is(itemId);
        query.addCriteria(criteria);

        System.out.println(updateBuyNow);

        Update update = new Update();
        update.set("canBuyNow", updateBuyNow.getCanBuyNow().equals("true"));
        update.set("buyNowPrice", updateBuyNow.getBuyNowPrice());

        mongoTemplate.upsert(query, update, BidRecord.class);
    }

    @Override
    public void addNewOffer(String itemId, String userId, double offer) {

        Query query = new Query();
        Criteria criteria = new Criteria().where("itemId").is(itemId);
        query.addCriteria(criteria);

        BidRecord bidRecord = mongoTemplate.findOne(query, BidRecord.class);
        LinkedHashMap<String, Double> offers = bidRecord.getPrevBid();
        offers.put(userId, offer);

        Update update = new Update();
        update.set("prevBid", offers);
        update.set("winnerId", userId);
        update.set("finalOffer", offer);
        update.set("bidCount", bidRecord.getBidCount() + 1);

        mongoTemplate.upsert(query, update, BidRecord.class);
    }

    @Override
    public void addWinnerAndFinalOffer(String itemId, String userId, double offer) {

        Query query = new Query();
        Criteria criteria = new Criteria().where("itemId").is(itemId);
        query.addCriteria(criteria);

        Update update = new Update();
        update.set("winnerId", userId);
        update.set("finalOffer", offer);

        mongoTemplate.upsert(query, update, BidRecord.class);
    }

    @Override
    public List<BidRecord> getActiveBids() {

        Query query = new Query();
        Criteria criteria = new Criteria().where("BidStatus").is(BidStatus.Open);
        query.addCriteria(criteria);

        return mongoTemplate.find(query, BidRecord.class);
    }

    @Override
    public List<BidRecord> getClosedBids() {

        Query query = new Query();
        Criteria criteria = new Criteria().where("BidStatus").is(BidStatus.Closed);
        query.addCriteria(criteria);

        return mongoTemplate.find(query, BidRecord.class);
    }

    @Override
    public List<BidRecord> getBidBySeller(String sellerId) {

        Query query = new Query();
        Criteria criteria = new Criteria().where("sellerId").is(sellerId);
        query.addCriteria(criteria);

        return mongoTemplate.find(query, BidRecord.class);
    }

}
