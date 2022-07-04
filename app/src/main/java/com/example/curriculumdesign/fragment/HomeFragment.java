package com.example.curriculumdesign.fragment;



import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.activity.NewClassActivity;
import com.example.curriculumdesign.activity.QrCodeActivity;
import com.example.curriculumdesign.adapter.HomeAdapter;
import com.example.curriculumdesign.entity.TblUser;
import com.flyco.tablayout.SlidingTabLayout;
import java.util.ArrayList;




public class HomeFragment extends BaseFragment  {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles={"我选的课","我教的课"};
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private HomeAdapter mAdapter;
    //二维码按钮
    private ImageView CodeBtn;

    private Button newClassBtn;

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        newClassBtn=mRootView.findViewById(R.id.new_class_btn);
//        tv = mRootView.findViewById(R.id.title);
        if (haveAuth())
        {
            newClassBtn.setVisibility(View.VISIBLE);
            newClassBtn.setOnClickListener((v -> {

                navigateTo(NewClassActivity.class);
//                showToast("新建课程");
            }));
        }
    CodeBtn=mRootView.findViewById(R.id.codeBtn);
    viewPager=mRootView.findViewById(R.id.fixedViewpager);
    slidingTabLayout=mRootView.findViewById(R.id.slidingTabLayout);
    }

    @Override
    protected void initData() {
        CodeBtn.setOnClickListener((View v) -> navigateTo(QrCodeActivity.class) );
        initTab();


    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    /**
     * 初始化导航条
     */
    private void initTab(){
        int i=0;
        for (String title: mTitles) {
            mFragments.add(ClassFragment.newInstance(i));
            i++;
        }
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new HomeAdapter(getFragmentManager(),mTitles,mFragments));
        slidingTabLayout.setViewPager(viewPager);
    }



    private Boolean haveAuth(){
        TblUser user = sp.getUserFromSP(mRootView.getContext());
        if (user==null)
            return false;
        if (user!=null && user.getRoleId()==0 || user.getRoleId()==2){
            return true;
        }
        else
            return false;
    }






}