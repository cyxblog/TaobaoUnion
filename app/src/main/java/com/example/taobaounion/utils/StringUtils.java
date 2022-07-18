package com.example.taobaounion.utils;

public class StringUtils {

    public static String getTicketCode(String ticketModel){
        String[] codeArr = ticketModel.split("￥");
        return "￥" + codeArr[1] + "￥";
    }
}
