package com.example.taobaounion.presenter;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.ISearchCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchCallback> {

    /**
     * 获取搜索历史
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void deleteHistories();

    /**
     * 发起搜索
     * @param keyword
     */
    void doSearch(String keyword);

    /**
     * 重新搜索
     */
    void research();

    /**
     * 获取更多搜索结果
     */
    void loadMore();

    /**
     * 获取推荐关键词
     */
    void getRecommendWords();
}
