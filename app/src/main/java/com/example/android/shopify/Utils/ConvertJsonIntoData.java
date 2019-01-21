package com.example.android.shopify.Utils;

import android.util.Log;

import com.example.android.shopify.Collection.Collections;
import com.example.android.shopify.Product.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConvertJsonIntoData {

    private static final String COLLECTIONS = "custom_collections";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String SRC = "src";
    private static final String IMAGE = "image";
    private static final String PRODUCT_ID = "product_id";
    private static final String COLLECTS = "collects";
    private static final String PRODUCTS = "products";
    private static final String PRODUCT_INVENTORY_QUANTITY = "inventory_quantity";
    private static final String VARIANTS = "variants";
    private static final String TAG = "ConvertJsonIntoData";


    public static ArrayList<Collections> convertIntoData(String JSONdata) {
        ArrayList<Collections> data = new ArrayList<>();
        String jsonData = JSONdata;
        if (jsonData != null){
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray collections = jsonObject.getJSONArray(COLLECTIONS);

                for (int i = 0; i < collections.length(); i++) {
                    JSONObject jOBJ = collections.getJSONObject(i);
                    String collection_id = jOBJ.getString(ID);
                    String collection_title = jOBJ.getString(TITLE);
                    JSONObject images = jOBJ.getJSONObject(IMAGE);
                    String src = images.getString(SRC);

                    Collections collection = new Collections(collection_id, collection_title, src);
                    data.add(collection);
                }

                return data;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    public static ArrayList<String> getProductIdFromJSON(String JSONdata) {
        ArrayList<String> data = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(JSONdata);
            JSONArray collects = jsonObject.getJSONArray(COLLECTS);
            for (int i = 0; i < collects.length(); i++) {
                JSONObject jsonOBJ = collects.getJSONObject(i);
                String productID = jsonOBJ.getString(PRODUCT_ID);
                data.add(productID);
            }
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Products> convertIntoProductDetails(String JSONdata, String collection_name, String image_src) {
        ArrayList<Products> data = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(JSONdata);
            JSONArray products = jsonObject.getJSONArray(PRODUCTS);
            for (int i = 0; i < products.length(); i++) {
                JSONObject product_object = products.getJSONObject(i);
                String product_title = product_object.getString(TITLE);
                int total_inventory = 0;
                JSONArray variants = product_object.getJSONArray(VARIANTS);
                for (int y = 0; y < variants.length(); y++) {
                    JSONObject variantObject = variants.getJSONObject(y);
                    total_inventory += Integer.parseInt(variantObject.getString(PRODUCT_INVENTORY_QUANTITY));
                }
                Products product = new Products(collection_name, product_title, image_src, String.valueOf(total_inventory));
                data.add(product);
                Log.d(TAG, "convertIntoProductDetails: " + data.toString());
            }
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
