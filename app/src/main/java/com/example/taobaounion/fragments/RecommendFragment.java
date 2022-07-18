package com.example.taobaounion.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.TicketActivity;
import com.example.taobaounion.adapter.RecommendLeftListAdapter;
import com.example.taobaounion.adapter.RecommendRightListAdapter;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.model.domain.RecommendCategories;
import com.example.taobaounion.model.domain.RecommendContent;
import com.example.taobaounion.presenter.IRecommendPresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.view.IRecommendCallback;

import java.util.List;

import butterknife.BindView;

public class RecommendFragment extends BaseFragment implements IRecommendCallback, RecommendLeftListAdapter.OnLeftItemClickListener, RecommendRightListAdapter.OnRightListItemClickListener {

    private IRecommendPresenter mRecommendPresenter;

    @BindView(R.id.recommend_left_list)
    public RecyclerView mRecommendLeftList;

    @BindView(R.id.recommend_right_list)
    public RecyclerView mRecommendRightList;

    private RecommendLeftListAdapter mRecommendLeftAdapter;
    private RecommendRightListAdapter mRecommendRightListAdapter;

    @Override
    protected void initPresenter() {
        mRecommendPresenter = PresenterManager.getInstance().getRecommendPresenter();
        mRecommendPresenter.registerCallback(this);
    }

    @Override
    protected void loadData() {
        mRecommendPresenter.getRecommendCategories();
    }

    @Override
    protected void release() {
        super.release();
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unregisterCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);

        // 左侧列表
        LinearLayoutManager leftLinearManager = new LinearLayoutManager(getContext());
        mRecommendLeftList.setLayoutManager(leftLinearManager);
        mRecommendLeftAdapter = new RecommendLeftListAdapter();
        mRecommendLeftList.setAdapter(mRecommendLeftAdapter);

        // 右侧列表
        LinearLayoutManager rightLinearManager = new LinearLayoutManager(getContext());
        mRecommendRightList.setLayoutManager(rightLinearManager);
        mRecommendRightListAdapter = new RecommendRightListAdapter();
        mRecommendRightList.setAdapter(mRecommendRightListAdapter);
        mRecommendRightList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 4);
                outRect.bottom = SizeUtils.dip2px(getContext(), 4);
                outRect.left = SizeUtils.dip2px(getContext(), 6);
                outRect.right = SizeUtils.dip2px(getContext(), 6);
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        if (mRecommendLeftAdapter != null) {
            mRecommendLeftAdapter.setOnLeftItemClickListener(this);
        }

        if (mRecommendRightListAdapter != null)
            mRecommendRightListAdapter.setOnRightListItemClickListener(this);
    }

    @Override
    public void onRecommendCategoriesLoaded(List<RecommendCategories.DataDTO> result) {
        setUpState(State.SUCCESS);
        LogUtils.d(this, "onRecommendCategoriesLoaded...." + result);
        mRecommendPresenter.getContentByCategory(result.get(0));
        if (mRecommendLeftAdapter != null) {
            mRecommendLeftAdapter.setData(result);
        }
    }

    @Override
    public void onContentLoaded(RecommendContent result) {
        if (mRecommendRightListAdapter != null) {
            List<RecommendContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> map_data =
                    result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
            mRecommendRightListAdapter.setData(map_data);
        }
        mRecommendRightList.scrollToPosition(0);
    }

    @Override
    protected void onRetryClick() {
        // 重试
        if (mRecommendPresenter != null) {
            mRecommendPresenter.reloadContent();
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

    }

    /**
     * 当左侧列表项被点击时
     * @param dataDTO
     */
    @Override
    public void onLeftListItemClick(RecommendCategories.DataDTO dataDTO) {
        if (mRecommendPresenter != null) {
            mRecommendPresenter.getContentByCategory(dataDTO);
        }
    }

    @Override
    public void onRightListItemClick(RecommendContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO item) {
        handleItemClick(item);
    }

    private void handleItemClick(RecommendContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO item) {
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
}
