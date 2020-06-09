package com.example.nitipaja.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitipaja.R;
import com.example.nitipaja.ui.category.CategoryActivity;

import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.MyViewHolder>{

    private Context mContext;
    private List<Category> mData;

    public CategoryRecyclerViewAdapter(Context mContext, List<Category> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.category_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tv_category_name.setText(mData.get(position).getCategoryName());
        holder.iv_category_image.setImageResource(mData.get(position).getCategoryImage());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, CategoryActivity.class);
                mIntent.putExtra("CategoryName",mData.get(position).getCategoryName());
                mContext.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_category_name;
        ImageView iv_category_image;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);

            tv_category_name = itemView.findViewById(R.id.tv_category_name);
            iv_category_image = itemView.findViewById(R.id.iv_category_img);
            cardView = itemView.findViewById(R.id.cv_category_item);
        }
    }
}
