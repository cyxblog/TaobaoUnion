package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {

    /**
     * 数据加载回来
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataDTO> contents);

    /**
     * 获取categoryId
     */
    int getCategoryId();


    /**
     * 加载更多网络错误
     *
     */
    void onLoadMoreError();

    /**
     * 没有更多内容
     *
     */
    void onLoadMoreEmpty();

    /**
     * 加载出更多
     * @param contents
     */
    void onLoadMoreLoaded(List<HomePagerContent.DataDTO> contents);

    /**
     * 加载轮播图数据
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataDTO> contents);
}
