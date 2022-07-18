package com.example.taobaounion.presenter;

import android.system.StructStatVfs;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.RecommendCategories;
import com.example.taobaounion.model.domain.RecommendContent;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.IRecommendCallback;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecommendPresenterImpl implements IRecommendPresenter{

    private final Api mApi;

    public RecommendPresenterImpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }


    private IRecommendCallback mViewCallback;

    @Override
    public void getRecommendCategories() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        // 加载分类数据
        Call<RecommendCategories> task = mApi.getRecommendCategories();
        task.enqueue(new Callback<RecommendCategories>() {
            @Override
            public void onResponse(Call<RecommendCategories> call, Response<RecommendCategories> response) {
                int code = response.code();
                LogUtils.d(RecommendPresenterImpl.this, "response code == >" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    RecommendCategories recommendCategories = response.body();
                    if (recommendCategories != null) {
                        LogUtils.d(RecommendPresenterImpl.this, "recommendCategories == > " + recommendCategories.toString());
                        handleRecommendSuccessResult(recommendCategories);
                    }

                }else{
                    LogUtils.i(RecommendPresenterImpl.this, "getRecommendCategories error...");
                    handleRecommendErrorResult();
                }
            }

            @Override
            public void onFailure(Call<RecommendCategories> call, Throwable t) {
                LogUtils.i(RecommendPresenterImpl.this, "getRecommendCategories onFailure..." + t);
                handleRecommendErrorResult();
            }
        });
    }

    @Override
    public void getContentByCategory(RecommendCategories.DataDTO item) {
        String targetUrl = UrlUtils.getRecommendContentUrl(item.getFavorites_id());
        LogUtils.d(RecommendPresenterImpl.this, "getContentByCategory..." + targetUrl);
        Call<RecommendContent> task = mApi.getRecommendContent(targetUrl);
        task.enqueue(new Callback<RecommendContent>() {
            @Override
            public void onResponse(Call<RecommendContent> call, Response<RecommendContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    RecommendContent recommendContent = response.body();
                    handleContentSuccessResult(recommendContent);
                }else{
                    LogUtils.i(RecommendPresenterImpl.this, "getContentByCategory response error code == " + code);
                    handleContentErrorResult();
                }
            }

            @Override
            public void onFailure(Call<RecommendContent> call, Throwable t) {
                LogUtils.i(RecommendPresenterImpl.this, "response failure...");
                handleContentErrorResult();
            }
        });
    }

    private void handleContentErrorResult() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    private void handleContentSuccessResult(RecommendContent recommendContent) {
        if (mViewCallback != null) {
            mViewCallback.onContentLoaded(recommendContent);
        }
    }

    @Override
    public void reloadContent() {
        getRecommendCategories();
    }

    private void handleRecommendErrorResult() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    private void handleRecommendSuccessResult(RecommendCategories recommendCategories) {
        if (mViewCallback != null ) {
            List<RecommendCategories.DataDTO> data = recommendCategories.getData();
            if (data != null && !data.isEmpty()) {
                mViewCallback.onRecommendCategoriesLoaded(data);
            }else{
                mViewCallback.onEmpty();
            }
        }
    }

    @Override
    public void registerCallback(IRecommendCallback callback) {
        mViewCallback = callback;
    }

    @Override
    public void unregisterCallback(IRecommendCallback callback) {
        if (mViewCallback != null) {
            mViewCallback = null;
        }
    }
}
