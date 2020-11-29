package com.example.biddingservice.dao.repository;

import com.example.biddingservice.dao.domain.CountDownRecord;
import java.util.List;

/**
 * @author lukewwang
 * @time 2020/11/29 3:43 PM
 */
public interface CountDownRecordRepository {

    void saveRecord(CountDownRecord countDownRecord);

    List<CountDownRecord> getRecordByUserId(String userId);

}
