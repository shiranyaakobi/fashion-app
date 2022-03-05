package com.idc.fashion.Model;

import java.io.Serializable;

public class Item implements Serializable {

    public enum Condition {
        EXCELLENT,
        GOOD,
        BAD
    }

    public enum Size {
        XXL,
        XL,
        L,
        M,
        S,
        XS
    }

    private int defaultImage;
    private String encodedBitmap;
    private String name;
    private String brand;
    private String description;
    private Condition condition;
    private Size size;
    private int price;

    public Item() {
    }

    public Item(int defaultImage, String name, String brand, String description, Condition condition, Size size, int price) {
        this.defaultImage = defaultImage;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.condition = condition;
        this.size = size;
        this.price = price;
    }

    public int getAvatar() {
        return defaultImage;
    }

    public void setAvatar(int avatar) {
        this.defaultImage = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(int defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getEncodedBitmap() {
        return encodedBitmap;
    }

    public void setEncodedBitmap(String encodedBitmap) {
        this.encodedBitmap = encodedBitmap;
    }
}
