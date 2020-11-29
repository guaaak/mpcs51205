package com.example.biddingservice.api;

import com.example.biddingservice.constants.SysConstants;
import com.example.biddingservice.dao.domain.BidRecord;
import com.example.biddingservice.dao.domain.CountDownRecord;
import com.example.biddingservice.model.AuctionWindowResponse;
import com.example.biddingservice.model.RequestInitBidBody;
import com.example.biddingservice.model.RequestNewOfferBody;
import com.example.biddingservice.model.ResponseActiveBidList;
import com.example.biddingservice.model.ResponseClosedBidList;
import com.example.biddingservice.model.SimpleResponse;
import com.example.biddingservice.model.UpdateBuyNow;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author lukewwang
 * @time 2020/11/25 7:35 PM
 */
@RequestMapping(SysConstants.BASE_URL + "/bidding")
public interface BiddingApi {

    @RequestMapping(value = "/newOffer", method = RequestMethod.POST)
    SimpleResponse makeNewOffer(@RequestBody RequestNewOfferBody requestNewOfferBody);

    @RequestMapping(value = "/newBid", method = RequestMethod.POST)
    SimpleResponse initBid(@RequestBody RequestInitBidBody requestInitBidBody);

    // admin forcing a bid on an item to close
    @RequestMapping(value = "/closeBid/{itemID}", method = RequestMethod.POST)
    SimpleResponse closeBidByAdmin(@PathVariable String itemID);

    @RequestMapping(value = "/closeEmptyBid/{itemID}", method = RequestMethod.GET)
    SimpleResponse closeEmptyBid(@PathVariable String itemID);

    @RequestMapping(value = "/activeBids", method = RequestMethod.GET)
    ResponseActiveBidList getActiveBidsList();

    @RequestMapping(value = "/closedBids", method = RequestMethod.GET)
    ResponseClosedBidList getClosedBidsList();

    @RequestMapping(value = "/bid/{itemId}", method = RequestMethod.GET)
    BidRecord getBidRecordByItem(@PathVariable String itemId);

    @RequestMapping(value = "/buyNow/{itemId}", method = RequestMethod.GET)
    SimpleResponse closeBidFromBuyNow(@PathVariable String itemId);

    @RequestMapping(value = "/buyNowUpdate/{itemId}", method = RequestMethod.POST)
    Boolean updateBuyNowOption(@PathVariable String itemId, @RequestBody UpdateBuyNow updateBuyNow);


    @RequestMapping(value = "/countdown/create", method = RequestMethod.POST)
    SimpleResponse createCountDown(@RequestBody CountDownRecord countDownRecord);

    @RequestMapping(value = "/countdown/{userId}", method = RequestMethod.GET)
    AuctionWindowResponse getCountDown(@PathVariable String userId);


}
