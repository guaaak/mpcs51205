package com.example.itemservice.dao.repository;

import com.example.itemservice.dao.domain.Category;
import java.util.List;

/**
 * @author lukewwang
 * @time 2020/11/30 11:49 PM
 */
public interface CategoryRepository {

    void save(Category category);

    void update(String categoryId, Category category);

    void delete(String categoryId);

    Category getById(String categoryId);

    List<Category> getAllCategory();


}
