package com.example.nitipaja.ui.category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.nitipaja.R;
import com.example.nitipaja.ui.home.orderProduct.OrderProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class CategoryActivity extends AppCompatActivity {

    private String userID;
    private String categoryName;
    private TextView tvCategoryName;

    private RecyclerView mRecyclerView;
    private CategoryAdapter categoryAdapter;

    private FirebaseAuth fAuth;
    private DatabaseReference databaseReference;
    private ArrayList<CategoryModel> listProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        tvCategoryName = findViewById(R.id.tv_category_name);

        Intent intent = getIntent();
        categoryName = intent.getExtras().getString("CategoryName");

        tvCategoryName.setText(categoryName);

        listProduct = new ArrayList<>();

        mRecyclerView = findViewById(R.id.rv_inside_category);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("transaction");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    CategoryModel categoryModel = postSnapshot.getValue(CategoryModel.class);
                    if(categoryModel.getItemCategory().equals(categoryName)
                            && (!categoryModel.getUserID().equals(userID))
                            && (categoryModel.getItemStatus().equals("Menunggu"))
                    ){
                        listProduct.add(categoryModel);
                    }
                }
                categoryAdapter = new CategoryAdapter(CategoryActivity.this,listProduct);
                mRecyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}