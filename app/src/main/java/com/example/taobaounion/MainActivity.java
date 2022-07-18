package com.example.taobaounion;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

import com.example.taobaounion.base.BaseActivity;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.fragments.HomeFragment;
import com.example.taobaounion.fragments.HomePagerFragment;
import com.example.taobaounion.fragments.RedPacketFragment;
import com.example.taobaounion.fragments.SearchFragment;
import com.example.taobaounion.fragments.RecommendFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private RedPacketFragment mRedPacketFragment;
    private SearchFragment mSearchFragment;
    private RecommendFragment mRecommendFragment;
    private FragmentManager mFm;
    private HomePagerFragment mHomePagerFragment;

    @Override
    protected void initView() {
        initFragments();
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initListener() {
        initEvent();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private void initFragments() {
        mHomeFragment = new HomeFragment();
        mRedPacketFragment = new RedPacketFragment();
        mSearchFragment = new SearchFragment();
        mRecommendFragment = new RecommendFragment();
        mFm = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    private void initEvent() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    switchFragment(mHomeFragment);
                }else if(item.getItemId() == R.id.red_packet){
                    switchFragment(mRedPacketFragment);

                } else if(item.getItemId() == R.id.selected){
                    switchFragment(mRecommendFragment);

                } else if(item.getItemId() == R.id.search){
                    switchFragment(mSearchFragment);

                }
                return true;
            }
        });
    }

    /**
     * 上一次显示的fragment
     */
    private BaseFragment lastFragment = null;

    private void switchFragment(BaseFragment fragment) {
        FragmentTransaction transaction = mFm.beginTransaction();

        if (!fragment.isAdded()) {
            transaction.add(R.id.main_page_container, fragment);
        }else{
            transaction.show(fragment);
        }
        if (lastFragment != null) {
            transaction.hide(lastFragment);
        }
        lastFragment = fragment;
//        transaction.replace(R.id.main_page_container, fragment);
        transaction.commit();
    }
}