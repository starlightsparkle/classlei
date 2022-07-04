package com.example.curriculumdesign.fragment;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.activity.ClassDatailActivity;
import com.example.curriculumdesign.adapter.StuSignAdapter;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.BaseResponse;
import com.example.curriculumdesign.entity.ClassEntity;
import com.example.curriculumdesign.entity.SignEntity;
import com.example.curriculumdesign.entity.TblUser;
import com.example.curriculumdesign.entity.responseBody.SignStuResponse;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PrimitiveIterator;

import okhttp3.Response;

public class SignStuFragment extends BaseFragment {

    private int categoryId;//0为已签到的学生名单 1为未签到
    private RecyclerView recyclerView;
    private SignEntity currentSign;
    private RefreshLayout refreshLayout;
    private List<TblUser> datas = new ArrayList<>();
    private StuSignAdapter adapter ;




    @Override
    protected int initLayout() {
        return R.layout.fragment_sign_stu;
    }

    @Override
    protected void initView() {
        refreshLayout = mRootView.findViewById(R.id.stuRefreshLayout);
        recyclerView=mRootView.findViewById(R.id.recyclerStuView);
        adapter = new StuSignAdapter(getActivity(),categoryId,currentSign);
        adapter.setOnItemClickListener(obj -> {
//            Log.d("1", "initView: "+((TblUser) obj).getUsername()+"  "+categoryId+" "+currentSign.getClassSignId());
            changeStatus((TblUser) obj);
        });
        initRecyclerView();
        recyclerView.setAdapter(adapter);
        getStu();

    }

    @Override
    protected void initData() {

    }

    private void getStu(){
        HashMap<String,Object> params = new HashMap<>();
        params.put("classSignId",currentSign.getClassSignId());
        Gson gson = new Gson();
        if (categoryId==0){
            Api.config(ApiConfig.FINISHSTUDENT,params).getRequest(getActivity(), new CallBack() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void OnSuccess(String res, Response response) {

                    getActivity().runOnUiThread(()->{
                    SignStuResponse json = gson.fromJson(res, SignStuResponse.class);
                    List<TblUser> list = json.getResult();
                        if (response!=null &&list.size()>0) {
                            list.remove(0);
                            datas=list;
                        adapter.setDatas(list);
                        }
                        else{
                            datas=null;
                            adapter.setDatas(datas);
                        }
                        refreshLayout.finishRefresh(true);//延时多久关闭动画
                        adapter.notifyDataSetChanged();//刷新数据
                });

                }

                @Override
                public void OnFailure(Exception e) {
                    refreshLayout.finishRefresh(true);
                }
            });
        }
        else{
            Api.config(ApiConfig.UNFINISHSTUDENT,params).getRequest(mRootView.getContext(), new CallBack() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void OnSuccess(String res, Response response) {
                    refreshLayout.finishRefresh(true);//延时多久关闭动画
                    getActivity().runOnUiThread(() -> {
                        SignStuResponse json = gson.fromJson(res, SignStuResponse.class);
                        List<TblUser> list = json.getResult();
                        if (response!=null &&list.size()>0)
                        {
                        datas=json.getResult();
                        adapter.setDatas(datas);
                        }
                        else{
                            datas=null;
                            adapter.setDatas(datas);
//                            showToastSync("暂时加载无数据");
                        }
                        adapter.notifyDataSetChanged();//刷新数据
                    });

                }

                @Override
                public void OnFailure(Exception e) {
                    refreshLayout.finishRefresh(true);
                }
            });

        }
    }


    public static SignStuFragment newInstance(int categoryId,SignEntity currentSign) {
        SignStuFragment fragment = new SignStuFragment();
        fragment.categoryId=categoryId;
        fragment.currentSign=currentSign;
        return fragment;
    }


    private void changeStatus(TblUser entity){
        Log.d("1", "initView: "+entity.getUserId()+"  "
                +categoryId+" "+currentSign.getClassSignId());
        HashMap<String,Object> params = new HashMap<>();
        params.put("userId",entity.getUserId());
        params.put("status",categoryId);
        params.put("classSignId",currentSign.getClassSignId());
        Gson gson = new Gson();
//        if (categoryId==0){
//
//        }
//        else {
            Api.config(ApiConfig.SignExchange,params).postRequest(mRootView.getContext(), new CallBack() {
                @Override
                public void OnSuccess(String res, Response response) {
                    Log.d("change", "OnSuccess: +"+res);
                    BaseResponse Response = gson.fromJson(res, BaseResponse.class);
                    if (Response.getCode() == 200) {
                        showToastSync("成功修改"+entity.getUsername()+"的签到");
                        getStu();
                    }
                }
                @Override
                public void OnFailure(Exception e) {

                }
            });



    }


    /**
     * 加载view
     */
    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        refreshLayout.setOnRefreshListener((refreshLayout)->{
            getStu();
        });
    }
}