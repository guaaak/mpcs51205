package com.example.itemservice.dao.repository.impl;

import com.example.itemservice.dao.domain.ItemMetaData;
import com.example.itemservice.dao.repository.MetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author lukewwang
 * @time 2020/11/23 2:41 PM
 */
@Repository
public class MetaRepositoryImpl implements MetaRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MetaRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public long getStartSequence() {

        Query query = new Query();
        Criteria criteria = new Criteria().where("tag").is(0);
        query.addCriteria(criteria);
        ItemMetaData itemMetaData = mongoTemplate.findOne(query, ItemMetaData.class);
        if (itemMetaData == null) {
            itemMetaData = new ItemMetaData();
            mongoTemplate.save(itemMetaData);
        }
        return mongoTemplate.findOne(query, ItemMetaData.class).getIdSequence();
    }

    @Override
    public void updateSequence(long newSequence) {

        Query query = new Query();
        Criteria criteria = new Criteria().where("tag").is(0);
        query.addCriteria(criteria);

        Update update = new Update();
        update.set("idSequence", newSequence);

        mongoTemplate.upsert(query, update, ItemMetaData.class);
    }
}
