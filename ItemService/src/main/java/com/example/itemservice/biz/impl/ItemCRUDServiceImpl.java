package com.example.itemservice.biz.impl;

import com.example.itemservice.biz.ItemCRUDService;
import com.example.itemservice.dao.domain.Item;
import com.example.itemservice.dao.repository.ItemRepository;
import com.example.itemservice.dao.repository.MetaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lukewwang
 * @time 2020/11/23 1:54 PM
 */
@Service
public class ItemCRUDServiceImpl implements ItemCRUDService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MetaRepository metaRepository;

    @Override
    public void createItem(Item item) {
        itemRepository.saveItem(item);
    }

    @Override
    public Item getItemById(String itemId) {
        return itemRepository.getItemByID(itemId);
    }

    @Override
    public long getNextSequenceID() {
        long newId = metaRepository.getStartSequence();
        metaRepository.updateSequence(newId + 1);
        return newId;
    }

    @Override
    public List<Item> getItemListByKeyword(String keyword) {
        return itemRepository.getItemListByKeyword(keyword);
    }

    @Override
    public List<Item> getItemListByCategory(String categoryId) {
        return itemRepository.getItemListByCategory(categoryId);
    }

    @Override
    public boolean flagItem(String itemID, String userID) {
        itemRepository.flagItem(itemID, userID);
        return true;
    }

    @Override
    public List<Item> getFlaggedItems() {
        return itemRepository.getItemListByFlag(true);
    }

    @Override
    public boolean deleteItem(String itemId) {
        itemRepository.deleteItem(itemId);
        return true;
    }
}
