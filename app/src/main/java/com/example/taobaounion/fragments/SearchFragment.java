package com.example.taobaounion.fragments;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.adapter.LinearItemContentAdapter;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.model.domain.SearchContent;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.presenter.ISearchPresenter;
import com.example.taobaounion.utils.KeyboardUtil;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.TicketUtil;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.ISearchCallback;
import com.example.taobaounion.view.custom.TextFlowLayout;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements ISearchCallback, TextFlowLayout.OnFlowTextItemClickListener, LinearItemContentAdapter.OnListItemClickListener {

    private ISearchPresenter mSearchPresenter;

    @BindView(R.id.search_history_flow_view)
    public TextFlowLayout mHistoryFlowLayout;

    @BindView(R.id.search_recommend_flow_view)
    public TextFlowLayout mRecommendFlowView;

    @BindView(R.id.search_history_container)
    public LinearLayout mHistoryContainer;

    @BindView(R.id.search_recommend_container)
    public LinearLayout mRecommendContainer;

    @BindView(R.id.search_history_delete_btn)
    public ImageView mHistoryDeleteBtn;

    @BindView(R.id.search_result_list)
    public RecyclerView mSearchResultList;

    @BindView(R.id.search_result_container)
    public TwinklingRefreshLayout mRefreshLayout;

    @BindView(R.id.search_btn)
    public TextView mSearchBtn;

    @BindView(R.id.search_clean_btn)
    public ImageView mSearchCleanBtn;

    @BindView(R.id.search_input_box)
    public EditText mSearchInputBox;

    private LinearItemContentAdapter mSearchResultAdapter;

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);

        mSearchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchResultList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, View view, RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2);
                outRect.left = SizeUtils.dip2px(getContext(), 2);
                outRect.right = SizeUtils.dip2px(getContext(), 2);
            }
        });
        mSearchResultAdapter = new LinearItemContentAdapter();
        mSearchResultList.setAdapter(mSearchResultAdapter);

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setEnableOverScroll(true);
    }

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerCallback(this);
    }

    @Override
    protected void release() {
        if (mSearchPresenter != null) {
            mSearchPresenter.unregisterCallback(this);
        }
    }

    @Override
    protected void initListener() {

        mHistoryFlowLayout.setOnFlowTextItemClickListener(this);
        mRecommendFlowView.setOnFlowTextItemClickListener(this);

        // 发起搜索
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果有内容则搜索
                String keyword = mSearchInputBox.getText().toString().trim();
                if (keyword.length() > 0) {
                    if (mSearchPresenter != null) {
//                        mSearchPresenter.doSearch(keyword);
                        toSearch(keyword);
                        KeyboardUtil.hide(getContext(), v);
                    }
                } else {
                    //隐藏键盘
                    KeyboardUtil.hide(getContext(), v);
                }
            }
        });

        mSearchCleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchInputBox.setText("");
                // 回到历史记录界面
                back2HistoryPage();
            }
        });

        // 监听输入框的内容变化
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchCleanBtn.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                mSearchBtn.setText(s.toString().trim().length() > 0 ? "搜索" : "取消");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSearchInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                    String keyword = v.getText().toString().trim();
                    if (TextUtils.isEmpty(keyword)) {
                        return false;
                    }
                    LogUtils.d(SearchFragment.this, "input text == > " + keyword);
                    if (mSearchPresenter != null) {
//                        mSearchPresenter.doSearch(keyword);
                        toSearch(keyword);
                    }
                }
                return false;
            }
        });

        mHistoryDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchPresenter != null) {
                    mSearchPresenter.deleteHistories();
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mSearchPresenter != null) {
                    mSearchPresenter.loadMore();
                }
            }
        });

        mSearchResultAdapter.setOnItemClickListener(this);
    }

    /**
     * 切换到历史和推荐关键词界面
     */
    private void back2HistoryPage() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
        mHistoryContainer.setVisibility(mHistoryFlowLayout.getContentSize() > 0 ? View.VISIBLE : View.GONE);
        mRecommendContainer.setVisibility(mRecommendFlowView.getContentSize() > 0 ? View.VISIBLE : View.GONE);

        // 搜索内容隐藏
        mRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    protected void loadData() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getRecommendWords();
            mSearchPresenter.getHistories();
        }
    }

    @Override
    public void onHistoriesLoaded(Histories histories) {
        LogUtils.d(this, "onHistoriesLoaded histories size==> " + histories);
        if (histories == null || histories.getHistories().isEmpty()) {
            mHistoryContainer.setVisibility(View.GONE);
        } else {
            mHistoryContainer.setVisibility(View.VISIBLE);
            mHistoryFlowLayout.setTextList(histories.getHistories());
            LogUtils.d(this, "mHistoryContainer is visible");
        }
    }

    @Override
    public void onHistoriesDeleted() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchContent result) {
        LogUtils.d(this, "search result ==> " + result);
        setUpState(State.SUCCESS);
        // 隐藏历史记录和推荐
        mHistoryContainer.setVisibility(View.GONE);
        mRecommendContainer.setVisibility(View.GONE);
        // 显示搜索界面
        mRefreshLayout.setVisibility(View.VISIBLE);
        //显示搜索结果
        mSearchResultAdapter.setDataList(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
    }

    @Override
    public void onMoreLoaded(SearchContent result) {
        mRefreshLayout.finishLoadmore();
        // 加载更多的结果
        // 拿到结果，并添加到适配器的尾部
        List<SearchContent.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO> dataList = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchResultAdapter.addData(dataList);
        ToastUtil.showToast("已为您加载" + dataList.size() + "个商品");
    }

    @Override
    public void onMoreLoadError() {
        mRefreshLayout.finishLoadmore();
        ToastUtil.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onMoreLoadEmpty() {
        mRefreshLayout.finishLoadmore();
        ToastUtil.showToast("没有更多商品");
    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataDTO> result) {
        LogUtils.d(this, "recommend size == >" + result.size());
        List<String> recommendKeywords = new ArrayList<>();
        for (SearchRecommend.DataDTO item : result) {
            recommendKeywords.add(item.getKeyword());
        }
        if (recommendKeywords.isEmpty()) {
            mRecommendContainer.setVisibility(View.GONE);
        } else {
            mRecommendContainer.setVisibility(View.VISIBLE);
            mRecommendFlowView.setTextList(recommendKeywords);
        }

    }

    @Override
    public void retry() {
        //重新加载
        if (mSearchPresenter != null) {
            mSearchPresenter.research();
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
        setUpState(State.EMPTY);
    }

    @Override
    public void onFlowTextItemClick(String text) {
        // 关键词textflow子view的点击事件
//        LogUtils.d(this, "onFlowTextItemClick " + text);
        toSearch(text);
    }

    private void toSearch(String text) {
        if (mSearchPresenter != null) {
            mSearchResultList.scrollToPosition(0);
            mSearchInputBox.setText(text);
            mSearchPresenter.doSearch(text);
            mSearchInputBox.setFocusable(true);
            mSearchInputBox.requestFocus();
            mSearchInputBox.setSelection(text.length(),text.length());
        }
    }

    @Override
    public void onListItemClick(IBaseInfo item) {
        TicketUtil.toTicketPage(getContext(), item);
    }
}
