package com.example.curriculumdesign.activity;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.curriculumdesign.R;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.BaseResponse;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

public class GpsActivity extends AppCompatActivity {
    public Context mContext;
    String type;
    String classId;
    String time;
    String classSignId;
    //是否需要检测后台定位权限，设置为true时，如果用户没有给予后台定位权限会弹窗提示
    private boolean needCheckBackLocation = false;
    //如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSON_REQUESTCODE = 0;

    private MapView mapView = null;
    private AMap aMap;
    public double jingdu;//经度
    public double weidu;//纬度

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    weidu = aMapLocation.getLatitude();//获取纬度
                    jingdu = aMapLocation.getLongitude();//获取经度
                    Log.e("s", "jingdu: " + jingdu);
                    Log.e("s", "weidu: " + weidu);
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        classId=intent.getStringExtra("classId");
        time=intent.getStringExtra("time");
        classSignId=intent.getStringExtra("signid");
        setContentView(R.layout.activity_gps);
        if(Build.VERSION.SDK_INT > 28
                && getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            needPermissions = new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    BACKGROUND_LOCATION_PERMISSION
            };
        }
        //获取地图控件
        mapView = (MapView)findViewById(R.id.map);
        //创建地图
        mapView.onCreate(savedInstanceState);
        //初始化地图控件器
        if(aMap == null){
            aMap = mapView.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        myLocationStyle.interval(2000);//设置连续定位的时间间隔
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置蓝点
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        Button btn = findViewById(R.id.btn);
        if(type.equals("sign"))
        {
            btn.setText("发起签到");
        }
        else
        {
            btn.setText("签到");
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("sign"))
                {
                    Long classId1=Long.parseLong(classId);
                    String locationXY=weidu+","+jingdu;
                    Long time1=Long.parseLong(time);
                    startSign(classId1,locationXY,1,time1);
                }
                else
                {
                    String locationXY=weidu+","+jingdu;
                    sign(classSignId,locationXY);
                }
                Log.e("s", "jingdu: " + jingdu);
                Log.e("s", "weidu: " + weidu);

            }
        });

        //初始化定位
        try {
            AMapLocationClient.updatePrivacyShow(this,true,true);
            AMapLocationClient.updatePrivacyAgree(this,true);
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            if (isNeedCheck) {
                checkPermissions(needPermissions);
            }
        }
    }

    /**
     *
     * @param permissions
     * @since 2.5.0
     *
     */
    private void checkPermissions(String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23
                    && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class,
                            int.class});

                    method.invoke(this, array, PERMISSON_REQUESTCODE);
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     *
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23){
            try {
                for (String perm : permissions) {
                    Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
                    Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod("shouldShowRequestPermissionRationale",
                            String.class);
                    if ((Integer)checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED
                            || (Boolean)shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
                        if(!needCheckBackLocation
                                && BACKGROUND_LOCATION_PERMISSION.equals(perm)) {
                            continue;
                        }
                        needRequestPermissonList.add(perm);
                    }
                }
            } catch (Throwable e) {

            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否所有的权限都已经授权
     * @param grantResults
     * @return
     * @since 2.5.0
     *
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                //showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     *
     */
    /*private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }*/

    /**
     *  启动应用的设置
     *
     * @since 2.5.0
     *
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    void startSign(Long classId,String locationXy,int signType,Long time)
    {
        ;
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("classId", classId);
        params.put("signType", signType);
        params.put("time",time);
        params.put("locationXy",locationXy);
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
    void sign(String id,String location)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("classSignId",Long.parseLong(id));
        params.put("signType",0);
        params.put("locationXy",location);
        Api.config(ApiConfig.SIGN, params).postRequest(this,new CallBack() {
            @Override
            public void OnSuccess(final String res, Response response) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(res, BaseResponse.class);
                if(baseResponse.getCode()==200)
                {

                    ShowToastAsyn("签到成功！！");
                    finish();
                }
                else
                {
                    ShowToastAsyn(baseResponse.getMessage());
                    finish();
                }
            }

            @Override
            public void OnFailure(Exception e) {
                Log.e("onFailure", e.toString());
                ShowToastAsyn("连接服务器失败！");
            }
        });
    }

    public void ShowToastAsyn(String msg)
    {
        Looper.prepare();
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

}
