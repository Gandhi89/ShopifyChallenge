package com.example.android.shopify.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class InternetConnectivityBR extends BroadcastReceiver {
    private OnSampleReadyListener mListener;

    public InternetConnectivityBR(){}

    public InternetConnectivityBR(OnSampleReadyListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                if(mListener != null){
                    mListener.onDataReady(true);
                    Toast.makeText(context, "This is from BR: Internet is on!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public interface OnSampleReadyListener {
        void onDataReady(Boolean b);
    }
}
