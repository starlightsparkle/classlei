package com.example.curriculumdesign.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.activity.ClassDatailActivity;
import com.example.curriculumdesign.adapter.ClassAdapter;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.ClassEntity;
import com.example.curriculumdesign.entity.Page;
import com.example.curriculumdesign.entity.ResponseBody;
import com.example.curriculumdesign.entity.TblUser;
import com.example.curriculumdesign.entity.pageResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.LongFunction;

import okhttp3.Response;

public class ClassFragment extends BaseFragment {

    private int categoryId;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private List<ClassEntity> datas = new ArrayList<>();
    private int pageNum=1;
    private ClassAdapter adapter ;


    @Override
    protected int initLayout() {
        return R.layout.fragment_class;
    }

    @Override
    protected void initView() {

        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        recyclerView=mRootView.findViewById(R.id.recyclerView);
        adapter = new ClassAdapter(getActivity());

        initRecyclerView();
//        getClass(true,categoryId);
        recyclerView.setAdapter(adapter);
//        tv.setText(title);

    }
    @Override
    protected void initData() {

    }



    /**
     * 获取选课列表
     */
    private void getClass(boolean isRefresh,int type){
        String url=ApiConfig.CLASSLIST;
        if (type==0)
            url=ApiConfig.CLASSLIST;
        else
            url=ApiConfig.CLASSLISTNYME;

        HashMap<String,Object> params = new HashMap<>();
        params.put("pageNum",pageNum);
        params.put("pageSize",ApiConfig.PAGE_SIZE);

        Api.config(url,params).getRequest(getActivity(), new CallBack() {
            @Override
            public void OnSuccess(String res, Response response) {
                getActivity().runOnUiThread(()->{
                    if (isRefresh){
                        refreshLayout.finishRefresh(true);//关闭下拉刷新
                    }
                    else
                    {
                        refreshLayout.finishLoadMore(true);
                    }
                    Gson gson = new Gson();
                    List<ClassEntity> list = new ArrayList<>();
                    try {
                        pageResponse body = gson.fromJson(res, pageResponse.class);
                        if(body.getCode()==200)
                        {
                            list= body.getResult().getList();
                        }
                    }catch (Exception e){
                        for (int i = 0; i <8 ; i++) {
                            list.add(new ClassEntity("深度学习(2021_10_17)","快来选课"));
                        }
                    }
                    if (response!=null &&list.size()>0)
                    {
                        if(isRefresh)
                            datas=list;
                        else
                            datas.addAll(list);

                        adapter.setDatas(datas);
                        adapter.setOnItemClickListener((obj -> {
                            ClassEntity newsEntity = (ClassEntity) obj;
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("class",newsEntity);
                            navigateToWithBundle(ClassDatailActivity.class, bundle);
                        }));
                        adapter.notifyDataSetChanged();//刷新数据
                    }
                    else{
                        if(isRefresh)
                            showToast("暂时加载无数据");
                        else
                            showToast("没有更多数据");
                    }

                });

            }

            @Override
            public void OnFailure(Exception e) {
               if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
            }
        });
    }





    public static ClassFragment newInstance(int categoryId) {
        ClassFragment fragment = new ClassFragment();
        fragment.categoryId=categoryId;
        return fragment;
    }

    /**
     * 加载view
     */
    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        refreshLayout.setOnRefreshListener((refreshLayout)->{
//            refreshLayout.finishRefresh(100);//延时多久关闭动画
            pageNum=1;
            getClass(true,categoryId);
        });
        refreshLayout.setOnLoadMoreListener((refreshLayout)->{
            pageNum++;
            getClass(false,categoryId);
        });
        getClass(true,categoryId);
    }
}