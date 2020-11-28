package com.example.biddingservice.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/29 12:17 AM
 */
@Data
@ToString
public class MeetCriteriaResponse {

    List<UserInfoBody> all_users = new ArrayList<>();
    String status = "";

}
