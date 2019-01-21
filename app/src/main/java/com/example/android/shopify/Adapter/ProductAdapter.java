package com.example.android.shopify.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.shopify.Product.Products;
import com.example.android.shopify.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    ArrayList<Products> products;

    public ProductAdapter(Context context, ArrayList<Products> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView collectionImage;
        TextView productName;
        TextView totalInventory;
        TextView collectionName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            collectionImage = itemView.findViewById(R.id.cardView_image);
            collectionName = itemView.findViewById(R.id.cardView_collectionName);
            productName = itemView.findViewById(R.id.cardView_productName);
            totalInventory = itemView.findViewById(R.id.cardView_totalInventory);

        }

        public void bind(int i) {
            collectionName.setText(products.get(i).collection_name);
            productName.setText(products.get(i).product_title);
            totalInventory.setText("Total Inventory: "+products.get(i).totalInventory);
            String src = products.get(i).src;
            if (src != null && !src.isEmpty()) {
                Picasso.get().load(src).into(collectionImage);
            }
        }
    }

}
