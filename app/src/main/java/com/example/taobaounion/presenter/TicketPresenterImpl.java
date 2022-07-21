package com.example.taobaounion.presenter;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.TicketParams;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ITicketCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterImpl implements ITicketPresenter {


    private ITicketCallback mViewCallback;
    private String mCover;
    private TicketResult mTicketResult;

    enum LoadState {
        LOADING, SUCCESS, ERROR, NONE
    }

    private LoadState mCurrentState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        onTicketLoading();
        mCover = cover;
        // 获取淘口令
        LogUtils.d(TicketPresenterImpl.this, "title..." + title);
        LogUtils.d(TicketPresenterImpl.this, "url..." + url);
        LogUtils.d(TicketPresenterImpl.this, "cover..." + cover);

        String targetUrl = UrlUtils.getTicketUrl(url);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(targetUrl, title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                LogUtils.d(TicketPresenterImpl.this, "result code ..." + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    mTicketResult = response.body();
                    LogUtils.d(TicketPresenterImpl.this, "result " + mTicketResult);
                    onTicketLoadSuccess();
                } else {
                    LogUtils.i(TicketPresenterImpl.this, "请求失败");
                    onLoadedTicketError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                LogUtils.i(TicketPresenterImpl.this, "请求失败");
                onLoadedTicketError();
            }
        });
    }

    private void onTicketLoadSuccess() {
        if (mViewCallback != null) {
            mViewCallback.onTicketLoaded(mCover, mTicketResult);
        } else {
            mCurrentState = LoadState.SUCCESS;
        }
    }

    private void onLoadedTicketError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        } else {
            mCurrentState = LoadState.ERROR;
        }
    }

    @Override
    public void registerCallback(ITicketCallback callback) {
        if (mCurrentState != LoadState.NONE) {
            if (mCurrentState == LoadState.SUCCESS) {
                onTicketLoadSuccess();
            } else if (mCurrentState == LoadState.ERROR) {
                onLoadedTicketError();
            } else if (mCurrentState == LoadState.LOADING) {
                onTicketLoading();
            }
        }
        mViewCallback = callback;
    }

    @Override
    public void unregisterCallback(ITicketCallback callback) {
        mViewCallback = null;
        callback = null;
    }

    private void onTicketLoading() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        } else {
            mCurrentState = LoadState.LOADING;
        }
    }
}
