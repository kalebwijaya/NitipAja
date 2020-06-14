package com.example.nitipaja.ui.transaction.request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nitipaja.R;
import com.example.nitipaja.ui.login.UserModel;
import com.example.nitipaja.ui.transaction.takeOrder.TabTakeOrderDetailsActivity;
import com.example.nitipaja.ui.transaction.takeOrder.TabTakeOrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TabRequestDetailsActivity extends AppCompatActivity {

    private String userID;
    private String itemID;
    private String childName;
    private TextView itemName, itemLocation, itemPrice, itemDescription, itemQuantity, itemStatus;
    private ImageView itemImage;
    private Button btnCancel, btnWA;

    private TabRequestModel currentRequest;
    private DatabaseReference databaseReference, userReference;

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

        databaseReference = FirebaseDatabase.getInstance().getReference("transaction");
        userReference = FirebaseDatabase.getInstance().getReference("user");
        Intent intent = getIntent();
        itemID = intent.getStringExtra("itemID");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    TabRequestModel tabRequestModel = postSnapshot.getValue(TabRequestModel.class);
                    if(tabRequestModel.getItemID().equals(itemID)){
                        currentRequest = tabRequestModel;
                        childName = postSnapshot.getKey();
                        setValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        byte[] bytes = getIntent().getByteArrayExtra("itemImage");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        itemImage.setImageBitmap(bitmap);

        if(intent.getStringExtra("itemStatus").equals("Menunggu")){
            btnWA.setVisibility(View.INVISIBLE);
        }else{
            btnWA.setVisibility(View.VISIBLE);
            btnCancel.setActivated(false);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder changeStatusDialog = new AlertDialog.Builder(v.getContext());
                changeStatusDialog.setTitle("Batalkan Pesanan");
                changeStatusDialog.setMessage("Batalkan pesanan " + currentRequest.getItemName());
                changeStatusDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child(childName).removeValue();
                        TabRequestDetailsActivity.this.finish();
                    }
                });

                changeStatusDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                changeStatusDialog.create().show();
            }
        });

        btnWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            UserModel userModel = postSnapshot.getValue(UserModel.class);
                            if(userModel.getUserID().equals(userID)){
                                String url = "https://api.whatsapp.com/send?phone=+62" + userModel.getUserPhonenumber();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void setValue(){
        userID = currentRequest.getUserID();
        itemName.setText(currentRequest.getItemName());
        itemLocation.setText(currentRequest.getItemLocation());
        itemDescription.setText(currentRequest.getItemDescription());
        itemPrice.setText(currentRequest.getItemPrice());
        itemQuantity.setText("Jumlah : " + currentRequest.getItemQuantity());
        itemStatus.setText("Status : " + currentRequest.getItemStatus());
    }
}