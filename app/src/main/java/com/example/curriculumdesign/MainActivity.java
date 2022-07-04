package com.example.curriculumdesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.curriculumdesign.activity.BaseActivity;
import com.example.curriculumdesign.activity.HomeActivity;
import com.xuexiang.xui.widget.banner.recycler.BannerLayout;

import com.xuexiang.xui.widget.toast.XToast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements BannerLayout.OnBannerItemClickListener {

//    private MyBannerAdapter mAdapter;
    private BannerLayout mBl;
//    轮播图数据
    public String[] urls = new String[]{
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160323071011277.jpg",
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144158380433341332.jpg",
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160286644953923.jpg",
            "http://photocdn.sohu.com/tvmobilemvms/20150902/144115156939164801.jpg",
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144159406950245847.jpg",
    };
    public List<Integer> PicList = new ArrayList<>(5);

    /**
     * 启动的界面如 R.layout.activity_home
     *
     * @return R.layout.activity_home
     */
    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void initView() {
        mBl = findViewById(R.id.main_bl);
    }
    @Override
    protected void initData() {
        PicList.add(R.drawable.p1);
        PicList.add(R.drawable.p2);
        PicList.add(R.drawable.p3);
        PicList.add(R.drawable.p4);
        PicList.add(R.drawable.p5);
       // mAdapter = new BannerAdapter(this ,PicList);
      //  mBl.setAdapter(mAdapter);
//
//        mAdapter.setOnBannerItemClickListener(this);
////        ShowToast(GetStringFromSP("Authorization"));
    }
    @Override
    public void onItemClick(int position) {
        XToast.normal(MainActivity.this,"点击了第 "+(position+1)+" 个"  ).show();
    }
}