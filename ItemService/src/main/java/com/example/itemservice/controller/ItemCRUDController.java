package com.example.itemservice.controller;

import com.example.itemservice.api.ItemCRUDApi;
import com.example.itemservice.biz.ItemCRUDService;
import com.example.itemservice.dao.domain.Item;
import com.example.itemservice.model.RequestFlagItem;
import com.example.itemservice.model.RequestItemBody;
import com.example.itemservice.model.ResponseFlaggedItem;
import com.example.itemservice.model.ResponseFlaggedList;
import com.example.itemservice.model.ResponseItemBody;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lukewwang
 * @time 2020/11/23 1:59 PM
 */
@RestController
public class ItemCRUDController implements ItemCRUDApi {


    @Autowired
    private ItemCRUDService itemCRUDService;


    @Override
    public ResponseItemBody createItem(RequestItemBody requestItemBody) {

        Item newItem = new Item();
        newItem.setId(String.valueOf(itemCRUDService.getNextSequenceID()));
        newItem.setCategoryId(requestItemBody.getCategoryId());
        newItem.setDescription(requestItemBody.getDescription());
        newItem.setFlagged(!requestItemBody.getIsFlagged().equals("false"));
        newItem.setName(requestItemBody.getName());
        newItem.setQuantity(requestItemBody.getQuantity());
        newItem.setRatings(requestItemBody.getRatings());

        itemCRUDService.createItem(newItem);

        ResponseItemBody response = new ResponseItemBody();
        response.setItem(newItem);
        return response;
    }

    @Override
    public Item getItemByID(String itemId) {
        return itemCRUDService.getItemById(itemId);
    }

    @Override
    public List<Item> getItemListByKeyword(String keyword) {
        return itemCRUDService.getItemListByKeyword(keyword);
    }

    @Override
    public List<Item> getItemListByCategory(String categoryId) {
        return itemCRUDService.getItemListByCategory(categoryId);
    }

    @Override
    public ResponseFlaggedList getItemListByFlag() {

        List<Item> list = itemCRUDService.getFlaggedItems();

        ResponseFlaggedList flaggedList = new ResponseFlaggedList();
        List<ResponseFlaggedItem> flagItemList = new ArrayList<>();

        for (Item item : list) {
            ResponseFlaggedItem flaggedItem = new ResponseFlaggedItem();
            flaggedItem.setItemId(item.getId());
            flaggedItem.setItemName(item.getName());
            flaggedItem.setFlaggedByUserIdList(item.getFlagUsers());
            flagItemList.add(flaggedItem);
        }

        flaggedList.setFlaggedItemList(flagItemList);

        return flaggedList;
    }

    @Override
    public Boolean flagItem(RequestFlagItem requestFlagItem) {

        itemCRUDService.flagItem(requestFlagItem.getItemID(), requestFlagItem.getUserID());

        return true;
    }

    @Override
    public Boolean deleteItem(String itemId) {

        itemCRUDService.deleteItem(itemId);
        return true;
    }


}
