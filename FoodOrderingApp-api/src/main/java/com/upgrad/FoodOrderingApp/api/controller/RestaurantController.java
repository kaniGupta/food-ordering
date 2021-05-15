package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.mapper.RestaurantMapper;
import com.upgrad.FoodOrderingApp.api.model.CategoryList;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemList.ItemTypeEnum;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantUpdatedResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.common.ItemType;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);
    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;
    private final CustomerService customerService;
    private final CategoryService categoryService;

    @Autowired
    public RestaurantController(final RestaurantService restaurantService,
                                final RestaurantMapper restaurantMapper,
        final CustomerService customerService, final CategoryService categoryService) {
        this.restaurantService = restaurantService;
        this.restaurantMapper = restaurantMapper;
        this.customerService = customerService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<RestaurantListResponse> getRestaurants() {
        final List<RestaurantEntity> restaurantEntities = restaurantService.getRestaurants();
        final RestaurantListResponse response = getRestaurantListResponse(
            restaurantEntities);
        return ResponseEntity.ok(response);
    }
    
    private RestaurantListResponse getRestaurantListResponse(
        List<RestaurantEntity> restaurantEntities) {
        final RestaurantListResponse response = new RestaurantListResponse();
        final List<RestaurantList> responseList = new ArrayList<>();
        restaurantEntities.forEach(restaurantEntity -> {
            final RestaurantList restaurantList = new RestaurantList();
            restaurantList.setId(UUID.fromString(restaurantEntity.getUuid()));
            restaurantList.setRestaurantName(restaurantEntity.getRestaurantName());
            restaurantList.setPhotoURL(restaurantEntity.getPhotoUrl());
            restaurantList.setCustomerRating(new BigDecimal(restaurantEntity.getCustomerRating()));
            restaurantList.setAveragePrice(restaurantEntity.getAveragePriceForTwo());
            restaurantList.setNumberCustomersRated(restaurantEntity.getNumberOfCustomersRated());
            final RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress();
            final AddressEntity addressEntity = restaurantService.getRestaurantAddress(
                restaurantEntity.getId());
            if (null != addressEntity) {
                responseAddress.setId(UUID.fromString(addressEntity.getUuid()));
                responseAddress.setFlatBuildingName(addressEntity.getFlatBuilNumber());
                responseAddress.setLocality(addressEntity.getLocality());
                responseAddress.setCity(addressEntity.getCity());
                responseAddress.setPincode(addressEntity.getPincode());
                final RestaurantDetailsResponseAddressState state = new RestaurantDetailsResponseAddressState();
                if (null != addressEntity.getState()) {
                    state.setId(UUID.fromString(addressEntity.getState().getUuid()));
                    state.setStateName(addressEntity.getState().getStateName());
                }
                responseAddress.setState(state);
            }
            restaurantList.setAddress(responseAddress);
            restaurantList.setCategories(restaurantService.getRestaurantCategories(restaurantEntity));
            responseList.add(restaurantList);
        });
        
        response.restaurants(responseList);
        return response;
    }

    @GetMapping("/name/{restaurant_name}")
    public ResponseEntity<RestaurantListResponse> getRestaurantsByName(
            @PathVariable("restaurant_name") final String restaurantName) {
        final List<RestaurantEntity> restaurantEntities = restaurantService.restaurantsByName(restaurantName);
        final RestaurantListResponse response = getRestaurantListResponse(
            restaurantEntities);
        return ResponseEntity.ok(response);
    }
   
    @GetMapping("/{restaurant_id}")
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantsByRestaurantId(
        @PathVariable("restaurant_id") final String restaurantId)
        throws RestaurantNotFoundException {
        if(restaurantId == null || StringUtils.isEmpty(restaurantId)) {
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);
        if(restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }
        final RestaurantDetailsResponse response = restaurantDetails(restaurantEntity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category_id}")
    public String getRestaurantsByCategory(@PathVariable("category_id") final String categoryId)
        throws CategoryNotFoundException {
        if(categoryId == null || StringUtils.isEmpty(categoryId)) {
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }
        CategoryEntity category = categoryService.getCategoryById(categoryId);
        if(category == null) {
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }
        return "Welcome";
    }

    

    @PutMapping("/{restaurant_id}")
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantsDetails(
        @RequestHeader final String authorization,
        @PathVariable("restaurant_id") final String restaurantId,
        @RequestParam("customerRating") final Double customerRating)
        throws AuthorizationFailedException, RestaurantNotFoundException, InvalidRatingException {
        String accessToken=authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
    
        if(restaurantId == null || StringUtils.isEmpty(restaurantId)) {
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);
        if(restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }
        if(customerRating<1.0 || customerRating>5.0) {
            throw new InvalidRatingException("IRE-001","Restaurant should be in the range of 1 to 5");
        }
        RestaurantEntity updatedRestaurantEntity = restaurantService.updateRestaurantRating(restaurantEntity,customerRating);
        RestaurantUpdatedResponse response = new RestaurantUpdatedResponse().id(UUID.fromString(updatedRestaurantEntity.getUuid())).status("RESTAURANT RATING UPDATED SUCCESSFULLY");
        return new ResponseEntity<RestaurantUpdatedResponse>(response, HttpStatus.OK);
    }
    
    
    private RestaurantDetailsResponse restaurantDetails(RestaurantEntity restaurantEntity){
        RestaurantDetailsResponse response = new RestaurantDetailsResponse();
        response.setId(UUID.fromString(restaurantEntity.getUuid()));
        response.setRestaurantName(restaurantEntity.getRestaurantName());
        response.setPhotoURL(restaurantEntity.getPhotoUrl());
        response.setCustomerRating(new BigDecimal(restaurantEntity.getCustomerRating()));
        response.setAveragePrice(restaurantEntity.getAveragePriceForTwo());
        response.setNumberCustomersRated(restaurantEntity.getNumberOfCustomersRated());
        final RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress();
        final AddressEntity addressEntity = restaurantService.getRestaurantAddress(
            restaurantEntity.getId());
        if (null != addressEntity) {
            responseAddress.setId(UUID.fromString(addressEntity.getUuid()));
            responseAddress.setFlatBuildingName(addressEntity.getFlatBuilNumber());
            responseAddress.setLocality(addressEntity.getLocality());
            responseAddress.setCity(addressEntity.getCity());
            responseAddress.setPincode(addressEntity.getPincode());
            final RestaurantDetailsResponseAddressState state = new RestaurantDetailsResponseAddressState();
            if (null != addressEntity.getState()) {
                state.setId(UUID.fromString(addressEntity.getState().getUuid()));
                state.setStateName(addressEntity.getState().getStateName());
            }
            responseAddress.setState(state);
        }
        response.setAddress(responseAddress);
        List<CategoryList> list = convertToCategoryList(restaurantEntity.getCategories());
        response.setCategories(list);
        return response;
    }
    
    
    private List<CategoryList> convertToCategoryList(List<CategoryEntity> categories) {
        List<CategoryList> list = new ArrayList<>();
        for(CategoryEntity categoryEntity: categories) {
            CategoryList listEntry = new CategoryList();
            listEntry.id(UUID.fromString(categoryEntity.getUuid()));
            listEntry.itemList(convertToItemList(categoryEntity.getItems()));
            list.add(listEntry);
        }
        return list;
    }
    
    private List<ItemList> convertToItemList(List<ItemEntity> items){
        List<ItemList> itemList = new ArrayList<>();
        for(ItemEntity item:items){
            ItemList listEntry = new ItemList();
            listEntry.id(UUID.fromString(item.getUuid()));
            listEntry.itemName(item.getItemName());
            if(StringUtils.equalsIgnoreCase("VEG",item.getType())) {
                listEntry.itemType(ItemTypeEnum.VEG);
            }
            else {
                listEntry.itemType(ItemTypeEnum.NON_VEG);
            }
            listEntry.price(item.getPrice());
            itemList.add(listEntry);
        }
        return itemList;
    }
    
    
    
}
