package com.example.nitipaja.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitipaja.R;
import com.example.nitipaja.ui.category.CategoryActivity;
import com.example.nitipaja.ui.category.CategoryAdapter;
import com.example.nitipaja.ui.category.CategoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private String userID;
    private String keyWord;

    private EditText mSearchBar;
    private RecyclerView mRecyclerView;
    private SearchAdapter searchAdapter;

    private FirebaseAuth fAuth;
    private DatabaseReference databaseReference;
    private ArrayList<SearchModel> listProduct;
    private ArrayList<SearchModel> allProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        keyWord = getIntent().getStringExtra("searchKey");

        listProduct = new ArrayList<>();
        allProduct = new ArrayList<>();

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();

        mSearchBar = findViewById(R.id.et_search_search_bar);
        mSearchBar.setText(keyWord);
        mRecyclerView = findViewById(R.id.rv_search_produk_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("transaction");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    SearchModel searchModel = postSnapshot.getValue(SearchModel.class);
                    if(searchModel.getItemName().toLowerCase().contains(keyWord.toLowerCase())
                            && (!searchModel.getUserID().equals(userID))
                            && (searchModel.getItemStatus().equals("Menunggu"))
                    ){
                        listProduct.add(searchModel);
                    }
                    allProduct.add(searchModel);
                }
                searchAdapter = new SearchAdapter(getApplicationContext(),listProduct);
                mRecyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    private void filter(String keyword){
        ArrayList<SearchModel> fillteredLIst = new ArrayList<>();

        for(SearchModel item : allProduct){
            if(item.getItemName().toLowerCase().contains(keyword.toLowerCase())){
                fillteredLIst.add(item);
            }
        }

        searchAdapter.fillterList(fillteredLIst);
    }
}