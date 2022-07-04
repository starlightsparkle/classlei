package com.example.curriculumdesign.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.curriculumdesign.R;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.BaseResponse;
import com.example.curriculumdesign.entity.UserResponse;
import com.example.curriculumdesign.utils.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private Button mBtnLogin;//登录按钮
    private Button mBtnRegister;//注册按钮
    private EditText mETUserName;//监听用户名
    private EditText mETPassword;//监听密码


    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        //获取输入框内容
        mETUserName = findViewById(R.id.edt_userID);
        mETPassword = findViewById(R.id.edt_password_OK);
        //找到按钮
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnRegister = findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
//        navgateTo(HomeActivity.class);// 调试用
        //设置监听事件
        mETUserName.addTextChangedListener(new TextWatcher() {
            @Override //输入前
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override //输入中
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override //输入后
            public void afterTextChanged(Editable s) {

            }
        });
        mETPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //设置对应的点击事件
        //监听登录按钮
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 上面获取的输入框的文字对比数据库中的数据
                login(mETUserName.getText().toString().trim(),mETPassword.getText().toString().trim());
            }
        });

        //监听注册按钮
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navgateTo(RegisterActivity.class);

            }
        });
         String token=GetStringFromSP("Authorization");
//        Log.d("name", "initData: "+token);
         if(!StringUtils.IsEmpty(token))
         {
             navgateToWithFlag(HomeActivity.class,
                     Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
         }
    }
    private void login(String account, String pwd) {
        if (StringUtils.IsEmpty(account)) {
            ShowToast("请输入账号");
            return;
        }
        if (StringUtils.IsEmpty(pwd)) {
            ShowToast("请输入密码");
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", account);
        params.put("password", pwd);
        Api.config(ApiConfig.LOGIN, params).postRequest1(this,new CallBack() {
            @Override
            public void OnSuccess(final String res, Response response) {
//                Log.e("onSuccess", res);
                Gson gson = new Gson();
                BaseResponse loginResponse = gson.fromJson(res, BaseResponse.class);
                if (loginResponse.getCode() == 200) {

                    Headers headers=response.headers();
                    String token=headers.get("Authorization");
                    SaveToSP("Authorization",token);
                    getCurrentUser();
                    navgateToWithFlag(HomeActivity.class,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ShowToastAsyn("登录成功");
                } else {
                    ShowToastAsyn("登录失败");
                }
            }

            @Override
            public void OnFailure(Exception e) {

            }
        });
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
