package com.example.nitipaja.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitipaja.R;
import com.example.nitipaja.ui.home.orderProduct.OrderProductActivity;
import com.example.nitipaja.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<Category> categoryList;
    private Button btnToOrder;
    private EditText mSearchBar;

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

        mSearchBar = root.findViewById(R.id.et_home_search_bar);
        mSearchBar.setOnEditorActionListener(editorListener);

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

    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("searchKey",mSearchBar.getText().toString());
                getContext().startActivity(intent);
            }
            return false;
        }
    };
}