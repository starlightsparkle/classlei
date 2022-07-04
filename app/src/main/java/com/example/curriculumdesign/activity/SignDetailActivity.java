package com.example.curriculumdesign.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.adapter.HomeAdapter;
import com.example.curriculumdesign.adapter.StuAdapter;
import com.example.curriculumdesign.adapter.StuSignAdapter;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.BaseResponse;
import com.example.curriculumdesign.entity.SignEntity;
import com.example.curriculumdesign.fragment.ClassFragment;
import com.example.curriculumdesign.fragment.SignStuFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Response;

public class SignDetailActivity extends BaseActivity {

    private SignEntity sign;
    private ImageView btnBack;
    private Button stop_btn;
    private TextView sign_detail_title;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles={"已签到学生","未签到学生"};
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private ImageView btnCode;
    private ImageView btnGps;



    @Override
    protected int initLayout() {
        return R.layout.activity_sign_detail;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void initView() {
        sign = (SignEntity)getIntent().getExtras().getSerializable("sign");
        sign_detail_title=findViewById(R.id.sign_detail_title);
        btnBack=findViewById(R.id.btn_back_class);
        btnCode=findViewById(R.id.signToQrCode);
        btnGps=findViewById(R.id.signToGPS);
        viewPager=findViewById(R.id.SignFixedViewpager);
        slidingTabLayout=findViewById(R.id.slidingSignTabLayout);
        stop_btn=findViewById(R.id.stop_btn);
        if (sign.getStatus()==0) {stop_btn.setEnabled(true);
        stop_btn.setBackground(getResources().getDrawable(R.color.Gray_red));
        }
        else {
            stop_btn.setOnClickListener((v -> {
                stopSign();
            }));
        }

        if (sign.getSignType()==0){
            btnCode.setVisibility(View.VISIBLE);
            btnGps.setVisibility(View.GONE);
            btnCode.setOnClickListener((v -> { //code
                Bundle bundle = new Bundle();
                bundle.putString("signid",sign.getClassSignId().toString());
                navigateToWithBundle(CodeActivity.class,bundle);
            }));
        }
        else{
            btnGps.setVisibility(View.VISIBLE);
            btnCode.setVisibility(View.GONE);
            btnGps.setOnClickListener((v -> {
                Bundle bundle = new Bundle();
                bundle.putString("classSignId",sign.getClassSignId().toString());
                navigateToWithBundle(ChangeMapActivity.class,bundle);
            }));
        }

    }

    private void stopSign() {
        HashMap<String,Object> params = new HashMap<>();
        params.put("classSignId",sign.getClassSignId());
        Api.config(ApiConfig.ENDSIGN,params).deleteRequest(mContext, new CallBack() {
            @Override
            public void OnSuccess(String res, Response response) {
                BaseResponse Response = gson.fromJson(res, BaseResponse.class);
                Log.d("TAG", "OnSuccess: +"+res);
                if (Response.getCode() == 200) {
                   ShowToastAsyn("成功停止");
                   runOnUiThread(()->{
                       stop_btn.setEnabled(false);
                       stop_btn.setBackground(getResources().getDrawable(R.color.Gray_red));
                   });
                }
                else {
                    runOnUiThread(()->{
                        stop_btn.setEnabled(false);
                        stop_btn.setBackground(getResources().getDrawable(R.color.Gray_red));
                    });
                    ShowToastAsyn(Response.getMessage());

                }

            }

            @Override
            public void OnFailure(Exception e) {
                flush(stop_btn);
            }
        });
    }
    public void flush(View view) {
        finish();
        Intent intent = new Intent(this,SignDetailActivity.class);
        startActivity(intent);
    }


    @Override
    protected void initData() {
        btnBack.setOnClickListener((v -> {finish();}));
        sign_detail_title.setText(sign.getSignName());

        initTab();
    }

    /**
     * 初始化导航条
     */
    private void initTab(){
        int i=0;
        for (String title: mTitles) {
            mFragments.add(SignStuFragment.newInstance(i,sign));
            i++;
        }
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new StuAdapter(getSupportFragmentManager(),mTitles,mFragments));
        slidingTabLayout.setViewPager(viewPager);
    }

}