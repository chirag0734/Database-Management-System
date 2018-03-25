package com.restapi.model;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class Order {
    private int orderId;
    private Timestamp orderTime;
    private String orderTimeString;
    private Timestamp deliveryTime;
    private String deliveryTimeString;
    private String deliveryAddress;
    private String userEmail;
    private float discount;
    private List<Pizza> pizzaList;
    private User user;

    public Order() {
    }

    public Order(int orderId, Timestamp orderTime, Timestamp deliveryTime, String deliveryAddress, String userEmail, float discount, List<Pizza> pizzaList, User user) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.deliveryTime = deliveryTime;
        this.deliveryAddress = deliveryAddress;
        this.userEmail = userEmail;
        this.discount = discount;
        this.pizzaList = pizzaList;
        this.user = user;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderTimeString() {
        return orderTimeString;
    }

    public void setOrderTimeString(Timestamp orderTime) {

        Date date = new Date();
        date.setTime(orderTime.getTime());
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        this.orderTimeString = formattedDate;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public String getDeliveryTimeString() {
        return deliveryTimeString;
    }

    public void setDeliveryTimeString(Timestamp deliveryTime) {

        Date date = new Date();
        date.setTime(deliveryTime.getTime());
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        this.deliveryTimeString = formattedDate;
    }

    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Timestamp deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserId(String userEmail) {
        this.userEmail = userEmail;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public List<Pizza> getPizzaList() {
        return pizzaList;
    }

    public void setPizzaList(List<Pizza> pizzaList) {
        this.pizzaList = pizzaList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderTime=" + orderTime +
                ", deliveryTime=" + deliveryTime +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", userId=" + userEmail +
                ", discount=" + discount +
                ", pizzaList=" + pizzaList +
                ", user=" + user +
                '}';
    }
}
