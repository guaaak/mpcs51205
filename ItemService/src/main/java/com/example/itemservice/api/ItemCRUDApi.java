package com.example.itemservice.api;

import com.example.itemservice.constants.SysConstants;
import com.example.itemservice.dao.domain.Item;
import com.example.itemservice.model.RequestFlagItem;
import com.example.itemservice.model.RequestItemBody;
import com.example.itemservice.model.ResponseFlaggedList;
import com.example.itemservice.model.ResponseItemBody;
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
    List<Item> getItemListByKeyword(@PathVariable String keyword);

    @RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
    List<Item> getItemListByCategory(@PathVariable String categoryId);

    @RequestMapping(value = "/flagged", method = RequestMethod.GET)
    ResponseFlaggedList getItemListByFlag();

    @RequestMapping(value = "/flagging", method = RequestMethod.POST)
    Boolean flagItem(@RequestBody RequestFlagItem requestFlagItem);

    @RequestMapping(value = "/delete/{itemId}", method = RequestMethod.POST)
    Boolean deleteItem(@PathVariable String itemId);


}
