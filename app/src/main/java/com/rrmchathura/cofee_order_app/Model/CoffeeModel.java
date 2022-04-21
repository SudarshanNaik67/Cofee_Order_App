package com.rrmchathura.cofee_order_app.Model;

public class CoffeeModel {

    String coffeeId,coffee_image,coffee_name,price,quantity,timestamp;
    String isCustomizeCusAvailable;


    public CoffeeModel() {
    }

    public CoffeeModel(String coffeeId, String coffee_image, String coffee_name, String price, String quantity, String timestamp, String isCustomizeCusAvailable) {
        this.coffeeId = coffeeId;
        this.coffee_image = coffee_image;
        this.coffee_name = coffee_name;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.isCustomizeCusAvailable = isCustomizeCusAvailable;
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

    public String getIsCustomizeCusAvailable() {
        return isCustomizeCusAvailable;
    }

    public void setIsCustomizeCusAvailable(String isCustomizeCusAvailable) {
        this.isCustomizeCusAvailable = isCustomizeCusAvailable;
    }
}
