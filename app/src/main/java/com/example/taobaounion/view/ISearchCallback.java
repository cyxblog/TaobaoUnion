package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.domain.SearchContent;
import com.example.taobaounion.model.domain.SearchRecommend;

import java.util.List;

public interface ISearchCallback extends IBaseCallback {

    /**
     * 搜索历史加载完成
     * @param histories
     */
    void onHistoriesLoaded(List<String> histories);

    /**
     * 历史记录删除完成
     */
    void onHistoriesDeleted();

    /**
     * 搜索成功
     */
    void onSearchSuccess(SearchContent result);

    /**
     * 加载更多内容
     * @param result
     */
    void onMoreLoaded(SearchContent result);

    /**
     * 更多内容加载失败
     */
    void onMoreLoadError();

    /**
     * 没有更多内容
     */
    void onMoreLoadEmpty();

    /**
     * 推荐词获取结果
     * @param recommendWords
     */
    void onRecommendWordsLoaded(List<SearchRecommend.DataDTO> result);
}
