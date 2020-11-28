package com.example.biddingservice.model;

import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/25 3:29 PM
 */
@Data
@ToString
public class ResponseFlaggedList {

    List<ResponseFlaggedItem> flaggedItemList;

}
