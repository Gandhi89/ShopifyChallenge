package com.example.android.shopify;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopify.Adapter.CollectionAdapter;
import com.example.android.shopify.BroadcastReceiver.InternetConnectivityBR;
import com.example.android.shopify.Collection.Collections;
import com.example.android.shopify.Utils.ConvertJsonIntoData;
import com.example.android.shopify.Utils.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CustomCollection extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, AdapterView.OnItemClickListener, InternetConnectivityBR.OnSampleReadyListener {

    private static final String TAG = "CustomCollection";
    CollectionAdapter collectionAdapter;
    private static final String INTENT_DATA = "collection_id";
    private static final String IMAGE_SRC = "image_src";
    private static final String COLLECTION_NAME = "collection_name";
    ProgressBar progressBar;
    GridView gridView;
    private static final int COLLECTION_LOADER = 1122;
    private static final String SHOPIFY_COLLECTION_URL = "";
    InternetConnectivityBR myReceiver;
    private Boolean dataDisplaying;
    LoaderManager loaderManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_collection);
        gridView = findViewById(R.id.CustomCollection_gridView);
        progressBar = findViewById(R.id.mainActivity_progressBar);


    } // end of onCreate()

    /**
     * method to display data on screen
     */
    private void loadData() {

        // build url
        URL url = NetworkUtils.buildURL_1();
        Bundle bundle = new Bundle();

        // save data in bundle
        bundle.putString(SHOPIFY_COLLECTION_URL, url.toString());

        loaderManager = LoaderManager.getInstance(CustomCollection.this);
        Loader<String> loader = loaderManager.getLoader(COLLECTION_LOADER);

        // initialize or restart loader
        if (loader == null) {
            loaderManager.initLoader(COLLECTION_LOADER, bundle, CustomCollection.this);
        } else {
            loaderManager.restartLoader(COLLECTION_LOADER, bundle, CustomCollection.this);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (bundle == null) {
                    return;
                }
                forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {
                String query = bundle.getString(SHOPIFY_COLLECTION_URL);
                if (query == null || query.isEmpty()) {
                    return null;
                }
                try {
                    URL url = new URL(query);
                    if (checkConnectivity()) {
                        String data = NetworkUtils.getResponseFromHttpUrl(url);
                        return data;
                    } else {
                        return null;
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        // hide progressbar and show result
        progressBar.animate().alpha(0).setDuration(300);
        Log.d(TAG, "onLoadFinished: " + s);
        if (s != null || s != "") {
            ArrayList<Collections> collectionsArrayList = ConvertJsonIntoData.convertIntoData(s);
            if (collectionsArrayList != null) {
                Log.d(TAG, "onLoadFinished: ArraySize:- " + collectionsArrayList.size());
                collectionAdapter = new CollectionAdapter(CustomCollection.this, collectionsArrayList);
                gridView.setAdapter(collectionAdapter);
                gridView.setOnItemClickListener(this);
            }

        } else {
            Toast.makeText(CustomCollection.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    /**
     * method to handle Gridview click events
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ImageView imageView;
        TextView textView;
        TextView id;

        imageView = view.findViewById(R.id.collection_item_imageView);
        textView = view.findViewById(R.id.collection_item_textView);
        id = view.findViewById(R.id.collection_item_id);

        String collectionName = textView.getText().toString();
        String image_src = imageView.getTag().toString();
        String collectionID = id.getText().toString();

        Intent intent = new Intent(this, CollectionDetail.class);
        intent.putExtra(INTENT_DATA, collectionID);
        intent.putExtra(IMAGE_SRC, image_src);
        intent.putExtra(COLLECTION_NAME, collectionName);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * Register Receiver
         */
        myReceiver = new InternetConnectivityBR(this);
        registerReceiver(myReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        /**
         * Check Internet Connectivity
         */
        if (checkConnectivity()) {
            loadData();
            dataDisplaying = true;
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            dataDisplaying = false;
        }

    }


    @Override
    protected void onPause() {
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
        super.onPause();
    }

    /**
     * method to display data when connected to internet
     */
    @Override
    public void onDataReady(Boolean b) {
        if (b) {
            if (!dataDisplaying) {
                Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
                loadData();
                dataDisplaying = true;
            }
        }
    }

    /**
     * method to check Internet connectivity
     */
    private boolean checkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) CustomCollection.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
