package com.example.nitipaja.ui.transaction.takeOrder;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TabTakeOrderFragment extends Fragment {

    private String userID;
    private RecyclerView mRecyclerView;
    private TabTakeOrderAdapter tabTakeOrderAdapter;

    private FirebaseAuth fAuth;
    private DatabaseReference databaseReference;
    private ArrayList<TabTakeOrderModel> listProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tab_take_order, container, false);

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();

        listProduct = new ArrayList<>();

        mRecyclerView = root.findViewById(R.id.rv_transaction_take_order);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference("transaction");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listProduct.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    TabTakeOrderModel tabTakeOrderModel = postSnapshot.getValue(TabTakeOrderModel.class);
                    if(tabTakeOrderModel.getTakenOrderBy().equals(userID)){
                        listProduct.add(tabTakeOrderModel);
                    }
                }
                tabTakeOrderAdapter = new TabTakeOrderAdapter(getContext(),listProduct);
                mRecyclerView.setAdapter(tabTakeOrderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }


}
