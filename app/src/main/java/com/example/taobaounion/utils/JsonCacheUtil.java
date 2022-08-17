package com.example.taobaounion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.taobaounion.base.BaseApplication;
import com.example.taobaounion.model.domain.CacheWithDuration;
import com.google.gson.Gson;

public class JsonCacheUtil {

    public static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";

    private final SharedPreferences mSharedPreferences;
    private final Gson mGson;

    private JsonCacheUtil() {
        mSharedPreferences = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public void saveCache(String key, Object value) {
        saveCache(key, value, -1L);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String valueStr = mGson.toJson(value);
        if (duration != -1L) {
            duration += System.currentTimeMillis();
        }
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration, valueStr);
        String cacheWithTime = mGson.toJson(cacheWithDuration);
        edit.putString(key, cacheWithTime);
        edit.apply();
    }

    public void delCache(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String valueWithDuration = mSharedPreferences.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = mGson.fromJson(valueWithDuration, CacheWithDuration.class);
        long duration = cacheWithDuration.getDuration();
        if (duration != -1L && duration - System.currentTimeMillis() <= 0) {
            // 过期了
            return null;
        } else {
            // 没过期
            String cache = cacheWithDuration.getCache();
            LogUtils.d(JsonCacheUtil.this, cache);
            return mGson.fromJson(cache, clazz);
        }

    }

    private static volatile JsonCacheUtil sInstance;

    public static JsonCacheUtil getInstance() {
        if (sInstance == null) {
            synchronized (JsonCacheUtil.class) {
                if (sInstance == null) {
                    sInstance = new JsonCacheUtil();
                }
            }
        }
        return sInstance;
    }
}
