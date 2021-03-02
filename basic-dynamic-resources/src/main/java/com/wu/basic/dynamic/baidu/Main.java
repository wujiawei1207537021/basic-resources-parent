package com.wu.basic.dynamic.baidu;


import com.wu.basic.dynamic.baidu.com.baidu.translate.demo.TransApi;

public class Main {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20210302000713203";
    private static final String SECURITY_KEY = "hKDKIQwN0vQHkL_BUY5X";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "高度600米";
        System.out.println(api.getTransResult(query, "auto", "en"));
    }

}
