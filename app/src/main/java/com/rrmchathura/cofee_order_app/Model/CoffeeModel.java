package com.rrmchathura.cofee_order_app.Model;

public class CoffeeModel {

    String coffeeId,coffee_image,coffee_name,price,quantity,timestamp;
    boolean isAdditionCusAvailable,isSizeCusAvailable,isSugarCusAvailable;

    public CoffeeModel() {
    }

    public CoffeeModel(String coffeeId, String coffee_image, String coffee_name, String price, String quantity, String timestamp, boolean isAdditionCusAvailable, boolean isSizeCusAvailable, boolean isSugarCusAvailable) {
        this.coffeeId = coffeeId;
        this.coffee_image = coffee_image;
        this.coffee_name = coffee_name;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.isAdditionCusAvailable = isAdditionCusAvailable;
        this.isSizeCusAvailable = isSizeCusAvailable;
        this.isSugarCusAvailable = isSugarCusAvailable;
    }

    public String getCoffeeId() {
        return coffeeId;
    }

    public void setCoffeeId(String coffeeId) {
        this.coffeeId = coffeeId;
    }

    public String getCoffee_image() {
        return coffee_image;
    }

    public void setCoffee_image(String coffee_image) {
        this.coffee_image = coffee_image;
    }

    public String getCoffee_name() {
        return coffee_name;
    }

    public void setCoffee_name(String coffee_name) {
        this.coffee_name = coffee_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isAdditionCusAvailable() {
        return isAdditionCusAvailable;
    }

    public void setAdditionCusAvailable(boolean additionCusAvailable) {
        isAdditionCusAvailable = additionCusAvailable;
    }

    public boolean isSizeCusAvailable() {
        return isSizeCusAvailable;
    }

    public void setSizeCusAvailable(boolean sizeCusAvailable) {
        isSizeCusAvailable = sizeCusAvailable;
    }

    public boolean isSugarCusAvailable() {
        return isSugarCusAvailable;
    }

    public void setSugarCusAvailable(boolean sugarCusAvailable) {
        isSugarCusAvailable = sugarCusAvailable;
    }
}
