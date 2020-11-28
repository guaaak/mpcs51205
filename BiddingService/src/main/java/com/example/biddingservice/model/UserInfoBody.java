package com.example.biddingservice.model;

import lombok.Data;

/**
 * @author lukewwang
 * @time 2020/11/29 12:17 AM
 */
@Data
public class UserInfoBody {

    Double criteria = 0.0D;

    String email = "";

    String uid = "";

    String username = "";

}
