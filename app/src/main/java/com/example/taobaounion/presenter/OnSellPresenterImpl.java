package com.example.taobaounion.presenter;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.IOnSellCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnSellPresenterImpl implements IOnSellPresenter {

    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private IOnSellCallback mCallback;
    private final Api mApi;

    private boolean mIsLoading = false;

    public OnSellPresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getOnSellContent() {

        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        if (mCallback != null) {
            mCallback.onLoading();
        }

        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        LogUtils.d(OnSellPresenterImpl.this, "getOnSellContent ==>" + targetUrl);
        Call<OnSellContent> task = mApi.getOnSellContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent onSellContent = response.body();
                    if (onSellContent != null) {
                        LogUtils.d(OnSellPresenterImpl.this, "onSellContent == >" + onSellContent);
                        handleContentLoadedSuccess(onSellContent);
                    }
                } else {
                    LogUtils.d(OnSellPresenterImpl.this, " onSellContent failure code == >" + code);
                    handleContentLoadedError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                LogUtils.d(OnSellPresenterImpl.this, " onSellContent onFailure " + t);
                handleContentLoadedError();
            }
        });
    }

    private void handleContentLoadedError() {
        mIsLoading = false;
        if (mCallback != null) {
            mCallback.onError();
        }
    }

    private void handleContentLoadedSuccess(OnSellContent result) {
        if (mCallback != null) {
            try {
                int size = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                if (size == 0) {
                    mCallback.onEmpty();
                } else {
                    mCallback.onContentLoaded(result);
                }
            } catch (NullPointerException e) {
                LogUtils.e(OnSellPresenterImpl.this, "数据为空");
                mCallback.onEmpty();
            }
        }
    }

    @Override
    public void reload() {
        // 重新加载
        getOnSellContent();
    }

    @Override
    public void loadMore() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        // 加载更多 todo
        mCurrentPage++;
        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent onSellContent = response.body();
                    if (onSellContent != null) {
                        LogUtils.d(OnSellPresenterImpl.this, "onSellContent == >" + onSellContent);
                        handleContentLoadMoreSuccess(onSellContent);
                    }
                } else {
                    LogUtils.d(OnSellPresenterImpl.this, " onSellContent failure code == >" + code);
                    handleContentLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                LogUtils.d(OnSellPresenterImpl.this, " onSellContent onFailure ");
                handleContentLoadMoreError();
            }
        });
    }

    /**
     * 加载更多内容成功
     *
     * @param result
     */
    private void handleContentLoadMoreSuccess(OnSellContent result) {
        if (mCallback != null) {
            try {
                int size = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                if (size == 0) {
                    mCurrentPage--;
                    mCallback.onMoreLoadEmpty();
                } else {
                    mCallback.onMoreLoaded(result);
                }
            } catch (NullPointerException e) {
                LogUtils.e(OnSellPresenterImpl.this, "数据为空");
                mCallback.onMoreLoadEmpty();
            }
        }
    }

    /**
     * 加载更多内容失败
     */
    private void handleContentLoadMoreError() {
        mIsLoading = false;
        mCurrentPage--;
        if (mCallback != null) {
            mCallback.onMoreLoadError();
        }
    }

    @Override
    public void registerCallback(IOnSellCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IOnSellCallback callback) {
        callback = null;
        mCallback = null;
    }
}
