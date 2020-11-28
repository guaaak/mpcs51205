package com.example.biddingservice.biz.impl;

import com.example.biddingservice.biz.OfferProcessService;
import com.example.biddingservice.dao.domain.OfferRecord;
import com.example.biddingservice.dao.repository.OfferRecordRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lukewwang
 * @time 2020/11/28 4:41 PM
 */

@Service
public class OfferProcessServiceImpl implements OfferProcessService {

    @Autowired
    private OfferRecordRepository offerRecordRepository;

    @Override
    public List<OfferRecord> getPrevBidsByUser(String userId) {

        return offerRecordRepository.getPrevOffersByUserId(userId);
    }

    @Override
    public void updateBidStatusByOthers(String itemId, String bidStatus, Double currHighestPrice) {
        offerRecordRepository.updateBidStatusByOthers(itemId, bidStatus, currHighestPrice);
    }

    @Override
    public void updateBidStatusByBidder(String itemId, String bidderId, String bidStatus,
            Double bidderOfferPrice) {
        offerRecordRepository
                .updateBidStatusByBidder(itemId, bidderId, bidStatus, bidderOfferPrice);
    }


}
