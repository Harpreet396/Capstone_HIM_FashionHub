package com.inderproduction.theinderapp.Modals;

import com.google.firebase.Timestamp;

import java.util.List;

public class FinalOrder {


    private String orderID;
    private String customerName,customerEmail,customerNumber,customerAddress;
    private List<OrderItem> cartItems;
    private double orderBaseTotal,orderTax,orderGrandTotal;
    private Timestamp orderTime;

    public FinalOrder() {
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public List<OrderItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<OrderItem> cartItems) {
        this.cartItems = cartItems;
    }

    public double getOrderBaseTotal() {
        return orderBaseTotal;
    }

    public void setOrderBaseTotal(double orderBaseTotal) {
        this.orderBaseTotal = orderBaseTotal;
    }

    public double getOrderTax() {
        return orderTax;
    }

    public void setOrderTax(double orderTax) {
        this.orderTax = orderTax;
    }

    public double getOrderGrandTotal() {
        return orderGrandTotal;
    }

    public void setOrderGrandTotal(double orderGrandTotal) {
        this.orderGrandTotal = orderGrandTotal;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }
}
