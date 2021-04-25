package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemDao itemDao;

    @Autowired
    public ItemService(final ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public Item getItemById(final Integer id) {
        return itemDao.getItemById(id);
    }
}
