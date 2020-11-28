package com.example.biddingservice.model;

import com.example.biddingservice.dao.domain.Item;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/23 2:33 PM
 */
@Data
@ToString
public class ResponseItemBody {

    boolean success = true;

    Item item = null;

}
