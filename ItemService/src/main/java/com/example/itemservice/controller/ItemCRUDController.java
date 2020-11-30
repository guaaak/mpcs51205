package com.example.itemservice.controller;

import com.example.itemservice.api.ItemCRUDApi;
import com.example.itemservice.biz.ItemCRUDService;
import com.example.itemservice.dao.domain.Item;
import com.example.itemservice.model.BidRecord;
import com.example.itemservice.model.BidStatus;
import com.example.itemservice.model.CountDownInfo;
import com.example.itemservice.model.RequestFlagItem;
import com.example.itemservice.model.RequestItemBody;
import com.example.itemservice.model.ResponseFlaggedItem;
import com.example.itemservice.model.ResponseFlaggedList;
import com.example.itemservice.model.ResponseItemBody;
import com.example.itemservice.model.ResponseSearchOnItem;
import com.example.itemservice.model.ResponseSearchResults;
import com.example.itemservice.model.SimpleResponse;
import com.example.itemservice.model.UpdateItemBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
        newItem.setQuantity((int) requestItemBody.getQuantity());
        newItem.setRatings(requestItemBody.getRatings());
        newItem.setShippingCosts(requestItemBody.getShippingCosts());
        System.out.println(requestItemBody.getQuantity());

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
    public ResponseSearchResults getItemListByKeyword(String keyword) {

        List<Item> itemList = itemCRUDService.getItemListByKeyword(keyword);

        System.out.println(itemList);

        List<ResponseSearchOnItem> res = new ArrayList<>();

        for (Item item : itemList) {
            RestTemplate restT = new RestTemplate();
            // 通过 Jackson JSON processing library 直接将返回值绑定到对象
            try {
                BidRecord bidRecord = restT
                        .getForObject(
                                "http://biddingservice:9090/auction/bidding/bid/" + item.getId(),
                                BidRecord.class);

                System.out.println(bidRecord);

                if (bidRecord.getBidStatus() == BidStatus.Closed) {
                    continue;
                }
                ResponseSearchOnItem response = new ResponseSearchOnItem();
                response.setBidCount(bidRecord.getBidCount());
                response.setBidStatus(bidRecord.getBidStatus());
                response.setCanBuyNow(bidRecord.isCanBuyNow());
                response.setStartTime(bidRecord.getStartTime());
                response.setEndTime(bidRecord.getEndTime());
                response.setBuyNowPrice(bidRecord.getBuyNowPrice());
                response.setSellerId(bidRecord.getSellerId());
                response.setItem(item);
                res.add(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(res);

        ResponseSearchResults response = new ResponseSearchResults();
        response.setSearchResults(res);
        return response;
    }

    @Override
    public ResponseSearchResults getItemListByCategory(String categoryId) {

        List<Item> itemList = itemCRUDService.getItemListByCategory(categoryId);

        System.out.println(itemList);

        List<ResponseSearchOnItem> res = new ArrayList<>();

        for (Item item : itemList) {
            RestTemplate restT = new RestTemplate();
            // 通过 Jackson JSON processing library 直接将返回值绑定到对象
            BidRecord bidRecord = restT
                    .getForObject("http://biddingservice:9090/auction/bidding/bid/" + item.getId(),
                            BidRecord.class);
            if (bidRecord.getBidStatus() == BidStatus.Closed) {
                continue;
            }
            ResponseSearchOnItem response = new ResponseSearchOnItem();
            response.setBidCount(bidRecord.getBidCount());
            response.setBidStatus(bidRecord.getBidStatus());
            response.setCanBuyNow(bidRecord.isCanBuyNow());
            response.setStartTime(bidRecord.getStartTime());
            response.setEndTime(bidRecord.getEndTime());
            response.setBuyNowPrice(bidRecord.getBuyNowPrice());
            response.setSellerId(bidRecord.getSellerId());
            response.setItem(item);
            res.add(response);
        }

        ResponseSearchResults response = new ResponseSearchResults();
        response.setSearchResults(res);
        return response;
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
    public SimpleResponse flagItem(RequestFlagItem requestFlagItem) {

        itemCRUDService.flagItem(requestFlagItem.getItemID(), requestFlagItem.getUserID());

        return new SimpleResponse();
    }

    @Override
    public SimpleResponse deleteItem(String itemId) {

        SimpleResponse simpleResponse = new SimpleResponse();

        RestTemplate restT = new RestTemplate();
        // 通过 Jackson JSON processing library 直接将返回值绑定到对象
        BidRecord bidRecord = restT
                .getForObject("http://biddingservice:9090/auction/bidding/bid/" + itemId,
                        BidRecord.class);

        System.out.println(bidRecord);

        if (bidRecord == null) {
            simpleResponse.setSuccess(false);
        } else {
            if (bidRecord.getPrevBid().size() != 0) {
                simpleResponse.setSuccess(false);
            } else {
                itemCRUDService.deleteItem(itemId);
                restT = new RestTemplate();
                restT.getForObject(
                        "http://biddingservice:9090/auction/bidding/closeEmptyBid/" + itemId,
                        String.class);
                simpleResponse.setSuccess(true);
            }
        }
        return simpleResponse;
    }

    @Override
    public SimpleResponse updateItem(String itemId, UpdateItemBody updateItemBody) {

        itemCRUDService.updateItem(itemId, updateItemBody);

        RestTemplate restT = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> params = new HashMap<>();
        params.put("canBuyNow", updateItemBody.getCanBuyNow());
        String url = "http://biddingservice:9090/auction/bidding/buyNowUpdate/" + itemId;

        String value = "";
        try {
            value = mapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
        ResponseEntity<String> response = restT.postForEntity(url, requestEntity, String.class);
        System.out.println("post json : " + response);
        return new SimpleResponse();
    }

    @Override
    public List<CountDownInfo> getCountdownInfo(String keyword) {

        List<Item> itemList = itemCRUDService.getItemListByKeyword(keyword);

        List<CountDownInfo> res = new ArrayList<>();

        for (Item item : itemList) {
            RestTemplate restT = new RestTemplate();
            // 通过 Jackson JSON processing library 直接将返回值绑定到对象
            try {
//                BidRecord bidRecord = restT
//                        .getForObject("http://localhost:9090/auction/bidding/bid/" + item.getId(),
//                                BidRecord.class);

                BidRecord bidRecord = restT
                        .getForObject(
                                "http://biddingservice:9090/auction/bidding/bid/" + item.getId(),
                                BidRecord.class);
                if (bidRecord.getBidStatus() != BidStatus.Ready) {
                    continue;
                }
                CountDownInfo countDownInfo = new CountDownInfo();
                countDownInfo.setItemID(bidRecord.getItemId());
                countDownInfo.setItemName(item.getName());
                countDownInfo.setStartTime(bidRecord.getStartTime());
                res.add(countDownInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return res;
    }


}
