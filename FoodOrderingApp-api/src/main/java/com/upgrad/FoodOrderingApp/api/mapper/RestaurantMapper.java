package com.upgrad.FoodOrderingApp.api.mapper;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantMapper {

    public RestaurantDetailsResponse mapRestaurantDetailsResponse(
            final com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponse response) {
        final RestaurantDetailsResponse details = new RestaurantDetailsResponse();
        details.setId(UUID.fromString(response.getId()));
        details.setRestaurantName(response.getRestaurantName());
        details.setPhotoURL(response.getPhotoURL());
        details.setCustomerRating(response.getCustomerRating());
        details.setAveragePrice(response.getAveragePrice());
        details.setNumberCustomersRated(response.getNumberCustomersRated());

        final com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponseAddress responseAddress =
                response.getAddress();
        if (null != responseAddress) {
            final RestaurantDetailsResponseAddress address = new RestaurantDetailsResponseAddress();
            address.setId(responseAddress.getId());
            address.setFlatBuildingName(responseAddress.getFlatBuildingName());
            address.setCity(responseAddress.getFlatBuildingName());
            address.setLocality(responseAddress.getLocality());
            address.setCity(responseAddress.getCity());
            address.setPincode(responseAddress.getPincode());
            final com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponseAddressState responseState =
                    responseAddress.getState();
            if (null != responseState) {
                final RestaurantDetailsResponseAddressState state = new RestaurantDetailsResponseAddressState();
                state.setId(responseState.getId());
                state.setStateName(responseState.getStateName());
                address.setState(state);
            }
            details.setAddress(address);
        }
        return details;
    }
}
