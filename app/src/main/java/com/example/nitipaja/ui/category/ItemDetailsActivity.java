package com.example.nitipaja.ui.category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitipaja.MainActivity;
import com.example.nitipaja.R;
import com.example.nitipaja.ui.transaction.takeOrder.TabTakeOrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemDetailsActivity extends AppCompatActivity {

    private String userID, itemID, childID;

    private TextView itemName, itemLocation, itemPrice, itemDescription, itemQuantity;
    private ImageView itemImage;
    private Button btnMakeRequest, btnTakeRequest;

    private CategoryModel currentItem;
    private FirebaseUser fUser;

    private DatabaseReference databaseReference;

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
        btnMakeRequest = findViewById(R.id.btn_make_request);
        btnTakeRequest = findViewById(R.id.btn_take_request);


        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = fUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("transaction");
        final Intent intent = getIntent();
        itemID = intent.getStringExtra("itemID");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    CategoryModel categoryModel = postSnapshot.getValue(CategoryModel.class);
                    if(categoryModel.getItemID().equals(itemID)){
                        currentItem = categoryModel;
                        childID = postSnapshot.getKey();
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

        itemName.setText(intent.getStringExtra("itemName"));
        itemLocation.setText(intent.getStringExtra("itemLocation"));
        itemDescription.setText(intent.getStringExtra("itemDescription"));
        itemPrice.setText(intent.getStringExtra("itemPrice"));
        itemQuantity.setText("Jumlah : " + intent.getStringExtra("itemQuantity"));
        itemImage.setImageBitmap(bitmap);

        btnMakeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText itemQuantity = new EditText(v.getContext());
                itemQuantity.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.background, null));
                itemQuantity.setPadding(20,0,20,0);
                LinearLayout container = new LinearLayout(getApplicationContext());
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100);
                lp.setMargins(40, 0, 40, 0);
                itemQuantity.setGravity(Gravity.CENTER_VERTICAL| Gravity.LEFT);
                itemQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                itemQuantity.setLines(1);
                itemQuantity.setMaxLines(1);
                itemQuantity.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
                itemQuantity.setLayoutParams(lp);
                itemQuantity.setHint("Jumlah Barang");
                container.addView(itemQuantity, lp);

                AlertDialog.Builder makeRequestDialog = new AlertDialog.Builder(v.getContext());
                makeRequestDialog.setTitle("Ikut Nitip");
                makeRequestDialog.setMessage("Masukkan jumlah barang");
                makeRequestDialog.setView(container);

                makeRequestDialog.setPositiveButton("Titip", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentItem.setUserID(userID);
                        currentItem.setItemQuantity(itemQuantity.getText().toString());
                        databaseReference.child(childID).setValue(currentItem);
                        ItemDetailsActivity.this.finish();
                    }
                });

                makeRequestDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                makeRequestDialog.create().show();

            }
        });

        btnTakeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder changeStatusDialog = new AlertDialog.Builder(v.getContext());
                changeStatusDialog.setTitle("Ambil Pesanan");
                changeStatusDialog.setMessage("Ambil pesanan " + intent.getStringExtra("itemName"));
                changeStatusDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentItem.setTakenOrderBy(userID);
                        databaseReference.child(childID).setValue(currentItem);
                        ItemDetailsActivity.this.finish();
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

    }
}