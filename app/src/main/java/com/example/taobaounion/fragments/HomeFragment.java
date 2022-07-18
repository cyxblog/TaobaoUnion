package com.example.taobaounion.fragments;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.example.taobaounion.R;
import com.example.taobaounion.adapter.HomePagerAdapter;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.presenter.HomePresenterImpl;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    @BindView(R.id.home_pager)
    public ViewPager mHomePager;

    private static final String TAG = "HomeFragment";
    private IHomePresenter mHomePresenter;
    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        mTabLayout.setupWithViewPager(mHomePager);
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mHomePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void initPresenter() {
        // 创建presenter
        mHomePresenter = PresenterManager.getInstance().getHomePresenter();
        Log.d(TAG, "initPresenter: ");
        mHomePresenter.registerCallback(this);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout, container, false);
    }

    @Override
    protected void loadData() {
        // 加载数据
        mHomePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        setUpState(State.SUCCESS);
        LogUtils.d(HomeFragment.this, categories.getData().size() + "...onCategoriesLoaded");
        if (mHomePagerAdapter != null) {
            mHomePagerAdapter.setCategories(categories);
        }
    }

    /**
     * 如果网络错误，则重新点击重试
     */
    @Override
    protected void onRetryClick() {
        if (mHomePresenter != null) {
            mHomePresenter.getCategories();
        }
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.LOADING);
    }

    @Override
    protected void release() {
        super.release();
        //取消资源回调
        if (mHomePresenter != null) {
            mHomePresenter.unregisterCallback(this);
        }
    }
}
