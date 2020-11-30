package com.example.biddingservice.controller;

import com.example.biddingservice.api.OfferApi;
import com.example.biddingservice.biz.OfferProcessService;
import com.example.biddingservice.model.PrevBidsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lukewwang
 * @time 2020/11/28 4:40 PM
 */
@RestController
public class OfferApiController implements OfferApi {

    @Autowired
    private OfferProcessService offerProcessService;

    @Override
    public PrevBidsResponse getPrevOffers(String userId) {

        PrevBidsResponse response = new PrevBidsResponse();
        response.setPrevBidsList(offerProcessService.getPrevBidsByUser(userId));

        return response;
    }
}
