package com.example.android.shopify.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.shopify.Collection.Collections;
import com.example.android.shopify.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CollectionAdapter extends BaseAdapter {

    Context context;
    ArrayList<Collections> collection;

    public CollectionAdapter(Context context, ArrayList<Collections> collection) {
        this.collection = collection;
        this.context = context;
    }

    @Override
    public int getCount() {
        return collection.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.collection_item, null);
        }
        ImageView imageView;
        TextView textView;
        TextView id;

        imageView = convertView.findViewById(R.id.collection_item_imageView);
        textView = convertView.findViewById(R.id.collection_item_textView);
        id = convertView.findViewById(R.id.collection_item_id);

        String src = collection.get(i).src;
        if (src != null && !src.isEmpty()) {
            Picasso.get().load(src).into(imageView);
        }
        imageView.setTag(src);
        textView.setText(collection.get(i).title);
        id.setText(collection.get(i).id);

        return convertView;
    }

}
