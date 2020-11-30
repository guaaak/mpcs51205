package com.example.itemservice.api;

import com.example.itemservice.constants.SysConstants;
import com.example.itemservice.dao.domain.Category;
import com.example.itemservice.dao.domain.Item;
import com.example.itemservice.model.CountDownInfo;
import com.example.itemservice.model.RequestFlagItem;
import com.example.itemservice.model.RequestItemBody;
import com.example.itemservice.model.ResponseCatList;
import com.example.itemservice.model.ResponseFlaggedList;
import com.example.itemservice.model.ResponseItemBody;
import com.example.itemservice.model.ResponseSearchResults;
import com.example.itemservice.model.SimpleResponse;
import com.example.itemservice.model.UpdateItemBody;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author lukewwang
 * @time 2020/11/23 1:42 PM
 */
@RequestMapping(SysConstants.BASE_URL + "/item")
public interface ItemCRUDApi {

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    ResponseItemBody createItem(@RequestBody RequestItemBody requestItemBody);

    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    Item getItemByID(@PathVariable String itemId);

    @RequestMapping(value = "/keyword/{keyword}", method = RequestMethod.GET)
    ResponseSearchResults getItemListByKeyword(@PathVariable String keyword);

    @RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
    ResponseSearchResults getItemListByCategory(@PathVariable String categoryId);

    @RequestMapping(value = "/flagged", method = RequestMethod.GET)
    ResponseFlaggedList getItemListByFlag();

    @RequestMapping(value = "/flagging", method = RequestMethod.POST)
    SimpleResponse flagItem(@RequestBody RequestFlagItem requestFlagItem);

    @RequestMapping(value = "/delete/{itemId}", method = RequestMethod.POST)
    SimpleResponse deleteItem(@PathVariable String itemId);

    @RequestMapping(value = "/update/{itemId}", method = RequestMethod.POST)
    SimpleResponse updateItem(@PathVariable String itemId,
            @RequestBody UpdateItemBody updateItemBody);

    @RequestMapping(value = "/delete/category/{categoryId}", method = RequestMethod.POST)
    SimpleResponse deleteCategory(@PathVariable String categoryId);

    @RequestMapping(value = "/create/category", method = RequestMethod.POST)
    SimpleResponse createCategory(@RequestBody Category category);

    @RequestMapping(value = "/update/category/{categoryId}", method = RequestMethod.POST)
    SimpleResponse updateCategory(@PathVariable String categoryId, @RequestBody Category category);

    @RequestMapping(value = "/category/all", method = RequestMethod.GET)
    ResponseCatList getAllCategory();

    @RequestMapping(value = "/category/get/{categoryId}", method = RequestMethod.GET)
    Category getCategoryByCatId(@PathVariable String categoryId);


    //Deprecated
    @RequestMapping(value = "/countdown/{keyword}", method = RequestMethod.GET)
    List<CountDownInfo> getCountdownInfo(@PathVariable String keyword);


}
