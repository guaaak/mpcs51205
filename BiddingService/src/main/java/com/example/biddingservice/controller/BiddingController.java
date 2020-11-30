package com.example.biddingservice.controller;

import com.example.biddingservice.api.BiddingApi;
import com.example.biddingservice.biz.BidProcessService;
import com.example.biddingservice.biz.OfferProcessService;
import com.example.biddingservice.constants.BidStatus;
import com.example.biddingservice.dao.domain.BidRecord;
import com.example.biddingservice.dao.domain.CountDownRecord;
import com.example.biddingservice.model.AuctionWindowResponse;
import com.example.biddingservice.model.MeetCriteriaResponse;
import com.example.biddingservice.model.PrevAuctionResponse;
import com.example.biddingservice.model.RequestInitBidBody;
import com.example.biddingservice.model.RequestNewOfferBody;
import com.example.biddingservice.model.ResponseActiveBidList;
import com.example.biddingservice.model.ResponseClosedBidList;
import com.example.biddingservice.model.SimpleResponse;
import com.example.biddingservice.model.UpdateBuyNow;
import com.example.biddingservice.model.UserInfoBody;
import com.example.biddingservice.util.TimeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author lukewwang
 * @time 2020/11/25 8:03 PM
 */
@RestController
@Scope("prototype")
public class BiddingController implements BiddingApi {

    @Autowired
    private BidProcessService bidProcessService;

