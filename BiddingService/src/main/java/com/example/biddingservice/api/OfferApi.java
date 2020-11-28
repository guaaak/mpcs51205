package com.example.biddingservice.api;

import com.example.biddingservice.constants.SysConstants;
import com.example.biddingservice.dao.domain.OfferRecord;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author lukewwang
 * @time 2020/11/28 4:35 PM
 */
@RequestMapping(SysConstants.BASE_URL + "/offer")
public interface OfferApi {

    @RequestMapping(value = "/prevBids/{userId}", method = RequestMethod.GET)
    List<OfferRecord> getPrevOffers(@PathVariable String userId);

}
