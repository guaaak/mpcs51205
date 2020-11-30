package com.example.itemservice.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * @author lukewwang
 * @time 2020/11/30 12:52 PM
 */
@Data
@ToString
public class ResponseSearchResults {

    List<ResponseSearchOnItem> searchResults = new ArrayList<>();

}
