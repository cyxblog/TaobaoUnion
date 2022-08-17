package com.example.taobaounion.presenter;

import android.text.TextUtils;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.SearchContent;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.utils.JsonCacheUtil;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.view.ISearchCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenterImpl implements ISearchPresenter {

    private final Api mApi;
    private ISearchCallback mCallback;

    public static final int DEFAULT_PAGE = 0;

    private int mCurrentPage = DEFAULT_PAGE;
    private String mCurrentKeyword;
    private final JsonCacheUtil mJsonCacheUtil;

    public SearchPresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
    }


    private static final String KEY_HISTORY = "key_history";

    private static final int DEFAULT_HISTORIES_SIZE = 10;
    private int mHistoriesMaxSize = DEFAULT_HISTORIES_SIZE;

    /**
     * 添加历史记录
     *
     * @param history
     */
    private void saveHistory(String history) {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORY, Histories.class);
        List<String> historiesList = null;
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        // 去重完成
        // 处理没有数据的情况
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }

        if (histories == null) {
            histories = new Histories();
        }

        histories.setHistories(historiesList);

        // 对个数进行设置
        if (historiesList.size() > mHistoriesMaxSize) {
            historiesList = historiesList.subList(0, mHistoriesMaxSize);
        }

        // 添加记录
        historiesList.add(history);
        // 保存记录
        mJsonCacheUtil.saveCache(KEY_HISTORY, histories);

    }

    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORY, Histories.class);
        if (mCallback != null) {
            mCallback.onHistoriesLoaded(histories);
        }
    }

    @Override
    public void deleteHistories() {
        mJsonCacheUtil.delCache(KEY_HISTORY);
        if (mCallback != null) {
            mCallback.onHistoriesDeleted();
        }
    }

    @Override
    public void doSearch(String keyword) {
        if (mCurrentKeyword == null || !mCurrentKeyword.equals(keyword)) {
            saveHistory(keyword);
            mCurrentKeyword = keyword;
        }
        if (mCallback != null) {
            mCallback.onLoading();
        }
        Call<SearchContent> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchContent>() {
            @Override
            public void onResponse(Call<SearchContent> call, Response<SearchContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchContent searchContent = response.body();
                    if (searchContent != null) {
                        LogUtils.d(SearchPresenterImpl.this, "search response ==> " + searchContent);
                        handleSearchSuccess(searchContent);
                    }
                } else {
                    LogUtils.i(SearchPresenterImpl.this, "handleSearchRecommendError code == " + code);
                    handleSearchError();
                }
            }

            @Override
            public void onFailure(Call<SearchContent> call, Throwable t) {
                LogUtils.i(SearchPresenterImpl.this, "handleSearchRecommendError  == " + t);
                handleSearchError();
            }
        });
    }

    private void handleSearchError() {
        if (mCallback != null) {
            mCallback.onError();
        }
    }

    private void handleSearchSuccess(SearchContent searchContent) {
        if (mCallback != null) {
            try {
                List<SearchContent.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO> dataList = searchContent.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
                if (dataList.isEmpty()) {
                    mCallback.onEmpty();
                } else {
                    mCallback.onSearchSuccess(searchContent);
                }
            } catch (Exception e) {
                LogUtils.e(SearchPresenterImpl.this, "data is null ==> " + e);
                mCallback.onEmpty();
            }
        }
    }

    @Override
    public void research() {
        if (!TextUtils.isEmpty(mCurrentKeyword)) {
            doSearch(mCurrentKeyword);
        } else {
            if (mCallback != null) {
                mCallback.onEmpty();
            }
        }
    }

    @Override
    public void loadMore() {
        mCurrentPage++;
        if (!TextUtils.isEmpty(mCurrentKeyword)) {
            doSearchMore();
        } else {
            if (mCallback != null) {
                mCallback.onMoreLoadEmpty();
            }
        }
    }

    private void doSearchMore() {
        Call<SearchContent> task = mApi.doSearch(mCurrentPage, mCurrentKeyword);
        task.enqueue(new Callback<SearchContent>() {
            @Override
            public void onResponse(Call<SearchContent> call, Response<SearchContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchContent searchContent = response.body();
                    if (searchContent != null) {
                        LogUtils.d(SearchPresenterImpl.this, "search response ==> " + searchContent);
                        handleSearchMoreSuccess(searchContent);
                    }
                } else {
                    LogUtils.i(SearchPresenterImpl.this, "handleSearchRecommendError code == " + code);
                    handleSearchMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchContent> call, Throwable t) {
                LogUtils.i(SearchPresenterImpl.this, "handleSearchRecommendError  == " + t);
                handleSearchMoreError();
            }
        });
    }

    private void handleSearchMoreSuccess(SearchContent result) {
        if (mCallback != null) {
            try {
                List<SearchContent.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO> dataList = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
                if (dataList.isEmpty()) {
                    mCallback.onMoreLoadEmpty();
                    mCurrentPage--;
                } else {
                    mCallback.onMoreLoaded(result);
                }
            } catch (Exception e) {
                LogUtils.e(SearchPresenterImpl.this, "load more data is null ==> " + e);
                mCallback.onMoreLoadEmpty();
                mCurrentPage--;
            }
        }
    }

    private void handleSearchMoreError() {
        mCurrentPage--;
        if (mCallback != null) {
            mCallback.onMoreLoadError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getSearchRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchRecommend searchRecommend = response.body();
                    if (searchRecommend != null) {
                        LogUtils.d(SearchPresenterImpl.this, "search response ==> " + searchRecommend);
                        handleSearchRecommendSuccess(searchRecommend);
                    }
                } else {
                    LogUtils.i(SearchPresenterImpl.this, "handleSearchRecommendError code == " + code);
                    handleSearchRecommendError();
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                LogUtils.e(SearchPresenterImpl.this, "handleSearchRecommendError == > " + t);
                handleSearchRecommendError();
            }
        });
    }

    private void handleSearchRecommendError() {
        if (mCallback != null) {
            mCallback.onError();
        }
    }

    private void handleSearchRecommendSuccess(SearchRecommend searchRecommend) {
        if (mCallback != null) {
            List<SearchRecommend.DataDTO> data = searchRecommend.getData();
            if (!data.isEmpty()) {
                mCallback.onRecommendWordsLoaded(data);
            }
        }
    }

    @Override
    public void registerCallback(ISearchCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ISearchCallback callback) {
        mCallback = null;
        callback = null;
    }
}
