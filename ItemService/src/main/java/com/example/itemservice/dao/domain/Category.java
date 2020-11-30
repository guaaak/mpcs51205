package com.example.itemservice.dao.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author lukewwang
 * @time 2020/11/30 11:30 PM
 */
@Data
@ToString
@Document(value = "cat-coll")
public class Category {

    String categoryId = "";
    String categoryName = "";

}
