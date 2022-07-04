package com.example.curriculumdesign.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Date 2020-04-08 23:48
 * 功能：和账号相关
 */
public class AccountHelper {

    /**
     * 获取本地账号信息
     *
     * @param context
     * @param key
     * @return
     */
    public static String getLoginInfo(Context context, String key) {
        return context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE).getString(key, "");
    }

    /*
     * 保存账号信息
     * */
    public static void saveLoginInfo(Context context, String userName, String password, String role, String name) {
        SharedPreferences.Editor editor = context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE).edit();
        editor.putString("userName", userName);
        editor.putString("password", password);
        editor.putString("name", name);
        editor.putString("role", role);
        editor.apply();
    }

    /*
     * 返回百度API所需要的id、key和secret_key
     * */
    public static String getAPP_ID() {
        return "19243618";
    }

    public static String getAPI_KEY() {
        return "vPobfsabKM41pMfGFDqBLUSi";
    }

    public static String getSECRET_KEY() {
        return "CS35tqGcMvCzpouDvGELKYrgIs6X7F0A";
    }

}
