package com.example.biddingservice.dao.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author lukewwang
 * @time 2020/11/29 3:42 PM
 */
@Data
@ToString
@Document(value = "count-down-coll")
public class CountDownRecord {

    String itemID = "";
    String userId = "";
    String itemName = "";
    String startTime = "";

}
