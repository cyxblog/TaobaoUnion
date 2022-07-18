package com.example.taobaounion.base;

import com.example.taobaounion.view.IHomeCallback;

public interface IBasePresenter<T> {

    /**
     * 注册UI更新接口
     * @param callback
     */
    void registerCallback(T callback);

    /**
     * 注销UI更新接口
     * @param callback
     */
    void unregisterCallback(T callback);
}
