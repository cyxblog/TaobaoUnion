package com.example.taobaounion.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.taobaounion.fragments.HomePagerFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    List<Categories.DataBean> mData = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mData.get(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        HomePagerFragment homePagerFragment = HomePagerFragment.newInstance(mData.get(position));
        return homePagerFragment;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public void setCategories(Categories categories) {
        mData.clear();
        List<Categories.DataBean> data = categories.getData();
        mData.addAll(data);
        notifyDataSetChanged();
    }
}
