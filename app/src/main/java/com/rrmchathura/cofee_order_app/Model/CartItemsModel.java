package com.rrmchathura.cofee_order_app.Model;

public class CartItemsModel {

    String coffeeId,coffeeImage,coffeeName,finalPrice,isCustomizeAvailable,quantity,selectedAdditions,selectedSize,selectedSugar,timeStamp,uid;

    public CartItemsModel() {
    }

    public CartItemsModel(String coffeeId, String coffeeImage, String coffeeName, String finalPrice, String isCustomizeAvailable, String quantity, String selectedAdditions, String selectedSize, String selectedSugar, String timeStamp, String uid) {
        this.coffeeId = coffeeId;
        this.coffeeImage = coffeeImage;
        this.coffeeName = coffeeName;
        this.finalPrice = finalPrice;
        this.isCustomizeAvailable = isCustomizeAvailable;
        this.quantity = quantity;
        this.selectedAdditions = selectedAdditions;
        this.selectedSize = selectedSize;
        this.selectedSugar = selectedSugar;
        this.timeStamp = timeStamp;
        this.uid = uid;
    }

    public String getCoffeeId() {
        return coffeeId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setCoffeeId(String coffeeId) {
        this.coffeeId = coffeeId;
    }

    public String getCoffeeImage() {
        return coffeeImage;
    }

    public void setCoffeeImage(String coffeeImage) {
        this.coffeeImage = coffeeImage;
    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public void setCoffeeName(String coffeeName) {
        this.coffeeName = coffeeName;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getIsCustomizeAvailable() {
        return isCustomizeAvailable;
    }

    public void setIsCustomizeAvailable(String isCustomizeAvailable) {
        this.isCustomizeAvailable = isCustomizeAvailable;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSelectedAdditions() {
        return selectedAdditions;
    }

    public void setSelectedAdditions(String selectedAdditions) {
        this.selectedAdditions = selectedAdditions;
    }

    public String getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public String getSelectedSugar() {
        return selectedSugar;
    }

    public void setSelectedSugar(String selectedSugar) {
        this.selectedSugar = selectedSugar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
