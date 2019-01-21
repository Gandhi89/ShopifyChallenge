package com.example.android.shopify.Product;

public class Products {

    public String collection_name;
    public String product_title;
    public String src;
    public String totalInventory;

    public Products(String collection_name, String product_title, String src, String totalInventory) {
        this.collection_name = collection_name;
        this.product_title = product_title;
        this.src = src;
        this.totalInventory = totalInventory;
    }
}
