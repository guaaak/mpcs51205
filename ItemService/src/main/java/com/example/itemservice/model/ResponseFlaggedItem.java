package com.example.itemservice.model;

import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/25 3:27 PM
 */
@Data
@ToString
public class ResponseFlaggedItem {

    String itemName;
    String itemId;
    List<String> flaggedByUserIdList;
}
