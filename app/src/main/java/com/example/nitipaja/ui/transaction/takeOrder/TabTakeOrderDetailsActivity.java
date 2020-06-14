package com.example.nitipaja.ui.transaction.takeOrder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitipaja.R;
import com.example.nitipaja.ui.login.LoginActivity;
import com.example.nitipaja.ui.login.UserModel;
import com.example.nitipaja.ui.profile.EditProfileActivity;
import com.example.nitipaja.ui.transaction.request.TabRequestDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TabTakeOrderDetailsActivity extends AppCompatActivity {

    private String userID;
    private String itemID;
    private String childName;
    private TextView itemName, itemLocation, itemPrice, itemDescription, itemQuantity, itemStatus;
    private ImageView itemImage;
    private Button btnStatus, btnCancel, btnWA;

    private DatabaseReference databaseReference, userReference;

    private TabTakeOrderModel currentTakenOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_take_order_details);

        itemName = findViewById(R.id.tv_tab_take_order_item_name);
        itemLocation = findViewById(R.id.tv_tab_take_order_location);
        itemDescription = findViewById(R.id.tv_tab_take_order_description);
        itemPrice = findViewById(R.id.tv_tab_take_order_price);
        itemQuantity = findViewById(R.id.tv_tab_take_order_quantity);
        itemStatus = findViewById(R.id.tv_tab_take_order_status);
        itemImage = findViewById(R.id.iv_tab_take_order_details);
        btnStatus = findViewById(R.id.btn_tab_take_order_update);
        btnCancel = findViewById(R.id.btn_tab_take_order_cancel);
        btnWA = findViewById(R.id.btn_take_order_WA);

        databaseReference = FirebaseDatabase.getInstance().getReference("transaction");
        userReference = FirebaseDatabase.getInstance().getReference("user");

        final Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        itemID = intent.getStringExtra("itemID");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    TabTakeOrderModel tabTakeOrderModel = postSnapshot.getValue(TabTakeOrderModel.class);
                    if(tabTakeOrderModel.getItemID().equals(itemID)){
                        currentTakenOrder = tabTakeOrderModel;
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

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Spinner statusSpinner = new Spinner(v.getContext());

                statusSpinner.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.background, null));

                LinearLayout container = new LinearLayout(getApplicationContext());
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100);
                lp.setMargins(40, 0, 40, 0);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.status, R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                statusSpinner.setAdapter(adapter);

                container.addView(statusSpinner,lp);

                AlertDialog.Builder changeStatusDialog = new AlertDialog.Builder(v.getContext());
                changeStatusDialog.setTitle("Ubah Status");
                changeStatusDialog.setMessage("Ubah status pesanan " + intent.getStringExtra("itemName"));
                changeStatusDialog.setView(container);

                changeStatusDialog.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String statusBaru = statusSpinner.getSelectedItem().toString();
                        currentTakenOrder.setItemStatus(statusBaru);
                        databaseReference.child(childName).setValue(currentTakenOrder);
                        finish();
                        startActivity(getIntent());
                    }
                });

                changeStatusDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                changeStatusDialog.create().show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder changeStatusDialog = new AlertDialog.Builder(v.getContext());
                changeStatusDialog.setTitle("Batalkan Pesanan");
                changeStatusDialog.setMessage("Batalkan pesanan " + currentTakenOrder.getItemName());
                changeStatusDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child(childName).removeValue();
                        TabTakeOrderDetailsActivity.this.finish();
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
        userID = currentTakenOrder.getUserID();
        itemName.setText(currentTakenOrder.getItemName());
        itemLocation.setText(currentTakenOrder.getItemLocation());
        itemDescription.setText(currentTakenOrder.getItemDescription());
        itemPrice.setText(currentTakenOrder.getItemPrice());
        itemQuantity.setText("Jumlah : " + currentTakenOrder.getItemQuantity());
        itemStatus.setText("Status : " + currentTakenOrder.getItemStatus());
    }
}