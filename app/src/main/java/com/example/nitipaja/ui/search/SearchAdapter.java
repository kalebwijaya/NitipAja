package com.example.nitipaja.ui.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitipaja.R;
import com.example.nitipaja.ui.ItemClickListener;
import com.example.nitipaja.ui.category.ItemDetailsActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

    Context mContext;
    ArrayList<SearchModel> models;

    public SearchAdapter(Context mContext, ArrayList<SearchModel> models) {
        this.mContext = mContext;
        this.models = models;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inside_category_item,null);

        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchHolder holder, int position) {
        holder.mItemLocation.setText(models.get(position).getItemLocation());
        holder.mItemPrice.setText(models.get(position).getItemPrice());
        holder.mItemName.setText(models.get(position).getItemName());

        SearchAdapter.LoadImage loadImage = new SearchAdapter.LoadImage(holder.mImageView);
        loadImage.execute(models.get(position).getImageURL());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String gItemName = models.get(position).getItemName();
                String gItemPrice = models.get(position).getItemPrice();
                String gItemLocation = models.get(position).getItemLocation();
                String gItemQuantity = models.get(position).getItemQuantity();
                String gItemDescription = models.get(position).getItemDescription();
                String gUserID = models.get(position).getUserID();
                String gItemID = models.get(position).getItemID();

                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.mImageView.getDrawable();

                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream);

                byte[] bytes = stream.toByteArray();

                Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                intent.putExtra("itemName",gItemName);
                intent.putExtra("itemPrice",gItemPrice);
                intent.putExtra("itemLocation",gItemLocation);
                intent.putExtra("itemQuantity",gItemQuantity);
                intent.putExtra("itemDescription",gItemDescription);
                intent.putExtra("itemImage",bytes);
                intent.putExtra("userID",gUserID);
                intent.putExtra("itemID",gItemID);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void fillterList(ArrayList<SearchModel> fillteredLIst) {
        models = fillteredLIst;
        notifyDataSetChanged();
    }


    private class LoadImage extends AsyncTask<String, Void, Bitmap>{
        ImageView imageView;

        public LoadImage(ImageView ivResult){
            this.imageView = ivResult;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(urlLink).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
