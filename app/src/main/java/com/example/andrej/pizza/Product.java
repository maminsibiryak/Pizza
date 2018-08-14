package com.example.andrej.pizza;

import java.util.HashMap;

public class Product {
    private String productId;
    private String productName;
    private String img_url;
    private String desc;
    private HashMap<String, String> size;

    public Product(String productId, String productName, String img_url, String desc, HashMap<String, String> size) {
        this.productName = productName;
        this.productId = productId;
        this.img_url = img_url;
        this.desc = desc;
        this.size = size;
    }

    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", productId='" + productId + '\'' +
                ", img_url='" + img_url + '\'' +
                ", desc='" + desc + '\'' +
                ", size=" + size +
                '}';
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public HashMap<String, String> getSize() {
        return size;
    }

    public void setSize(HashMap<String, String> size) {
        this.size = size;
    }
}
