package com.example.itemservice.dao.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author lukewwang
 * @time 2020/11/23 2:39 PM
 */
@Data
@ToString
@Document(value = "meta-coll")
public class ItemMetaData {

    private int tag = 0;
    private long idSequence = 0;

}
