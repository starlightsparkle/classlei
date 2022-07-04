package com.example.curriculumdesign.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.BaseResponse;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.Response;

public class NewClassActivity extends BaseActivity {
    private ImageView ivBackup;//返回图标
    private EditText edtClassName;//课程名称
    private EditText edtClassContent;//课程内容
    private Button btnCreateClass;//创建课程按钮


    @Override
    protected int initLayout() {
        return R.layout.activity_newclass;
    }

    @Override
    protected void initView() {
        ivBackup = findViewById(R.id.iv_back_up);
        edtClassName = findViewById(R.id.edt_courseName);
        edtClassContent = findViewById(R.id.edt_course_content);
        btnCreateClass = findViewById(R.id.btn_create_course);

    }

    @Override
    protected void initData() {
        ivBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                navgateTo(LoginActivity.class);
                finish();
            }
        });
        //获取课程名称
        edtClassName.addTextChangedListener(new TextWatcher() {
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
        //获取课程内容
        edtClassContent.addTextChangedListener(new TextWatcher() {
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

        btnCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String classContent=edtClassContent.getText().toString();
                 String className=edtClassName.getText().toString();
                 newclass(classContent,className);
//                finish();
//                navgateTo(LoginActivity.class);
            }
        });


    }
    void newclass(String classContent,String className)
    {
        Long userid=Long.parseLong(GetStringFromSP("userid"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("classContent", classContent);
        params.put("className", className);
        params.put("createId",userid);

        Api.config(ApiConfig.ADDCLASS, params).postRequest(this,new CallBack() {
            @Override
            public void OnSuccess(final String res, Response response) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(res, BaseResponse.class);
                if(baseResponse.getCode()==200)
                {

                    ShowToastAsyn("添加成功！");
                }
                else
                {
                    ShowToastAsyn(baseResponse.getMessage());
                }
            }

            @Override
            public void OnFailure(Exception e) {
                Log.e("onFailure", e.toString());
                ShowToastAsyn("连接服务器失败！");
            }
        });
    }
   


}