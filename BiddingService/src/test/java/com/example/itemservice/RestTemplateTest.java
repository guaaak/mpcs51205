package com.example.itemservice;

import com.example.biddingservice.dao.domain.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author lukewwang
 * @time 2020/11/28 7:47 PM
 */
public class RestTemplateTest {

    public static void main(String[] args) throws JsonProcessingException {
        RestTemplate restT = new RestTemplate();
        // 通过 Jackson JSON processing library 直接将返回值绑定到对象
        Item item = restT
                .getForObject("http://localhost:8080/auction/item/0", Item.class);

        System.out.println(item);

        restT = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> params = new HashMap<>();
        params.put("itemID", "0");
        params.put("userID", "qwq");
        String url = "http://localhost:8080/auction/item/flagging";

        String value = mapper.writeValueAsString(params);
        HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
        ResponseEntity<String> response = restT.postForEntity(url, requestEntity, String.class);
        System.out.println("post json : " + response);

    }


}
