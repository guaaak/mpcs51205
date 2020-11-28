package com.example.itemservice.model;

/**
 * @author lukewwang
 * @time 2020/11/28 9:28 PM
 */

public enum BidStatus {

    Ready(0), Open(1), Closed(2);

    private int value;

    BidStatus(int value) {
        this.value = value;
    }
}
