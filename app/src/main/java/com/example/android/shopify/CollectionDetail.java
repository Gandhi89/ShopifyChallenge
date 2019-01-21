package com.example.android.shopify;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.shopify.Adapter.ProductAdapter;
import com.example.android.shopify.BroadcastReceiver.InternetConnectivityBR;
import com.example.android.shopify.Product.Products;
import com.example.android.shopify.Utils.ConvertJsonIntoData;
import com.example.android.shopify.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class CollectionDetail extends AppCompatActivity implements InternetConnectivityBR.OnSampleReadyListener {

    private static final String TAG = "CollectionDetail";
    private static final String INTENT_DATA = "collection_id";
    String intentData = null;
    String image_src = null;
    String collection_name = null;
    private static final String IMAGE_SRC = "image_src";
    private static final String COLLECTION_NAME = "collection_name";
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;
    InternetConnectivityBR myReceiver;
    private Boolean dataDisplaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_detail2);
        recyclerView = findViewById(R.id.CollectionDetail_recyclerView);
        constraintLayout = findViewById(R.id.CollectionDetail_constrainLayout);
        progressBar = findViewById(R.id.CollectionDetail_progressBar);
        constraintLayout.setBackgroundColor(Color.parseColor("#ffffff"));

        /**
         * Check Internet Connectivity
         */
        ConnectivityManager cm = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            loadData();
            dataDisplaying = true;
        } else {
            Toast.makeText(this, "No Internet Connection !!", Toast.LENGTH_SHORT).show();
            dataDisplaying = false;
        }

        myReceiver = new InternetConnectivityBR(this);
        registerReceiver(myReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }// end of onCreate()

    /**
     * method to load data on screen
     */
    private void loadData() {
        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_DATA) && intent.hasExtra(IMAGE_SRC) && intent.hasExtra(COLLECTION_NAME)) {
            intentData = intent.getStringExtra(INTENT_DATA);
            image_src = intent.getStringExtra(IMAGE_SRC);
            collection_name = intent.getStringExtra(COLLECTION_NAME);
        }

        if (intentData != null) {
            new getProductIDs().execute(intentData);
        }
    }

    /**
     * method to display data when connected to internet
     */
    @Override
    public void onDataReady(Boolean b) {
        if (!dataDisplaying) {
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            loadData();
            dataDisplaying = true;
        }
    }

    /**
     * method to get productIDs
     */
    private class getProductIDs extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String collectionID = strings[0];

            URL url = NetworkUtils.buildURL_2(collectionID);
            try {
                String data = NetworkUtils.getResponseFromHttpUrl(url);
                return data;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String productIDs = "";
            ArrayList<String> data = ConvertJsonIntoData.getProductIdFromJSON(s);
            for (int i = 0; i < data.size(); i++) {
                if (i + 1 == data.size()) {
                    productIDs += data.get(i);
                } else {
                    productIDs += data.get(i) + ",";
                }
            }
            new getDataFromWeb().execute(productIDs);
        }
    }

    /**
     * method to get products
     */
    private class getDataFromWeb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String collectionID = strings[0];
            URL url = NetworkUtils.buildURL_3(collectionID);
            try {
                String data = NetworkUtils.getResponseFromHttpUrl(url);
                return data;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            constraintLayout.setBackgroundColor(Color.parseColor("#004c40"));
            if (s != null && s != "") {
                ArrayList<Products> product = ConvertJsonIntoData.convertIntoProductDetails(s, collection_name, image_src);
                if (product != null) {
                    productAdapter = new ProductAdapter(CollectionDetail.this, product);
                    recyclerView.setAdapter(productAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CollectionDetail.this));

                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
        super.onDestroy();
    }
}


