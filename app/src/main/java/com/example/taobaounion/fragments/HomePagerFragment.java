package com.example.taobaounion.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaounion.R;
import com.example.taobaounion.TicketActivity;
import com.example.taobaounion.adapter.HomePagerContentAdapter;
import com.example.taobaounion.adapter.LooperPagerAdapter;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.presenter.CategoryPagerPresenterImpl;
import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.presenter.TicketPresenterImpl;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ICategoryPagerCallback;
import com.example.taobaounion.view.custom.AutoLoopViewPager;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.lcodecore.tkrefreshlayout.views.TBNestedScrollView;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, HomePagerContentAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperPagerItemClickListener {

    private ICategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialId;
    private HomePagerContentAdapter mHomePagerContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;
    private AutoLoopViewPager mLooperPager;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment fragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_MATERIAL_ID, category.getId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.home_title)
    public TextView mHomePagerTitle;

    @BindView(R.id.looper_point_container)
    public LinearLayout mLooperPointContainer;

    @BindView(R.id.home_pager_parent)
    public LinearLayout mHomePagerParent;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout mHomePagerHeaderContainer;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout mHomePagerRefresh;

    @BindView(R.id.home_pager_nested_scroll_view)
    public TBNestedScrollView homePagerScrollView;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void onResume() {
        super.onResume();
        mLooperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLooperPager.stopLoop();
    }

    @Override
    protected void initListener() {

        mHomePagerContentAdapter.setOnItemClickListener(this);

        mLooperPagerAdapter.setOnLooperPagerItemClickListener(this);

        mHomePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mHomePagerHeaderContainer == null) {
                    return;
                }
                int headerHeight = mHomePagerHeaderContainer.getMeasuredHeight();
                homePagerScrollView.setHeaderHeight(headerHeight);

                if (mHomePagerParent == null) {
                    return;
                }
                int measuredHeight = mHomePagerParent.getMeasuredHeight();

                if (mContentList == null) {
                    return;
                }
                ViewGroup.LayoutParams layoutParams = mContentList.getLayoutParams();
                layoutParams.height = measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                if(measuredHeight != 0){
                    mHomePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        mLooperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int dataSize = mLooperPagerAdapter.getDataSize();
                if(dataSize == 0) return;
                int targetPosition = position % dataSize;
                // 切换指示器
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mHomePagerRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {

                mHomePagerRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCategoryPagerPresenter != null) {
                            mCategoryPagerPresenter.loadMore(mMaterialId);
                        }
                        mHomePagerRefresh.finishLoadmore();
                    }
                }, 3000);
            }
        });
    }

    /**
     * 切换指示器
     * @param targetPosition
     */
    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < mLooperPointContainer.getChildCount(); i++) {
            View dot = mLooperPointContainer.getChildAt(i);
            if(i == targetPosition){
                dot.setBackgroundResource(R.drawable.shape_indicator_dot_selected);
            }else{
                dot.setBackgroundResource(R.drawable.shape_indicator_dot_normal);
            }
        }
    }

    @Override
    protected void initView(View rootView) {
        mLooperPager = rootView.findViewById(R.id.looper_view_pager);

        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect,  View view,  RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
                outRect.left = 8;
                outRect.right = 8;
            }
        });
        // 创建适配器
        mHomePagerContentAdapter = new HomePagerContentAdapter();
        mContentList.setAdapter(mHomePagerContentAdapter);

        // 创建轮播图适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        mLooperPager.setAdapter(mLooperPagerAdapter);

        //设置Refresh相关内容
        mHomePagerRefresh.setEnableRefresh(false);
        mHomePagerRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = PresenterManager.getInstance().getCategoryPagerPresenter();
        mCategoryPagerPresenter.registerCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
            mMaterialId = arguments.getInt(Constants.KEY_HOME_MATERIAL_ID);
            LogUtils.d(this, "title..." + title);
            LogUtils.d(this, "materialId..." + mMaterialId);
            mHomePagerTitle.setText(title);
            // 加载数据
            if (mCategoryPagerPresenter != null) {
                mCategoryPagerPresenter.getContentByCategoryId(mMaterialId);
            }
        }
    }

    @Override
    protected void release() {
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.unregisterCallback(this);
        }
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataDTO> contents) {
        //数据列表加载
        if (mHomePagerContentAdapter != null) {
            mHomePagerContentAdapter.setDataList(contents);
        }
        setUpState(State.SUCCESS);
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        // 网络错误
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoadMoreError() {
        if (mHomePagerRefresh != null) {
            mHomePagerRefresh.finishLoadmore();
        }
        ToastUtil.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onLoadMoreEmpty() {
        if (mHomePagerRefresh != null) {
            mHomePagerRefresh.finishLoadmore();
        }
        ToastUtil.showToast("没有更多商品");
    }

    @Override
    public void onLoadMoreLoaded(List<HomePagerContent.DataDTO> contents) {
        // 添加到适配器底部
        mHomePagerContentAdapter.addData(contents);
        if (mHomePagerRefresh != null) {
            mHomePagerRefresh.finishLoadmore();
            ToastUtil.showToast("已为您加载" + contents.size() + "个商品。");
        }
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataDTO> contents) {
        LogUtils.d(this, "looper data size + " + contents.size());
        mLooperPagerAdapter.setData(contents);

        // 设置轮播图的中间值
        mLooperPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % contents.size());
        mLooperPointContainer.removeAllViews();
        //添加点
        for (int i = 0; i < contents.size(); i++) {
            View dot = new View(getContext());
            int size = SizeUtils.dip2px(getContext(), 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(), 5);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(), 5);
            dot.setLayoutParams(layoutParams);
            if(i == 0){
                dot.setBackgroundResource(R.drawable.shape_indicator_dot_selected);
            }else{
                dot.setBackgroundResource(R.drawable.shape_indicator_dot_normal);
            }
            mLooperPointContainer.addView(dot);
        }
    }

    @Override
    public void onListItemClick(HomePagerContent.DataDTO item) {
        // 列表item被点击了
        LogUtils.d(this, item.getTitle());
        handleItemClick(item);
    }

    private void handleItemClick(HomePagerContent.DataDTO item) {
        //处理数据
        String title = item.getTitle();
        //领券页面
        String url = item.getCoupon_click_url();
        if (TextUtils.isEmpty(url)) {
            // 详情页面
            url = item.getClick_url();
        }
        String cover = item.getPict_url();
        // 拿到TicketPresenter对象
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url , cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }

    @Override
    public void onLooperItemClick(HomePagerContent.DataDTO item) {
        // 轮播图item被点击了
        LogUtils.d(this, item.getTitle());
        handleItemClick(item);
    }
}
