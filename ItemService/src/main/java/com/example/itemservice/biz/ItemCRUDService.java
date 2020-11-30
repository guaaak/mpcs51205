package com.example.itemservice.biz;

import com.example.itemservice.dao.domain.Category;
import com.example.itemservice.dao.domain.Item;
import com.example.itemservice.model.UpdateItemBody;
import java.util.List;

/**
 * @author lukewwang
 * @time 2020/11/23 1:54 PM
 */
public interface ItemCRUDService {

    void createItem(Item item);

    Item getItemById(String itemId);

    long getNextSequenceID();

    List<Item> getItemListByKeyword(String keyword);

    List<Item> getItemListByCategory(String categoryId);

    boolean flagItem(String itemID, String userID);

    List<Item> getFlaggedItems();

    boolean deleteItem(String itemId);

    void updateItem(String itemId, UpdateItemBody updateItemBody);

    void saveCategory(Category category);

    void updateCategory(String categoryId, Category category);

    void deleteCategory(String categoryId);

    Category getCategoryById(String categoryId);

    List<Category> getAllCategory();

}
