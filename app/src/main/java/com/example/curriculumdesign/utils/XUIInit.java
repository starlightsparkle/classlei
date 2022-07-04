package com.example.curriculumdesign.utils;

import android.app.Application;

import com.xuexiang.xui.XUI;

/**
 * 不可删除，用于XUI的初始化
 */
public class XUIInit extends Application{

    @Override
    public void onCreate(){
        super.onCreate();

        XUI.init(this); //初始化UI框架
        XUI.debug(true);//开启UI框架测试日志
    }
}