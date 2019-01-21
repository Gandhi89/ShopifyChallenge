package com.example.android.shopify.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    // URL 1
    private static final String BASE_URL_1 = "https://shopicruit.myshopify.com/admin/custom_collections.json";

    private static final String PARAM_PAGE_URL_1 = "page";
    private static final String PARAM_ACCESS_TOKEN_URL_1 = "access_token";
    private static final String VALUE_PAGE_URL_1 = "1";
    private static final String VALUE_ACCESS_TOKEN_URL_1 = "c32313df0d0ef512ca64d5b336a0d7c6";

    // URL 2
    private static final String BASE_URL_2 = "https://shopicruit.myshopify.com/admin/collects.json";

    private static final String COLLECTION_ID_URL_2 = "collection_id";
    private static String VALUE_COLLECTION_ID_URL_2 = "";
    private static final String PARAM_PAGE_URL_2 = "page";
    private static final String PARAM_ACCESS_TOKEN_URL_2 = "access_token";
    private static final String VALUE_PAGE_URL_2 = "1";
    private static final String VALUE_ACCESS_TOKEN_URL_2 = "c32313df0d0ef512ca64d5b336a0d7c6";


    // URL 3
    private static final String BASE_URL_3 = "https://shopicruit.myshopify.com/admin/products.json";

    private static final String PARAM_IDS_URL_3 = "ids";
    private static final String PARAM_PAGE_URL_3 = "page";
    private static final String PARAM_ACCESS_TOKEN_URL_3 = "access_token";
    private static final String VALUE_PAGE_URL_3 = "1";
    private static String VALUE_IDS_URL_3 = "";
    private static final String VALUE_ACCESS_TOKEN_URL_3 = "c32313df0d0ef512ca64d5b336a0d7c6";


    public static URL buildURL_1() {

        Uri uri = Uri.parse(BASE_URL_1).buildUpon()
                .appendQueryParameter(PARAM_PAGE_URL_1, VALUE_PAGE_URL_1)
                .appendQueryParameter(PARAM_ACCESS_TOKEN_URL_1, VALUE_ACCESS_TOKEN_URL_1)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildURL_2(String collection_id) {

        VALUE_COLLECTION_ID_URL_2 = collection_id;
        Uri uri = Uri.parse(BASE_URL_2).buildUpon()
                .appendQueryParameter(COLLECTION_ID_URL_2, VALUE_COLLECTION_ID_URL_2)
                .appendQueryParameter(PARAM_PAGE_URL_2, VALUE_PAGE_URL_2)
                .appendQueryParameter(PARAM_ACCESS_TOKEN_URL_2, VALUE_ACCESS_TOKEN_URL_2)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildURL_3(String ids) {

        VALUE_IDS_URL_3 = ids;
        Uri uri = Uri.parse(BASE_URL_3).buildUpon()
                .appendQueryParameter(PARAM_IDS_URL_3, VALUE_IDS_URL_3)
                .appendQueryParameter(PARAM_PAGE_URL_3, VALUE_PAGE_URL_3)
                .appendQueryParameter(PARAM_ACCESS_TOKEN_URL_3, VALUE_ACCESS_TOKEN_URL_3)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            Log.d("MainActivity/", scanner.toString());
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
