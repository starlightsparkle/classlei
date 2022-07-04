package com.example.curriculumdesign.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.ServiceSettings;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.curriculumdesign.R;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.FinsihStudentResponse;
import com.example.curriculumdesign.entity.Gps;
import com.example.curriculumdesign.entity.TblUserSign;
import com.example.curriculumdesign.entity.UserResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

public class ChangeMapActivity extends AppCompatActivity {
    public Context mContext;
    MapView mapView=null;//地图视图

    AMap aMap;//地图对象

    CameraUpdate cameraUpdate;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_change_map);
        mapView= (MapView) findViewById(R.id.map1);

        mapView.onCreate(savedInstanceState);//创建地图

        aMap=mapView.getMap();//获取地图对象
        Intent intent= getIntent();
        String classSignId=intent.getStringExtra("classSignId");
        getFinsihList(Long.parseLong(classSignId));
        //getadress();
    }
    public void makepoint(String s,List<Gps> gpsList){
        Log.e("map","开始绘图");

//北纬39.22，东经116.39，为负则表示相反方向

        LatLng latLng=new LatLng(29.22,116.39);

        Log.e("地址",s);
        //自定义点标记

        MarkerOptions markerOptions=new MarkerOptions();

        markerOptions.position(new LatLng(gpsList.get(0).getV(),gpsList.get(0).getV1())).title("老师").snippet(s);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory

                .decodeResource(getResources(),R.drawable.ic_first_place)));//设置图标

        aMap.addMarker(markerOptions);

//使用默认点标记

        Marker maker=aMap.addMarker(new MarkerOptions().position(latLng).title("地点:").snippet(s));
        for (int i=1;i<gpsList.size();i++)
        {
            Gps gps=gpsList.get(i);

             latLng=new LatLng(gps.getV(),gps.getV1());

            Log.e("学生：",gps.getUsername());

//使用默认点标记

            aMap.addMarker(new MarkerOptions().position(latLng).title("学生:").snippet(gps.getUsername()));
        }




//改变可视区域为指定位置

//CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向(正北逆时针算起，0-360)

        cameraUpdate= CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,8,0,30));

        aMap.moveCamera(cameraUpdate);//地图移向指定区域

//位置坐标的点击事件

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override

            public boolean onMarkerClick(Marker marker) {
//Toast.makeText(MainActivity.this,"点击指定位置",Toast.LENGTH_SHORT).show();

                return false;

            }

        });

//位置上面信息窗口的点击事件

        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override

            public void onInfoWindowClick(Marker marker) {
//                Toast.makeText(this,"点击了我的地点",Toast.LENGTH_SHORT).show();

            }

        });

    }

//解析指定坐标的地址

    public void getadress(FinsihStudentResponse finsihStudentResponseList){
        Log.e("map","调用getadress");
        List<Gps> gpsList=new ArrayList<Gps>();
       List<TblUserSign> tblUserSigns=finsihStudentResponseList.getResult();
        for (TblUserSign tblUserSign : tblUserSigns) {
            String[] vs=tblUserSign.getLocationXy().split(",");
            double v=Double.parseDouble(vs[0]);
            double v1=Double.parseDouble(vs[1]);
            Gps gps=new Gps();
            gps.setV(v);
            gps.setV1(v1);
            gps.setUsername(tblUserSign.getUsername());
            gpsList.add(gps);
        }
        GeocodeSearch geocodeSearch= null;//地址查询器
        try {
            ServiceSettings.updatePrivacyShow(this, true, true);
            ServiceSettings.updatePrivacyAgree(this,true);
            geocodeSearch = new GeocodeSearch(this);
        } catch (AMapException e) {
            e.printStackTrace();
        }

//设置查询参数,

//三个参数依次为坐标，范围多少米，坐标系

        RegeocodeQuery regeocodeQuery=new RegeocodeQuery(new LatLonPoint(gpsList.get(0).getV(),gpsList.get(0).getV1()),200,GeocodeSearch.AMAP);

//设置查询结果监听

        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {

//根据坐标获取地址信息调用

            @Override

            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                String s =regeocodeResult.getRegeocodeAddress().getFormatAddress();

                Log.e("map","获得请求结果");

                makepoint(s,gpsList);

            }

//根据地址获取坐标信息是调用

            @Override

            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            }

        });

        geocodeSearch.getFromLocationAsyn(regeocodeQuery);//发起异步查询请求

    }

//将地图生命周期跟活动绑定，减少某些不必要的bug

    @Override

    protected void onPostResume() {
        super.onPostResume();

        mapView.onResume();

    }

    @Override

    protected void onPause() {
        super.onPause();

        mapView.onPause();

    }

    @Override

    protected void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();

    }
    void getFinsihList(Long classSignId)
    {
        HashMap<String, Object> params = new HashMap<>();
        params.put("classSignId",classSignId);
        Api.config(ApiConfig.FINISHSTUDENT, params).getRequest(mContext, new CallBack() {
            @Override
            public void OnSuccess(final String res, Response response) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                FinsihStudentResponse finsihStudentResponse = gson.fromJson(res, FinsihStudentResponse.class);
                if(finsihStudentResponse.getCode()==200)
                {

                    if(finsihStudentResponse.getResult().size()>0)
                    {
                        getadress(finsihStudentResponse);
                    }
                    else
                    {
                        ShowToastAsyn("没有人签到呢！");
                    }
                }


            }

            @Override
            public void OnFailure(Exception e) {
                Log.e("onFailure", e.getMessage());
                ShowToastAsyn("连接服务器失败，请稍后再试试！");
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
