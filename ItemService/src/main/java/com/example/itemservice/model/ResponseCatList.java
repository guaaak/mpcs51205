package com.example.itemservice.model;

import com.example.itemservice.dao.domain.Category;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/12/1 12:16 AM
 */
@Data
@ToString
public class ResponseCatList {

    List<Category> categoryList = new ArrayList<>();

}
