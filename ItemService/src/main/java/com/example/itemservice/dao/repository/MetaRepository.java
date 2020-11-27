package com.example.itemservice.dao.repository;

/**
 * @author lukewwang
 * @time 2020/11/23 2:40 PM
 */
public interface MetaRepository {

    long getStartSequence();

    void updateSequence(long newSequence);

}
