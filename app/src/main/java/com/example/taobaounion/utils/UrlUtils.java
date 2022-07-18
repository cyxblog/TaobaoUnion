package com.example.taobaounion.utils;

public class UrlUtils {

    public static String createHomePagerUrl(int materialId, int page) {
        return "discovery/" + materialId + "/" + page;
    }

    public static String getCoverPath(String goodsCoverUrl, int size) {
        if (goodsCoverUrl.startsWith("http") || goodsCoverUrl.startsWith("https")) {
            return goodsCoverUrl + "_" + size + "x" + size + ".jpg";
        } else {
            return "https:" + goodsCoverUrl + "_" + size + "x" + size + ".jpg";
        }
    }

    public static String getTicketUrl(String url) {
        if (url.startsWith("http") || url.startsWith("https")) {
            return url;
        } else {
            return "https:" + url;
        }
    }

    public static String getRecommendContentUrl(Integer favorites_id) {
        return "recommend/" + favorites_id;
    }
}
