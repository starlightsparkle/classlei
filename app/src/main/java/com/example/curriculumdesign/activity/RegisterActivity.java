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
import com.example.curriculumdesign.utils.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.Response;

public class RegisterActivity extends BaseActivity {
    private EditText mETUserName;//创建新的用户真实姓名
    private EditText mETNewUserName;//监听新建账号
    private EditText mETPassword;//监听新的密码
    private EditText mETPasswordAck;//监听确认新的密码

    private Button mBtnRegesterCheck;


    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        //获取输入框内容
//        mETUserName = findViewById(R.id.edt_name);
        mETNewUserName = findViewById(R.id.edt_ID);
        mETPassword = findViewById(R.id.edt_password);
        mETPasswordAck = findViewById(R.id.edt_new_password);

        //监听按钮事件
        mBtnRegesterCheck = findViewById(R.id.btn_register_check);
    }

    @Override
    protected void initData() {

        mETNewUserName.addTextChangedListener(new TextWatcher() {
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
        mETPasswordAck.addTextChangedListener(new TextWatcher() {
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
        mBtnRegesterCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*****************************************
                 TODO 根据获取的内容判断注册是否成功，加入数据库
                 如果执行成功执行下面的语句，跳转到登录界面进行登录
                 ******************************************/
                register(mETNewUserName.getText().toString().trim(),mETPassword.getText().toString().trim());
            }
        });



    }
    private void register(String account, String pwd) {
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
        params.put("avatarUrl","111");
        params.put("phone","111111");
        params.put("roleId",1);
        Api.config(ApiConfig.REGISTER, params).postRequest1(this,new CallBack() {
            @Override
            public void OnSuccess(final String res, Response response) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                BaseResponse registerResponse = gson.fromJson(res, BaseResponse.class);
                if(registerResponse.getCode()==200)
                {
                    navgateToWithFlag(LoginActivity.class,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ShowToastAsyn("注册成功！");
                }
                else
                {
                    ShowToastAsyn("注册失败！");
                }
            }

            @Override
            public void OnFailure(Exception e) {
                Log.e("onFailure", e.toString());
            }
        });
    }
}
