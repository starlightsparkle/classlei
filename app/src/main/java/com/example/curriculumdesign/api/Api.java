package com.example.curriculumdesign.api;

import static android.content.Context.MODE_PRIVATE;

import static com.example.curriculumdesign.api.ApiConfig.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.curriculumdesign.activity.LoginActivity;
import com.example.curriculumdesign.utils.StringUtils;

public class Api {
    private static OkHttpClient client;
    private static String requestUrl;
    private static HashMap<String,Object> mParams;
    public static Api api=new Api();
    public Api()
    {

    }
    public static Api config(String url,HashMap<String,Object> params)
    {
        client=new OkHttpClient.Builder().build();
        requestUrl= BASE_URL+url;
        mParams=params;
        return api;
    }
    public static Api codeUrl(String url,HashMap<String,Object> params)
    {
        client=new OkHttpClient.Builder().build();
        requestUrl= url;
        mParams=params;
        return api;
    }
    //不添加令牌的post请求，用于注册和登陆
    public void postRequest1(Context mContext,final CallBack callback)
    {
        String url=getAppendUrl(requestUrl,mParams);
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8")
                        , jsonStr);
        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(url)
                .addHeader("contentType", "application/json;charset=UTF-8")
                .post(requestBodyJson)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.OnFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                callback.OnSuccess(result,response);
            }
        });
    }
    //添加令牌的post请求，用于普通的请求
    public void postRequest(Context mContext,final CallBack callback)
    {
        SharedPreferences sp = mContext.getSharedPreferences("sp_znjz", MODE_PRIVATE);
        String token = sp.getString("Authorization", "");
        String url=getAppendUrl(requestUrl,mParams);
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8")
                        , jsonStr);
        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(url)
                .addHeader("contentType", "application/json;charset=UTF-8")
                .addHeader("Authorization",token)
                .post(requestBodyJson)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.OnFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                callback.OnSuccess(result,response);
            }
        });
    }

    public void postRequest(final CallBack callback)
    {
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8")
                        , jsonStr);
        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("contentType", "application/json;charset=UTF-8")
                .post(requestBodyJson)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.OnFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Headers headers = response.headers();
                callback.OnSuccess(result,response);

            }
        });
    }
    public void getRequest(Context mContext,final CallBack callBack)
    {
        SharedPreferences sp = mContext.getSharedPreferences("sp_znjz", MODE_PRIVATE);
        String token = sp.getString("Authorization", "");
        String url=getAppendUrl(requestUrl,mParams);
        Request request=new Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
                .get()
                .build();
        Call call =client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure",e.getMessage());
                callBack.OnFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String message=jsonObject.getString("message");
                    if(message.equals("请先登录"))
                    {
                        Intent in=new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(in);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.OnSuccess(result,response);
            }
        });
    }
    //delete请求
    public void deleteRequest(Context mContext,final CallBack callBack)
    {
        SharedPreferences sp = mContext.getSharedPreferences("sp_znjz", MODE_PRIVATE);
        String token = sp.getString("Authorization", "");
        String url=getAppendUrl(requestUrl,mParams);
        Request request=new Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
                .delete()
                .build();
        Call call =client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure",e.getMessage());
                callBack.OnFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String message=jsonObject.getString("message");
                    if(message.equals("请先登录"))
                    {
                        Intent in=new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(in);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.OnSuccess(result,response);
            }
        });
    }
    //put请求
    public void putRequest(Context mContext,final CallBack callback)
    {
        SharedPreferences sp = mContext.getSharedPreferences("sp_znjz", MODE_PRIVATE);
        String token = sp.getString("Authorization", "");
        String url=getAppendUrl(requestUrl,mParams);
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8")
                        , jsonStr);
        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(url)
                .addHeader("contentType", "application/json;charset=UTF-8")
                .addHeader("Authorization",token)
                .put(requestBodyJson)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.OnFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String message=jsonObject.getString("message");
                    if(message.equals("请先登录"))
                    {
                        Intent in=new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(in);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.OnSuccess(result,response);
            }
        });
    }
    private String getAppendUrl(String url, Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (StringUtils.IsEmpty(buffer.toString())) {
                    buffer.append("?");
                } else {
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }
}
