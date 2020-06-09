package com.example.nitipaja.ui.transaction.request;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nitipaja.R;

public class TabRequestDetailsActivity extends AppCompatActivity {

    private String userID;
    private TextView itemName, itemLocation, itemPrice, itemDescription, itemQuantity, itemStatus;
    private ImageView itemImage;
    private Button btnCancel, btnWA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_request_details);

        itemName = findViewById(R.id.tv_tab_request_item_name);
        itemLocation = findViewById(R.id.tv_tab_request_location);
        itemDescription = findViewById(R.id.tv_tab_request_description);
        itemPrice = findViewById(R.id.tv_tab_request_price);
        itemQuantity = findViewById(R.id.tv_tab_request_quantity);
        itemStatus = findViewById(R.id.tv_tab_request_status);
        itemImage = findViewById(R.id.iv_tab_request_details);
        btnWA = findViewById(R.id.btn_request_WA);
        btnCancel = findViewById(R.id.btn_tab_request_cancel);

        Intent intent = getIntent();

        byte[] bytes = getIntent().getByteArrayExtra("itemImage");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        userID = intent.getStringExtra("userID");
        itemName.setText(intent.getStringExtra("itemName"));
        itemLocation.setText(intent.getStringExtra("itemLocation"));
        itemDescription.setText(intent.getStringExtra("itemDescription"));
        itemPrice.setText(intent.getStringExtra("itemPrice"));
        itemQuantity.setText("Jumlah : " + intent.getIntExtra("itemQuantitiy",1));
        itemStatus.setText("Status : " + intent.getStringExtra("itemStatus"));
        itemImage.setImageBitmap(bitmap);

        if(intent.getStringExtra("itemStatus").equals("Menunggu")){
            btnWA.setVisibility(View.INVISIBLE);
        }else{
            btnWA.setVisibility(View.VISIBLE);
            btnCancel.setActivated(false);
        }

    }
}