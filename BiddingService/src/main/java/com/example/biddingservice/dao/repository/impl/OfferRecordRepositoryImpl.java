package com.example.biddingservice.dao.repository.impl;

import com.example.biddingservice.dao.domain.OfferRecord;
import com.example.biddingservice.dao.repository.OfferRecordRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author lukewwang
 * @time 2020/11/28 4:12 PM
 */
@Repository
public class OfferRecordRepositoryImpl implements OfferRecordRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public OfferRecordRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveRecord(OfferRecord offerRecord) {
        mongoTemplate.save(offerRecord);
    }

    @Override
    public void updateBidStatusByOthers(String itemId, String bidStatus, Double currHighestPrice) {
        Query query = new Query();
        Criteria criteria = new Criteria().where("itemId").is(itemId);
        query.addCriteria(criteria);

        Update update = new Update();
        update.set("bidStatus", bidStatus);
        update.set("currHighestPrice", currHighestPrice);

        mongoTemplate.updateMulti(query, update, OfferRecord.class);
    }

    @Override
    public void updateBidStatusByBidder(String itemId, String bidderId, String bidStatus,
            Double bidderOfferPrice) {

        Query query = new Query();
        Criteria criteria = new Criteria().where("itemId").is(itemId);
        criteria.andOperator(Criteria.where("bidderId").is(bidderId));
        query.addCriteria(criteria);

        Update update = new Update();
        update.set("bidderOfferPrice", bidderOfferPrice);
        update.set("currHighestPrice", bidderOfferPrice);
        update.set("bidStatus", bidStatus);

        mongoTemplate.upsert(query, update, OfferRecord.class);
    }

    @Override
    public List<OfferRecord> getPrevOffersByUserId(String userId) {

        Query query = new Query();
        Criteria criteria = new Criteria().where("bidderId").is(userId);
        query.addCriteria(criteria);

        return mongoTemplate.find(query, OfferRecord.class);
    }
}
