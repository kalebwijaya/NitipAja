package com.example.nitipaja.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitipaja.R;
import com.example.nitipaja.ui.home.orderProduct.OrderProductActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<Category> categoryList;
    Button btnToOrder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        categoryList = new ArrayList<>();
        int[] categoryIcons = {R.drawable.food_icon,R.drawable.skin_care_icon,R.drawable.electronic_icon,R.drawable.toys_icon,
                R.drawable.souvenir_icon,R.drawable.merchandise_icon,R.drawable.fashion_icon,R.drawable.others_icon};
        String[] categories = this.getResources().getStringArray(R.array.category);
        for(int i=1;i<categories.length;i++){
            categoryList.add(new Category(categories[i],categoryIcons[i-1]));
        }

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        btnToOrder = root.findViewById(R.id.btn_home_to_order_item);

        btnToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrderProductActivity.class);
                getContext().startActivity(intent);
            }
        });

        RecyclerView rvCategory = root.findViewById(R.id.rv_category);
        CategoryRecyclerViewAdapter myAdapter = new CategoryRecyclerViewAdapter(getContext(), categoryList);
        rvCategory.setLayoutManager(new GridLayoutManager(getContext(),4));
        rvCategory.setAdapter(myAdapter);

        return root;
    }
}