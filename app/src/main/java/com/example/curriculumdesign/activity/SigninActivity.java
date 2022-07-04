package com.example.curriculumdesign.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.BaseResponse;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.Response;

public class SigninActivity extends BaseActivity {

    private RadioGroup style;
    private EditText time;
    private Button submit;
    private int selection=0;//默认是二维码签到
    private String classId;
    private ImageView back;
    @Override
    protected int initLayout() {
        return R.layout.activity_signin;
    }

    @Override
    protected void initView() {
        back=findViewById(R.id.iv_signin_back_up);
        style = findViewById(R.id.radio);
        time = findViewById(R.id.edt_time);
        submit = findViewById(R.id.btn_send_signin);
        Intent intent=getIntent();
        classId=intent.getStringExtra("classId");
        //classId="30";
    }

    @Override
    protected void initData() {
        back.setOnClickListener((v -> {
            finish();
        }));
        time.addTextChangedListener(new TextWatcher() {
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


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test",String.valueOf(selection));
                Log.e("classid",classId);
                if(selection==0)
                {
                    startSign(Long.parseLong(classId),null,selection,Long.parseLong(time.getText().toString()));
                    finish();
                }
                else
                {
                    Intent intent=new Intent(mContext,GpsActivity.class);
                    intent.putExtra("type","sign");
                    intent.putExtra("classId",classId);
                    intent.putExtra("time",time.getText().toString());
                    startActivity(intent);
                }
            }
        });

        style.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_qrcode)
                {
                    Log.d("radio", "onRadioButtonClicked: 二维码");
                    selection=0;
                }
                else
                {
                    Log.d("gps", "onRadioButtonClicked: gps");
                    selection=1;
                }
            }
        });
    }


//    private int getWhichButton(){
//        RadioButton radioButton = style.getCheckedRadioButtonId();
//        if (radioButton.getText().equals("二维码")){
//            return 1;
//        }
//        else {
//            return 2;
//        }
//    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_qrcode:
                if (checked)
                    Log.d("radio", "onRadioButtonClicked: 二维码");
                    selection=0;
                    break;
            case R.id.radio_GPS:
                if (checked)
                    // Ninjas rule
                    selection=1;
                    break;
        }
    }
    void startSign(Long classId,String locationXy,int signType,Long time)
    {
        Long userid=Long.parseLong(GetStringFromSP("userid"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("classId", classId);
        params.put("signType", signType);
        params.put("time",time);
        if(selection==1)
        {
            params.put("locationXy",locationXy);
        }
        Api.config(ApiConfig.STARTSIGN, params).postRequest(this,new CallBack() {
            @Override
            public void OnSuccess(final String res, Response response) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(res, BaseResponse.class);
                if(baseResponse.getCode()==200)
                {

                    ShowToastAsyn("开始签到成功！");
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