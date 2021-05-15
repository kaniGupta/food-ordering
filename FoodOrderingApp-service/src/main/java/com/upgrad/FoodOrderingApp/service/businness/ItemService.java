package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemDao itemDao;

    @Autowired
    public ItemService(final ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public ItemEntity getItemById(final Integer id) {
        return itemDao.getItemById(id);
    }
    
    public ItemEntity getItemByUuid(final String uuid) {
        return itemDao.getItemByUuid(uuid);
    }
    
    public ItemEntity restaurantByUUID(final String uuid) {
        return itemDao.getItemByUuid(uuid);
    }
    
    public List<ItemEntity> getItemsByCategoryAndRestaurant(final String restaurant,final String categoryUuid) {
        
        return Collections.emptyList();
    }
    
    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {
        
        return Collections.emptyList();
    }
    
    
}
