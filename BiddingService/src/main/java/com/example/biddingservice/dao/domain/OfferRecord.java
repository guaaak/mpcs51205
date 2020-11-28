package com.example.biddingservice.dao.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author lukewwang
 * @time 2020/11/28 2:54 PM
 */

@Data
@ToString
@Document(value = "offer-record-coll")
public class OfferRecord {

    String bidderId;

    String itemId;

    String bidStatus;

    Double bidderOfferPrice;

    Double currHighestPrice;

}
