package com.example.nitipaja.ui.transaction.request;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class TabRequestFragment extends Fragment {

    private String userID;
    private RecyclerView mRecyclerView;
    private TabRequestAdapter tabRequestAdapter;

    private FirebaseAuth fAuth;
    private DatabaseReference databaseReference;
    private ArrayList<TabRequestModel> listProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_tab_request, container, false);

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();

        listProduct = new ArrayList<>();

        mRecyclerView = root.findViewById(R.id.rv_transaction_request);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference("transaction");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listProduct.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    TabRequestModel tabRequestModel = postSnapshot.getValue(TabRequestModel.class);
                    if(tabRequestModel.getUserID().equals(userID)){
                        listProduct.add(tabRequestModel);
                    }
                }
                tabRequestAdapter = new TabRequestAdapter(getContext(),listProduct);
                mRecyclerView.setAdapter(tabRequestAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;

    }
}
