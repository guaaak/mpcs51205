package com.example.biddingservice.dao.repository.impl;

import com.example.biddingservice.dao.domain.Item;
import com.example.biddingservice.dao.repository.ItemRepository;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author lukewwang
 * @time 2020/11/23 1:09 AM
 */

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ItemRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Item saveItem(Item item) {
        return mongoTemplate.save(item);
    }

    @Override
    public Item getItemByID(String itemID) {
        Query query = new Query();
        Criteria criteria = new Criteria().where("id").is(itemID);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query, Item.class);
    }

    @Override
    public List<Item> getItemListByKeyword(String keyword) {
        Pattern pattern = Pattern.compile("^.*" + keyword + ".*$", Pattern.CASE_INSENSITIVE);
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("name").regex(pattern),
                Criteria.where("description").regex(pattern));
        query.addCriteria(criteria);

        return mongoTemplate.find(query, Item.class);
    }

    @Override
    public List<Item> getItemListByCategory(String categoryId) {
        Query query = new Query();
        Criteria criteria = new Criteria().where("categoryId").is(categoryId);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Item.class);
    }

    @Override
    public void deleteItem(String itemID) {
        Query query = new Query();
        Criteria criteria = new Criteria().where("id").is(itemID);
        query.addCriteria(criteria);
        mongoTemplate.remove(query, Item.class);
    }

    @Override
    public List<Item> getItemListByFlag(boolean isFlagged) {
        Query query = new Query();
        Criteria criteria = new Criteria().where("isFlagged").is(isFlagged);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Item.class);
    }

    @Override
    public void flagItem(String itemID, String userID) {
        Query query = new Query();
        Criteria criteria = new Criteria().where("id").is(itemID);
        query.addCriteria(criteria);
        Item item = mongoTemplate.findOne(query, Item.class);

        item.setFlagged(true);
        List<String> flagUsers = item.getFlagUsers();

        if (!flagUsers.contains(userID)) {
            flagUsers.add(userID);
        }
        item.setFlagUsers(flagUsers);

        mongoTemplate.save(item);
    }
}
