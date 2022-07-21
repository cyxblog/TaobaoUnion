package com.example.taobaounion.fragments;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.TicketActivity;
import com.example.taobaounion.adapter.OnSellContentListAdapter;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.model.domain.RecommendContent;
import com.example.taobaounion.presenter.IOnSellPresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.TicketUtil;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.IOnSellCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.BindView;

public class OnSellFragment extends BaseFragment implements IOnSellCallback, OnSellContentListAdapter.OnSellPageItemClickListener {


    private IOnSellPresenter mOnSellPresenter;

    private static final int DEFAULT_SPAN_COUNT = 2;

    @BindView(R.id.on_sell_content_list)
    public RecyclerView onSellContentList;

    @BindView(R.id.on_sell_fresh_layout)
    public TwinklingRefreshLayout mTwinklingRefreshLayout;

    private OnSellContentListAdapter mOnSellContentListAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_on_sell;
    }

    @Override
    protected void initView(View rootView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT);
        onSellContentList.setLayoutManager(gridLayoutManager);
        mOnSellContentListAdapter = new OnSellContentListAdapter();
        onSellContentList.setAdapter(mOnSellContentListAdapter);

        mTwinklingRefreshLayout.setEnableLoadmore(true);
        mTwinklingRefreshLayout.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mOnSellPresenter = PresenterManager.getInstance().getOnSellPresenter();
        mOnSellPresenter.registerCallback(this);
    }

    @Override
    protected void release() {
        if (mOnSellPresenter != null) {
            mOnSellPresenter.unregisterCallback(this);
        }
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_with_bar_layout, container, false);
        TextView titleView = rootView.findViewById(R.id.fragment_title);
        titleView.setText(R.string.text_on_sell_title);
        return rootView;
    }

    @Override
    protected void loadData() {
        if (mOnSellPresenter != null) {
            mOnSellPresenter.getOnSellContent();
        }
    }

    @Override
    protected void initListener() {
        mOnSellContentListAdapter.setOnSellPageItemClickListener(this);

        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mOnSellPresenter != null) {
                    mOnSellPresenter.loadMore();
                }

            }
        });
    }

    @Override
    public void retry() {
        if (mOnSellPresenter != null) {
            mOnSellPresenter.reload();
        }
    }

    @Override
    public void onContentLoaded(OnSellContent result) {
        setUpState(State.SUCCESS);
        // 获取到特惠内容
        List<OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> map_data = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        mOnSellContentListAdapter.setData(map_data);
    }

    @Override
    public void onMoreLoaded(OnSellContent result) {
        mTwinklingRefreshLayout.finishLoadmore();
        List<OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> dataList = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        mOnSellContentListAdapter.onMoreLoaded(dataList);
        LogUtils.d(OnSellFragment.this, "加载" + dataList.size() + "个商品");
        ToastUtil.showToast("已为您加载" + dataList.size() + "个商品");
    }

    @Override
    public void onMoreLoadError() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtil.showToast("商品加载失败，请稍后重试");
    }

    @Override
    public void onMoreLoadEmpty() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtil.showToast("没有更多商品");
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
        setUpState(State.EMPTY);
    }

    @Override
    public void onSellItemClick(IBaseInfo data) {
        handleItemClick(data);
    }

    private void handleItemClick(IBaseInfo item) {
        TicketUtil.toTicketPage(getContext(), item);
    }
}
