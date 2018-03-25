package com.restapi.model;

public class PizzaOrder {
    private int pizzaId;
    private int orderId;
    private int quantity;
    private double cost;

    public PizzaOrder(int pizzaId, int orderId, int quantity, double cost) {
        this.pizzaId = pizzaId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.cost = cost;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "PizzaOrder{" +
                "pizzaId=" + pizzaId +
                ", orderId=" + orderId +
                ", quantity=" + quantity +
                ", cost=" + cost +
                '}';
    }
}