    @Autowired
    private OfferProcessService offerProcessService;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);


    @Override
    public SimpleResponse makeNewOffer(RequestNewOfferBody requestNewOfferBody) {

        bidProcessService
                .placeNewBid(requestNewOfferBody.getItemId(), requestNewOfferBody.getUserId(),
                        requestNewOfferBody.getNewBidPrice());

        BidRecord bidRecord = bidProcessService.getBidRecord(requestNewOfferBody.getItemId());

        // call notification service to send emails to seller
        try {
            callNotificationToSeller(bidRecord.getItemId(), bidRecord.getSellerId(),
                    requestNewOfferBody.getUserId(), requestNewOfferBody.getNewBidPrice());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // call notification to alert other bidders
        List<String> biddersToNotify = new ArrayList<>();
        for (String bidderId : bidRecord.getPrevBid().keySet()) {
            if (!bidderId.equals(requestNewOfferBody.getUserId())) {
                biddersToNotify.add(bidderId);
            }
        }
        try {
            callNotificationToBidders(bidRecord.getItemId(), biddersToNotify,
                    requestNewOfferBody.getUserId(), requestNewOfferBody.getNewBidPrice());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new SimpleResponse();
    }

    @Override
    public SimpleResponse initBid(RequestInitBidBody requestInitBidBody) {

        bidProcessService.initBidProcess(requestInitBidBody);

        Date startDate = TimeHandler.toDate(requestInitBidBody.getStartTime());
        Long delay = TimeHandler.getTimeDelay(startDate, new Date()) / 1000;
        Date endDate = TimeHandler.toDate(requestInitBidBody.getEndTime());

        Long delayFromEnd = TimeHandler.getTimeDelay(endDate, new Date()) / 1000;

        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(() -> {

            BidRecord bidRecord = bidProcessService.getBidRecord(requestInitBidBody.getItemId());

            System.out.println("start bidding on " + requestInitBidBody.getItemId());

            if (bidRecord.getBidStatus() != BidStatus.Closed) {

                bidProcessService.startBidProcess(requestInitBidBody.getItemId());

                Date currTime = TimeHandler.getFixedCurrentTime();
                Long lag = TimeHandler.getTimeDelay(endDate, currTime) / 1000;
                if ((lag - 60) <= 0 || currTime.compareTo(endDate) >= 0) {
                    bidProcessService.closeBidProcess(requestInitBidBody.getItemId());
                    System.out.println("closing bid on " + requestInitBidBody.getItemId());

                    BidRecord winnerBid = bidProcessService
                            .getBidRecord(requestInitBidBody.getItemId());
                    System.out.println("Winner is " + winnerBid.getWinnerId() + " with final offer "
                            + winnerBid.getFinalOffer());
                    offerProcessService.updateBidStatusByOthers(winnerBid.getItemId(), "closed",
                            winnerBid.getFinalOffer());

                    offerProcessService
                            .updateBidStatusByBidder(winnerBid.getItemId(), winnerBid.getWinnerId(),
                                    "won", winnerBid.getFinalOffer());

                    //TO_DO call notification to send emails to winner and seller
                    System.out.println("sending emails to winner and seller...");
                    //TO_DO add item to the winners cart
                    System.out.println("adding item to winner's cart");
                    try {
                        callBuyingToAddToCart(bidRecord.getWinnerId(), bidRecord.getItemId(),
                                bidRecord.getFinalOffer());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    executorService.shutdown();
                }

            } else {
                executorService.shutdown();
            }


        }, delay - 60, 60, TimeUnit.SECONDS);

        // less than a minute
        if (delayFromEnd >= 120) {

            Long delayForAlertLastMin = (TimeHandler.getTimeDelay(endDate, new Date()) / 1000) - 60;
            ScheduledFuture<?> future_lastMinute = executorService.scheduleAtFixedRate(() -> {

                BidRecord bidRecord = bidProcessService
                        .getBidRecord(requestInitBidBody.getItemId());

                List<String> usersToNotify = new ArrayList<>();
                for (String bidderId : bidRecord.getPrevBid().keySet()) {
                    usersToNotify.add(bidderId);
                }
                usersToNotify.add(bidRecord.getSellerId());

                // sending alerts only if the bid is open; in case admin closed it early
                if (bidRecord.getBidStatus() != BidStatus.Closed) {
                    //TO_DO call notification to send emails to bidders and seller
                    System.out.println(
                            "sending last minute alert..." + requestInitBidBody.getItemId());
                    try {
                        callNotificationOnPredeterminedTiming(bidRecord.getItemId(), usersToNotify,
                                60);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                } else {
                    executorService.shutdown();
                }

            }, delayForAlertLastMin - 60, 1000000000, TimeUnit.SECONDS);
        }

        // less than 5 minutes
        if (delayFromEnd >= 360) {

            Long delayForAlertLast5Min =
                    (TimeHandler.getTimeDelay(endDate, new Date()) / 1000) - 300;
            ScheduledFuture<?> future_lastMinute = executorService.scheduleAtFixedRate(() -> {
                BidRecord bidRecord = bidProcessService
                        .getBidRecord(requestInitBidBody.getItemId());

                List<String> usersToNotify = new ArrayList<>();
                for (String bidderId : bidRecord.getPrevBid().keySet()) {
                    usersToNotify.add(bidderId);
                }
                usersToNotify.add(bidRecord.getSellerId());

                if (bidRecord.getBidStatus() != BidStatus.Closed) {
                    //TO_DO call notification to send emails to bidders and seller
                    System.out.println(
                            "sending last 5 minutes alert..." + requestInitBidBody.getItemId());

                    try {
                        callNotificationOnPredeterminedTiming(bidRecord.getItemId(), usersToNotify,
                                300);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                } else {
                    executorService.shutdown();
                }

            }, delayForAlertLast5Min - 60, 1000000000, TimeUnit.SECONDS);
        }

        return new SimpleResponse();
    }

    @Override
    public SimpleResponse closeBidByAdmin(String itemID) {

        bidProcessService.closeBidProcess(itemID);
        System.out.println("closing bid on " + itemID);

        BidRecord bidRecord = bidProcessService.getBidRecord(itemID);
        System.out.println("Winner is " + bidRecord.getWinnerId() + " with final offer " + bidRecord
                .getFinalOffer());
        offerProcessService.updateBidStatusByOthers(bidRecord.getItemId(), "closed",
                bidRecord.getFinalOffer());
        offerProcessService
                .updateBidStatusByBidder(bidRecord.getItemId(), bidRecord.getWinnerId(), "won",
                        bidRecord.getFinalOffer());

        //TO_DO call notification to send emails to winner and seller
        System.out.println("sending emails to winner and seller...");
        //TO_DO add item to the winners cart
        System.out.println("adding item to winner's cart");
        try {
            callBuyingToAddToCart(bidRecord.getWinnerId(), bidRecord.getItemId(),
                    bidRecord.getFinalOffer());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new SimpleResponse();
    }

    @Override
    public SimpleResponse closeEmptyBid(String itemID) {
        bidProcessService.closeBidProcess(itemID);
        return new SimpleResponse();
    }

    @Override
    public ResponseActiveBidList getActiveBidsList() {

        ResponseActiveBidList responseActiveBidList = new ResponseActiveBidList();
        responseActiveBidList.setActiveBidList(bidProcessService.getActiveBidsList());

        return responseActiveBidList;
    }

    @Override
    public ResponseClosedBidList getClosedBidsList() {
        ResponseClosedBidList responseClosedBidList = new ResponseClosedBidList();
        responseClosedBidList.setClosedBidList(bidProcessService.getClosedBidsList());
        return responseClosedBidList;
    }

    @Override
    public BidRecord getBidRecordByItem(String itemId) {
        return bidProcessService.getBidRecord(itemId);
    }

    @Override
    public SimpleResponse closeBidFromBuyNow(String itemId) {

        bidProcessService.closeBidProcess(itemId);
        System.out.println("closing bid from buy now on " + itemId);

        BidRecord bidRecord = bidProcessService.getBidRecord(itemId);
        System.out.println("adding item to winner's cart");
        try {
            callBuyingToAddToCart(bidRecord.getWinnerId(), bidRecord.getItemId(),
                    bidRecord.getFinalOffer());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new SimpleResponse();
    }

    @Override
    public Boolean updateBuyNowOption(String itemId, UpdateBuyNow updateBuyNow) {

        bidProcessService.updateBuyNowOption(itemId, updateBuyNow);

        RestTemplate restT = new RestTemplate();
        // 通过 Jackson JSON processing library 直接将返回值绑定到对象
//        MeetCriteriaResponse response = restT
//                .getForObject(" http://localhost:23333/meetCriteria/?item_id=" + itemId + "&price="
//                        + updateBuyNow.getBuyNowPrice(), MeetCriteriaResponse.class);

        MeetCriteriaResponse response = restT
                .getForObject(
                        " http://usermanagement:23333/meetCriteria/?item_id=" + itemId + "&price="
                                + updateBuyNow.getBuyNowPrice(), MeetCriteriaResponse.class);

        System.out.println(response);

        for (UserInfoBody userInfoBody : response.getAll_users()) {

            try {
                callNotificationForWatchlist(itemId, userInfoBody.getUid());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    @Override
    public SimpleResponse createCountDown(CountDownRecord countDownRecord) {
        bidProcessService.createCountDown(countDownRecord);

        return new SimpleResponse();
    }

    @Override
    public AuctionWindowResponse getCountDown(String userId) {

        List<CountDownRecord> list = bidProcessService.getCountDownByUserId(userId);
        AuctionWindowResponse response = new AuctionWindowResponse();
        response.setWindowList(list);
        return response;
    }

    @Override
    public PrevAuctionResponse getPrevAuctionsList(String userId) {

        PrevAuctionResponse response = new PrevAuctionResponse();
        response.setPrevAuctionList(bidProcessService.getBidRecordsBySellerId(userId));
        return response;
    }

    private void callNotificationToSeller(String itemId, String sellerId, String bidderId,
            Double bidPrice) throws JsonProcessingException {
        RestTemplate restT = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode params = mapper.createObjectNode();
        params.put("item_id", itemId);
        params.put("seller_id", sellerId);
        params.put("user_id", bidderId);
        params.put("bid_price", bidPrice);

//        String url = "http://localhost:5000/notification/seller_alert";
        String url = "http://NotiCon:5000/notification/seller_alert";

        String value = mapper.writeValueAsString(params);
        HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
        ResponseEntity<String> response = restT.postForEntity(url, requestEntity, String.class);
        System.out.println("post json : " + response);
    }

    private void callNotificationToBidders(String itemId, List<String> bidders,
            String highestBidderId,
            Double bidPrice) throws JsonProcessingException {
        RestTemplate restT = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode params = mapper.createObjectNode();
        params.put("item_id", itemId);
        params.put("user_id_list", bidders.toString());
        params.put("highest_bid_user_id", highestBidderId);
        params.put("highest_bid_price", bidPrice);

        //String url = "http://localhost:5000/notification/bid_update";
        String url = "http://NotiCon:5000/notification/bid_update";

        String value = mapper.writeValueAsString(params);
        HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
        ResponseEntity<String> response = restT.postForEntity(url, requestEntity, String.class);
        System.out.println("post json : " + response);
    }


    private void callNotificationOnPredeterminedTiming(String itemId, List<String> bidders,
            int remainSeconds) throws JsonProcessingException {
        RestTemplate restT = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode params = mapper.createObjectNode();
        params.put("item_id", itemId);
        params.put("user_id_list", bidders.toString());
        params.put("remain_time", remainSeconds);

        //String url = "http://localhost:5000/notification/time_alert";
        String url = "http://NotiCon:5000/notification/time_alert";

        String value = mapper.writeValueAsString(params);
        HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
        ResponseEntity<String> response = restT.postForEntity(url, requestEntity, String.class);
        System.out.println("post json : " + response);
    }

    private void callBuyingToAddToCart(String userId, String itemId, double price) {

        RestTemplate restT = new RestTemplate();

//        String item = restT
//                .getForObject("http://localhost:23334/addItemToCart/?uid=" + userId + "&item_id="
//                        + itemId + "&price=" + price, String.class);
        String item = restT
                .getForObject("http://buying:23334/addItemToCart/?uid=" + userId + "&item_id="
                        + itemId + "&price=" + price, String.class);

        System.out.println(item);

    }

    private void callNotificationForWatchlist(String itemId, String userId)
            throws JsonProcessingException {
        RestTemplate restT = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode params = mapper.createObjectNode();
        params.put("item_id", itemId);
        params.put("user_id", userId);

        //String url = "http://localhost:5000/notification/watchlist";
        String url = "http://NotiCon:5000/notification/watchlist";

        String value = mapper.writeValueAsString(params);
        HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
        ResponseEntity<String> response = restT.postForEntity(url, requestEntity, String.class);
        System.out.println("post json : " + response);
    }


}
