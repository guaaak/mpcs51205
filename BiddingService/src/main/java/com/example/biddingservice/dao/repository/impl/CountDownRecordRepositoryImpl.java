package com.example.biddingservice.dao.repository.impl;

import com.example.biddingservice.dao.domain.CountDownRecord;
import com.example.biddingservice.dao.repository.CountDownRecordRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author lukewwang
 * @time 2020/11/29 3:44 PM
 */
@Repository
public class CountDownRecordRepositoryImpl implements CountDownRecordRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CountDownRecordRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveRecord(CountDownRecord countDownRecord) {
        mongoTemplate.save(countDownRecord);

    }

    @Override
    public List<CountDownRecord> getRecordByUserId(String userId) {
        Query query = new Query();
        Criteria criteria = new Criteria().where("userId").is(userId);
        query.addCriteria(criteria);

        return mongoTemplate.find(query, CountDownRecord.class);

    }
}
