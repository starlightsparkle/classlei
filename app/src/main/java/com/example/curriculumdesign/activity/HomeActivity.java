package com.example.curriculumdesign.activity;


import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.adapter.MyPagerAdapter;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.BaseResponse;
import com.example.curriculumdesign.entity.Gps;
import com.example.curriculumdesign.entity.TabEntity;
import com.example.curriculumdesign.entity.UserResponse;
import com.example.curriculumdesign.fragment.HomeFragment;
import com.example.curriculumdesign.fragment.MessageFragment;
import com.example.curriculumdesign.fragment.MyFragment;
import com.example.curriculumdesign.service.NotificationService;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Response;

public class HomeActivity extends BaseActivity {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ViewPager viewPager;
    private CommonTabLayout commonTabLayout;
    //导航栏标题
    private String[] mTitles = {"首页", "消息", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.home_unselect, R.mipmap.collect_unselect,
            R.mipmap.my_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.home_selected, R.mipmap.collect_selected,
            R.mipmap.my_selected
    };
    //导航栏 end

    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        //navgateTo(SigninActivity.class);
//        getCurrentUser();
        viewPager = findViewById(R.id.viewpager);
        commonTabLayout = findViewById(R.id.commonTabLayout);
    }

    @Override
    protected void initData() {
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(MessageFragment.newInstance());
        mFragments.add(MyFragment.newInstance());
        //绑定
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        viewPager.setOffscreenPageLimit(mFragments.size());
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                //切换
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                commonTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),mTitles,mFragments));
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent); //启动监听消息推送的服务

    }
    void getCurrentUser()
    {
        HashMap<String, Object> params = new HashMap<>();
        Api.config(ApiConfig.CURRENT, params).getRequest(mContext, new CallBack() {
            @Override
            public void OnSuccess(final String res, Response response) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                UserResponse userResponse = gson.fromJson(res, UserResponse.class);
//                ShowToastAsyn(userResponse.toString());
                if(userResponse.getCode()==200)
                {
                    setUserToSP(userResponse.getResult());
                    SaveToSP("username",userResponse.getResult().getUsername());
                    SaveToSP("userid",String.valueOf(userResponse.getResult().getUserId()));
                    SaveToSP("roleid",String.valueOf(userResponse.getResult().getRoleId()));
                    SaveToSP("avatarurl",String.valueOf(userResponse.getResult().getAvatarUrl()));
                }
                else
                {
                    ShowToastAsyn("获取用户数据失败！，请重新登陆！");
                }

            }

            @Override
            public void OnFailure(Exception e) {
//                Log.e("onFailure", e.getMessage());
                ShowToastAsyn("连接服务器失败！");
            }
        });
    }
}