package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemQuantity;
import com.upgrad.FoodOrderingApp.api.model.ItemQuantityResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemQuantityResponseItem;
import com.upgrad.FoodOrderingApp.api.model.ItemQuantityResponseItem.TypeEnum;
import com.upgrad.FoodOrderingApp.api.model.OrderList;
import com.upgrad.FoodOrderingApp.api.model.OrderListAddress;
import com.upgrad.FoodOrderingApp.api.model.OrderListAddressState;
import com.upgrad.FoodOrderingApp.api.model.OrderListCoupon;
import com.upgrad.FoodOrderingApp.api.model.OrderListCustomer;
import com.upgrad.FoodOrderingApp.api.model.OrderListPayment;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderResponse;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.Coupon;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItem;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    
    private final AddressService addressService;
    
    private final CustomerService customerService;
    
    private final OrderService orderService;
    
    private final PaymentService paymentService;
    
    private final RestaurantService restaurantService;
    
    private final ItemService itemService;
    
    @Autowired
    public OrderController(AddressService addressService,
        CustomerService customerService,
        OrderService orderService, PaymentService paymentService,
        RestaurantService restaurantService, ItemService itemService) {
        this.addressService = addressService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.restaurantService = restaurantService;
        this.itemService = itemService;
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Order successfully created"),
    })
    @RequestMapping(method = RequestMethod.POST, path="/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader final String authorization,
        @RequestBody(required = false) final SaveOrderRequest saveOrderRequest)
        throws AuthorizationFailedException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, CouponNotFoundException, ItemNotFoundException {
        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        AddressEntity addressEntity = addressService.getAddressByUUID(
            saveOrderRequest.getAddressId().toString(), customerEntity);
        if (addressEntity == null) {
            throw new AddressNotFoundException("ATHR-004",
                "You are not authorized to view/update/delete any one else's address");
        }
        PaymentEntity paymentEntity = paymentService
                                          .getPayment(saveOrderRequest.getPaymentId().toString());
        if (paymentEntity == null) {
            throw new PaymentMethodNotFoundException("PNF-002",
                "No payment method found by this id");
        }
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(
            saveOrderRequest.getRestaurantId().toString());
        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        Coupon coupon = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());
        if(coupon == null) {
            throw new CouponNotFoundException("CPF-002","No coupon by this id");
        }
        OrderEntity newOrder = new OrderEntity();
        newOrder.setUuid(UUID.randomUUID().toString());
        newOrder.setAddress(addressEntity);
        newOrder.setBill(saveOrderRequest.getBill().doubleValue());
        newOrder.setCoupon(coupon);
        newOrder.setCustomer(customerEntity);
        newOrder.setRestaurant(restaurantEntity);
        newOrder.setPayment(paymentEntity);
        newOrder.setDiscount(saveOrderRequest.getDiscount().doubleValue());
        newOrder.setDate(ZonedDateTime.now());
        OrderEntity updatedOrder = orderService.saveOrder(newOrder);
        for(ItemQuantity itemQuantity:saveOrderRequest.getItemQuantities()) {
            ItemEntity item = itemService.getItemByUuid(itemQuantity.getItemId().toString());
            if(item == null) {
                throw new ItemNotFoundException("INF-003","No item by this id exist");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(updatedOrder);
            orderItem.setItem(item);
            orderItem.setPrice(itemQuantity.getPrice());
            orderItem.setQuantity(itemQuantity.getQuantity());
            orderService.saveOrderItem(orderItem);
        }
        
        SaveOrderResponse response = new SaveOrderResponse().id(newOrder.getUuid()).status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<SaveOrderResponse>(response, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.GET, path="/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<CouponDetailsResponse> getCouponDetails(@RequestHeader final String authorization, @PathVariable("coupon_name") final String couponName)
        throws AuthorizationFailedException, CouponNotFoundException {
        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        if(couponName == null || StringUtils.isEmpty(couponName)) {
            throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
        }
        Coupon coupon = orderService.getCouponByCouponName(couponName);
        if(coupon == null) {
            throw new CouponNotFoundException("CPF-001","No coupon by this name");
        }
        CouponDetailsResponse response = new CouponDetailsResponse().id(UUID.fromString(coupon.getUuid())).couponName(coupon.getCouponName()).percent(coupon.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(response,HttpStatus.OK);
    }
    
    
    @RequestMapping(method = RequestMethod.GET, path="/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<List<OrderList>> getOrderDetails(@RequestHeader final String authorization)
        throws AuthorizationFailedException, CouponNotFoundException {
        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        List<OrderEntity> orders = orderService.getOrdersByCustomers(customerEntity.getUuid());
        orders.sort(new Comparator<OrderEntity>() {
            @Override
            public int compare(OrderEntity o1, OrderEntity o2) {
                if(o2.getDate().isAfter(o1.getDate())) return 1;
                else if (o2.getDate().isAfter(o1.getDate())) return -1;
                else return 0;
            }
        });
       
       List<OrderList> orderList = new ArrayList<>();
       for(OrderEntity orderEntity:orders) {
           OrderList listEntry = new OrderList();
           listEntry.id(UUID.fromString(orderEntity.getUuid()));
           listEntry.bill(BigDecimal.valueOf(orderEntity.getBill()));
           listEntry.coupon(convert(orderEntity.getCoupon()));
           listEntry.discount(BigDecimal.valueOf(orderEntity.getDiscount()));
           listEntry.date(orderEntity.getDate().toString());
           listEntry.payment(convert(orderEntity.getPayment()));
           listEntry.customer(convert(orderEntity.getCustomer()));
           listEntry.address(convert(orderEntity.getAddress()));
           listEntry.itemQuantities(convertToItemQuantityResponse(orderEntity));
           orderList.add(listEntry);
       }
       return new ResponseEntity<List<OrderList>>(orderList,HttpStatus.OK);
    }
    
    private OrderListAddress convert(AddressEntity addressEntity) {
        OrderListAddress address = new OrderListAddress();
        address.id(UUID.fromString(addressEntity.getUuid()));
        address.city(addressEntity.getCity());
        address.flatBuildingName(addressEntity.getFlatBuilNumber());
        address.locality(addressEntity.getLocality());
        address.pincode(addressEntity.getPincode());
        StateEntity state = addressEntity.getState();
        OrderListAddressState state1 = new OrderListAddressState();
        state1.id(UUID.fromString(state.getUuid()));
        state1.stateName(state.getStateName());
        address.state(state1);
        return address;
    }
    
    private OrderListCustomer convert(CustomerEntity customerEntity){
        OrderListCustomer customer = new OrderListCustomer();
        customer.id(UUID.fromString(customerEntity.getUuid()));
        customer.firstName(customerEntity.getFirstName());
        customer.lastName(customerEntity.getLastName());
        customer.emailAddress(customerEntity.getEmail());
        customer.contactNumber(customerEntity.getContactNumber());
        return customer;
    }
    
    private OrderListCoupon convert(Coupon coupon) {
        OrderListCoupon orderListCoupon = new OrderListCoupon();
        orderListCoupon.id(UUID.fromString(coupon.getUuid()));
        orderListCoupon.couponName(coupon.getCouponName());
        orderListCoupon.percent(coupon.getPercent());
        return orderListCoupon;
    }
    
    private OrderListPayment convert(PaymentEntity paymentEntity) {
        OrderListPayment orderListPayment = new OrderListPayment();
        orderListPayment.id(UUID.fromString(paymentEntity.getUuid()));
        orderListPayment.paymentName(paymentEntity.getPaymentName());
        return orderListPayment;
    }
    
    private List<ItemQuantityResponse> convertToItemQuantityResponse(final OrderEntity orderEntity) {
        List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(orderEntity.getId());
        List<ItemQuantityResponse> list = new ArrayList<>();
        for(OrderItem orderItem:orderItems) {
            ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse();
            itemQuantityResponse.price(orderItem.getPrice());
            itemQuantityResponse.quantity(orderItem.getQuantity());
            ItemEntity itemEntity = itemService.getItemById(orderItem.getItem().getId());
            ItemQuantityResponseItem item = new ItemQuantityResponseItem();
            item.id(UUID.fromString(itemEntity.getUuid()));
            item.itemName(itemEntity.getItemName());
            item.itemPrice(itemEntity.getPrice());
            if(itemEntity.getType().equalsIgnoreCase("VEG"))
               item.type(TypeEnum.VEG);
            else
                item.type(TypeEnum.NON_VEG);
            itemQuantityResponse.item(item);
            list.add(itemQuantityResponse);
        }
        return list;
    }
    
    
}