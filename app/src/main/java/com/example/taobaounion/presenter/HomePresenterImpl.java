package com.example.taobaounion.presenter;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {

    private static final String TAG = "HomePresenterImpl";
    private IHomeCallback mCallback;

    @Override
    public void getCategories() {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        // 加载分类数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();

        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                // 数据结果
                int code = response.code();
                LogUtils.d(HomePresenterImpl.this, "result code is " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    // 请求成功
                    Categories categories = response.body();
                    if (mCallback != null) {
                        if (categories == null || categories.getData().isEmpty()) {
                            mCallback.onEmpty();
                        } else {
                            mCallback.onCategoriesLoaded(categories);
                        }
                    }
                } else {
                    // 请求失败
                    LogUtils.i(HomePresenterImpl.this, "请求失败");
                    if (mCallback != null) {
                        mCallback.onError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                // 加载失败的结果
                LogUtils.e(HomePresenterImpl.this, "请求错误..." + t);
                if (mCallback != null) {
                    mCallback.onError();
                }
            }
        });
    }

    @Override
    public void registerCallback(IHomeCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IHomeCallback callback) {
        mCallback = null;
    }
}
