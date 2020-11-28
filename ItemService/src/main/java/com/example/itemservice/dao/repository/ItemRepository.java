package com.example.itemservice.dao.repository;

import com.example.itemservice.dao.domain.Item;
import com.example.itemservice.model.UpdateItemBody;
import java.util.List;

/**
 * @author lukewwang
 * @time 2020/11/23 1:09 AM
 */
public interface ItemRepository {

    Item saveItem(Item item);

    Item getItemByID(String itemID);

    List<Item> getItemListByKeyword(String keyword);

    List<Item> getItemListByCategory(String category);

    void deleteItem(String itemID);

    List<Item> getItemListByFlag(boolean isFlagged);

    void flagItem(String itemID, String userID);

    void updateItem(String itemId, UpdateItemBody updateItemBody);


}
