package com.rrmchathura.cofee_order_app.Model;

public class OrdersModel {

    String username,email,address,finalPrice,mobile,orderDate;
    boolean isExpandable;

    public OrdersModel() {
    }

    public OrdersModel(String username, String email, String address, String finalPrice, String mobile, String orderDate, boolean isExpandable) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.finalPrice = finalPrice;
        this.mobile = mobile;
        this.orderDate = orderDate;
        this.isExpandable = isExpandable;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }
}
