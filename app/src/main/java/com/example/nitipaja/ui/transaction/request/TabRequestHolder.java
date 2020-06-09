package com.example.nitipaja.ui.transaction.request;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitipaja.R;
import com.example.nitipaja.ui.ItemClickListener;

public class TabRequestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImageView;
    TextView mItemName, mItemPrice, mItemLocation, mItemStatus;
    ItemClickListener itemClickListener;

    TabRequestHolder(@NonNull View itemView) {
        super(itemView);

        this.mImageView = itemView.findViewById(R.id.iv_inside_category_item);
        this.mItemPrice = itemView.findViewById(R.id.tv_category_item_price);
        this.mItemLocation = itemView.findViewById(R.id.tv_category_item_location);
        this.mItemName = itemView.findViewById(R.id.tv_category_item_name);
        this.mItemStatus = itemView.findViewById(R.id.tv_category_item_status);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClickListener(v, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }
}
