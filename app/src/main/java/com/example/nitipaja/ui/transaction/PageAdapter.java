package com.example.nitipaja.ui.transaction;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.nitipaja.ui.transaction.request.TabRequestFragment;
import com.example.nitipaja.ui.transaction.takeOrder.TabTakeOrderFragment;

public class PageAdapter extends FragmentStatePagerAdapter {

    int countTab;

    public PageAdapter(FragmentManager fm, int countertab) {
        super(fm);
        this.countTab = countertab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                TabRequestFragment tabRequestFragment = new TabRequestFragment();
                return tabRequestFragment;
            case 1 :
                TabTakeOrderFragment tabTakeOrderFragment = new TabTakeOrderFragment();
                return tabTakeOrderFragment;
            default :
                return null;
        }
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
