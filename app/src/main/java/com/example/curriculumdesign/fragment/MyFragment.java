package com.example.curriculumdesign.fragment;

import android.content.Intent;

import android.view.View;
import android.widget.TextView;

import butterknife.OnClick;;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.activity.LoginActivity;
import com.example.curriculumdesign.entity.TblUser;
import com.example.curriculumdesign.utils.SPUtils;


public class MyFragment extends BaseFragment {


    private TextView username;
    private TextView role;
    private SPUtils sp;
    private TblUser user;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        sp=new SPUtils();
        username=mRootView.findViewById(R.id.username);
        role=mRootView.findViewById(R.id.roles);
        user = sp.getUserFromSP(mRootView.getContext());

    }

    @Override
    protected void initData() {
        if (username!=null&&user!=null){
            username.setText(user.getUsername());
            String[] strings={"管理员","学生","教师"};
            role.setText(strings[Integer.parseInt(user.getRoleId().toString())]);
        }

    }

    @OnClick({R.id.img_header,R.id.btn_logout})
    public void onViewClicked(View view){
        switch(view.getId()){
            case R.id.img_header:
                break;
            case R.id.btn_logout:
                sp.clear(mRootView.getContext());
                removeByKey("Authorization");
                removeByKey("user_info");
                navigateToWithFlag(LoginActivity.class,
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                showToast("退出登录");
        }
    }

}