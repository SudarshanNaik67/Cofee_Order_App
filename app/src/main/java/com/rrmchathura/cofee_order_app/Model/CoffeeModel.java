package com.rrmchathura.cofee_order_app.Model;

public class CoffeeModel {

    String coffeeId;
    String coffeeName;
    int quantity,price;

    public CoffeeModel() {
    }

    public CoffeeModel(String coffeeId, String coffeeName, int quantity, int price) {
        this.coffeeId = coffeeId;
        this.coffeeName = coffeeName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getCoffeeId() {
        return coffeeId;
    }

    public void setCoffeeId(String coffeeId) {
        this.coffeeId = coffeeId;
    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public void setCoffeeName(String coffeeName) {
        this.coffeeName = coffeeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
