package com.example.itemservice.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/23 1:51 PM
 */
@Data
@ToString
public class RequestItemBody {

    private int quantity = 0;
    private double ratings = 0;
    private String description = "";
    private String name = "";

    private String isFlagged = "false";

    private String categoryId = "";

}
