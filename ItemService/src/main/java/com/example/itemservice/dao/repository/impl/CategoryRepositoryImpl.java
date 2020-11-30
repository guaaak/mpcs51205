package com.example.itemservice.dao.repository.impl;

import com.example.itemservice.dao.domain.Category;
import com.example.itemservice.dao.repository.CategoryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author lukewwang
 * @time 2020/11/30 11:53 PM
 */
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {


    private final MongoTemplate mongoTemplate;

    @Autowired
    public CategoryRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(Category category) {

        mongoTemplate.save(category);

    }

    @Override
    public void update(String categoryId, Category category) {

        Query query = new Query();
        Criteria criteria = new Criteria().where("categoryId").is(categoryId);
        query.addCriteria(criteria);

        Update update = new Update();
        update.set("categoryId", category.getCategoryId());
        update.set("categoryName", category.getCategoryName());

        mongoTemplate.upsert(query, update, Category.class);
    }

    @Override
    public void delete(String categoryId) {

        Query query = new Query();
        Criteria criteria = new Criteria().where("categoryId").is(categoryId);
        query.addCriteria(criteria);

        mongoTemplate.remove(query, Category.class);

    }

    @Override
    public Category getById(String categoryId) {

        Query query = new Query();
        Criteria criteria = new Criteria().where("categoryId").is(categoryId);
        query.addCriteria(criteria);

        return mongoTemplate.findOne(query, Category.class);
    }

    @Override
    public List<Category> getAllCategory() {

        return mongoTemplate.findAll(Category.class);
    }
}
