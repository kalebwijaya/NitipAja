package com.example.nitipaja.ui.category;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nitipaja.R;

public class ItemDetailsActivity extends AppCompatActivity {

    String userID;
    TextView itemName, itemLocation, itemPrice, itemDescription, itemQuantity;
    ImageView itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        itemName = findViewById(R.id.tv_item_details_name);
        itemLocation = findViewById(R.id.tv_item_details_location);
        itemDescription = findViewById(R.id.tv_item_details_description);
        itemPrice = findViewById(R.id.tv_item_details_price);
        itemQuantity = findViewById(R.id.tv_item_details_quantity);
        itemImage = findViewById(R.id.iv_item_details);

        Intent intent = getIntent();
        byte[] bytes = getIntent().getByteArrayExtra("itemImage");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        userID = intent.getStringExtra("userID");
        itemName.setText(intent.getStringExtra("itemName"));
        itemLocation.setText(intent.getStringExtra("itemLocation"));
        itemDescription.setText(intent.getStringExtra("itemDescription"));
        itemPrice.setText(intent.getStringExtra("itemPrice"));
        itemQuantity.setText("Jumlah : " + intent.getIntExtra("itemQuantitiy",1));
        itemImage.setImageBitmap(bitmap);

    }
}