package com.example.biddingservice.constants;

/**
 * @author lukewwang
 * @time 2020/11/25 4:41 PM
 */
public enum BidStatus {

    Ready(0), Open(1), Closed(2);

    private int value;

    BidStatus(int value) {
        this.value = value;
    }

//    public int getValue(){return this.value;}
}
