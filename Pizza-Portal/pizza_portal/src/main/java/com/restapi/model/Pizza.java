package com.restapi.model;

import java.util.List;

public class Pizza {
    private int pizzaId;
    private String pizzaName;
    private Sauce sauce;
    private Size size;
    private String isDefault;
    private List<Topping> toppingList;
    private int quantity;
    private int cost;

    public Pizza() {
    }

    public Pizza(int pizzaId, String pizzaName, Sauce sauce, Size size, String isDefault, List<Topping> toppingList, int quantity, int cost) {
        this.pizzaId = pizzaId;
        this.pizzaName = pizzaName;
        this.sauce = sauce;
        this.size = size;
        this.isDefault = isDefault;
        this.toppingList = toppingList;
        this.quantity = quantity;
        this.cost = cost;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public Sauce getSauce() {
        return sauce;
    }

    public void setSauce(Sauce sauce) {
        this.sauce = sauce;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public List<Topping> getToppingList() {
        return toppingList;
    }

    public void setToppingList(List<Topping> toppingList) {
        this.toppingList = toppingList;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "pizzaId=" + pizzaId +
                ", pizzaName='" + pizzaName + '\'' +
                ", sauce=" + sauce +
                ", size=" + size +
                ", isDefault='" + isDefault + '\'' +
                ", toppingList=" + toppingList +
                ", quantity=" + quantity +
                ", cost=" + cost +
                '}';
    }
}
