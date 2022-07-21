package com.example.taobaounion.utils;

import com.example.taobaounion.presenter.CategoryPagerPresenterImpl;
import com.example.taobaounion.presenter.HomePresenterImpl;
import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.presenter.IOnSellPresenter;
import com.example.taobaounion.presenter.IRecommendPresenter;
import com.example.taobaounion.presenter.ISearchPresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.presenter.OnSellPresenterImpl;
import com.example.taobaounion.presenter.RecommendPresenterImpl;
import com.example.taobaounion.presenter.SearchPresenterImpl;
import com.example.taobaounion.presenter.TicketPresenterImpl;

public class PresenterManager {

    private static final PresenterManager sInstance = new PresenterManager();

    public ISearchPresenter getSearchPresenter() {
        return mSearchPresenter;
    }

    private final ISearchPresenter mSearchPresenter;

    public IOnSellPresenter getOnSellPresenter() {
        return mOnSellPresenter;
    }

    private final IOnSellPresenter mOnSellPresenter;

    public IRecommendPresenter getRecommendPresenter() {
        return mRecommendPresenter;
    }

    private final IRecommendPresenter mRecommendPresenter;

    public ITicketPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    private final ITicketPresenter mTicketPresenter;

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    private final IHomePresenter mHomePresenter;

    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return mCategoryPagerPresenter;
    }

    private final ICategoryPagerPresenter mCategoryPagerPresenter;

    public static PresenterManager getInstance(){
        return sInstance;
    }

    private PresenterManager(){
        mCategoryPagerPresenter = new CategoryPagerPresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
        mRecommendPresenter = new RecommendPresenterImpl();
        mOnSellPresenter = new OnSellPresenterImpl();
        mSearchPresenter = new SearchPresenterImpl();
    }
}
