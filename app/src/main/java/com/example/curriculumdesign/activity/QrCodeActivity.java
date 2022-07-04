package com.example.curriculumdesign.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.BaseResponse;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.king.zxing.CameraScan;
import com.king.zxing.CaptureActivity;
import com.king.zxing.DecodeConfig;
import com.king.zxing.DecodeFormatManager;
import com.king.zxing.analyze.MultiFormatAnalyzer;
import com.king.zxing.util.CodeUtils;
import com.king.zxing.util.LogUtils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class QrCodeActivity extends BaseActivity{



    //弹出扫描结果
    private Toast toast;
    private Class<?> cls;
    private String title;
    //是否开启连续扫描
    private boolean isContinuousScan;
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_IS_QR_CODE = "key_code";
    public static final String KEY_IS_CONTINUOUS = "key_continuous_scan";

    public static final int REQUEST_CODE_SCAN = 0X01;
    public static final int REQUEST_CODE_PHOTO = 0X02;
    public static final int RC_CAMERA = 0X01;
    public static final int RC_READ_PHOTO = 0X02;

    private int sign=0;

    private ImageView imageView;
    private TextView textView;

    /**
     * 启动的界面如 R.layout.activity_home
     * @return R.layout.activity_home
     */
    @Override
    protected int initLayout() {
        return R.layout.activity_qrcode;
    }
    /**
     * 在这个绑定视图
     */
    @Override
    protected void initView() {
        cls = CaptureActivity.class;
        imageView=findViewById(R.id.jiegu);
        textView=findViewById(R.id.content);
    }
    /**
     * 加载绑定数据
     */
    @Override
    protected void initData() {
        checkCameraPermissions();

//        if (sign==0)
//        finish();
    }

    /**
     * 动态查看权限，判断摄像头是否可用
     */
    @AfterPermissionGranted(RC_CAMERA)
    private void checkCameraPermissions(){
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {//有权限
            startScan(cls,title);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_camera),
                    RC_CAMERA, perms);
        }
    }

    /**
     * 传递扫描结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            switch (requestCode){
                case REQUEST_CODE_SCAN:
                    //返回扫描结果
                    String result = CameraScan.parseScanResult(data);
                    //0签到，1加课
                    String[] res=result.split(",");
                    codeResoulve(res[0],res[1]);
                    ShowToast(result);
                    break;
                case REQUEST_CODE_PHOTO:
                    parsePhoto(data);
                    break;
                default:
                    finish();
            }
        }
        Log.d("TAG", "onActivityResult: 没有操作");
    }


    /**
     * 不知道干啥的，开了个线程
     * @param runnable
     */
    private void asyncThread(Runnable runnable){
        new Thread(runnable).start();
    }
    private Context getContext(){
        return this;
    }
    private void parsePhoto(Intent data){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
            //异步解析
            asyncThread(() -> {
                final String result = CodeUtils.parseCode(bitmap);
                runOnUiThread(() -> {
                    LogUtils.d("result:" + result);
                    Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
                });

            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 扫码
     * @param cls
     * @param title
     */
    private void startScan(Class<?> cls,String title){
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this,R.anim.in,R.anim.out);
        Intent intent = new Intent(this, cls);
        intent.putExtra(KEY_TITLE,title);
        intent.putExtra(KEY_IS_CONTINUOUS,isContinuousScan);
        ActivityCompat.startActivityForResult(this,intent,REQUEST_CODE_SCAN,optionsCompat.toBundle());
    }
    void codeResoulve(String type,String id)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        if(type.equals("1"))
        {
            params.put("classId",Long.parseLong(id));
            Api.config(ApiConfig.JIONCLASS, params).postRequest(this,new CallBack() {
                @Override
                public void OnSuccess(final String res, Response response) {
                    Log.e("onSuccess", res);
                    Gson gson = new Gson();
                    BaseResponse baseResponse = gson.fromJson(res, BaseResponse.class);
                    sign=1;
                    if(baseResponse.getCode()==200)
                    {
                        runOnUiThread(()->{
                            textView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                            textView.setText(baseResponse.getMessage());
                        });
                        ShowToastAsyn("加课成功！！");
                    }
                    else
                    {
                        runOnUiThread(()->{
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageResource(R.mipmap.fail);
                            textView.setTextColor(mContext.getResources().getColor(R.color.red));
                            textView.setText(baseResponse.getMessage());
                            textView.setVisibility(View.VISIBLE);
                        });

                        ShowToastAsyn(baseResponse.getMessage());
                    }
                }

                @Override
                public void OnFailure(Exception e) {
                    Log.e("onFailure", e.toString());
                    runOnUiThread(()->{
                        imageView.setImageResource(R.mipmap.fail);
                        textView.setTextColor(mContext.getResources().getColor(R.color.red));
                        textView.setText("连接服务器失败！");
                        textView.setVisibility(View.VISIBLE);
                        });


                    ShowToastAsyn("连接服务器失败！");
                }
            });
        }
        else
            {
                params.put("classSignId",Long.parseLong(id));
                params.put("signType",0);
                Api.config(ApiConfig.SIGN, params).postRequest(this,new CallBack() {
                    @Override
                    public void OnSuccess(final String res, Response response) {
                        Log.e("onSuccess", res);
                        Gson gson = new Gson();
                        BaseResponse baseResponse = gson.fromJson(res, BaseResponse.class);
                        if(baseResponse.getCode()==200)
                        {

                            runOnUiThread(()->{
                                textView.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                                textView.setText(baseResponse.getMessage());

                            });

                            ShowToastAsyn("签到成功！！");
                        }
                        else
                        {
                            runOnUiThread(()->{
                                imageView.setImageResource(R.mipmap.fail);
                                textView.setTextColor(mContext.getResources().getColor(R.color.red));
                                textView.setText(baseResponse.getMessage());
                                textView.setVisibility(View.VISIBLE);
                            });

                            ShowToastAsyn(baseResponse.getMessage());
                        }
                    }

                    @Override
                    public void OnFailure(Exception e) {
                        Log.e("onFailure", e.toString());
                         runOnUiThread(()->{
                             imageView.setImageResource(R.mipmap.fail);
                             textView.setTextColor(mContext.getResources().getColor(R.color.red));
                             textView.setText("连接服务器失败！");
                             textView.setVisibility(View.VISIBLE);
                            });
                        ShowToastAsyn("连接服务器失败！");
                    }
                });
            }
        }



}