package com.example.taobaounion.presenter;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer, Integer> pagesInfo = new HashMap<>();

    private List<ICategoryPagerCallback> mCallbacks = new ArrayList<>();

    private final int DEFAULT_PAGE = 1;
    private Integer mCurrentPage;


    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
        // 加载内容

        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, targetPage);
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagerPresenterImpl.this, "onResponse code ..." + code);
                if (code== HttpURLConnection.HTTP_OK) {
                    HomePagerContent pageContent = response.body();
                    LogUtils.d(CategoryPagerPresenterImpl.this, "pageContent..." + pageContent);
                    // 把数据给到UI
                    handleHomePagerContentResult(pageContent, categoryId);
                }else{
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(CategoryPagerPresenterImpl.this, "onFailure ..." + t);
                handleNetworkError(categoryId);
            }
        });
    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        LogUtils.d(this, "home page url.." + homePagerUrl);
        Call<HomePagerContent> task = api.getHomePagerContent(homePagerUrl);
        return task;
    }

    private void handleNetworkError(int categoryId) {
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }
    }

    private void handleHomePagerContentResult(HomePagerContent pageContent, int categoryId) {
        List<HomePagerContent.DataDTO> data = pageContent.getData();
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (data.isEmpty()) {
                    callback.onEmpty();
                }else{
                    List<HomePagerContent.DataDTO> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void loadMore(int categoryId) {
        //加载数据
        mCurrentPage = pagesInfo.get(categoryId);
        if (mCurrentPage == null) {
            mCurrentPage = 1;
        }
        mCurrentPage++;
        Call<HomePagerContent> task = createTask(categoryId, mCurrentPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                // 结果
                int code = response.code();
                LogUtils.d(CategoryPagerPresenterImpl.this, "response code ..." + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pagerContent = response.body();
                    handleLoadMoreSuccessResult(pagerContent, categoryId);
                }else{
                    handleLoadMoreError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(CategoryPagerPresenterImpl.this, t.toString());
                handleLoadMoreError(categoryId);
            }
        });
    }

    /**
     * 处理加载更多成功的结果
     */
    private void handleLoadMoreSuccessResult(HomePagerContent pagerContent, int categoryId) {
        pagesInfo.put(categoryId, mCurrentPage);
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pagerContent == null || pagerContent.getData().isEmpty()) {
                    callback.onLoadMoreEmpty();
                }else{
                    callback.onLoadMoreLoaded(pagerContent.getData());
                }
            }
        }
    }

    /**
     * 处理加载失败的结果
     * @param categoryId
     */
    private void handleLoadMoreError(int categoryId) {
        mCurrentPage--;
        pagesInfo.put(categoryId, mCurrentPage);
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoadMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryId) {

    }

    @Override
    public void registerCallback(ICategoryPagerCallback iCategoryPagerCallback) {
        if (!mCallbacks.contains(iCategoryPagerCallback)) {
            mCallbacks.add(iCategoryPagerCallback);
        }
    }

    @Override
    public void unregisterCallback(ICategoryPagerCallback iCategoryPagerCallback) {
        mCallbacks.remove(iCategoryPagerCallback);
    }
}
