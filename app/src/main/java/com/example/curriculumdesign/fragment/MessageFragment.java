package com.example.curriculumdesign.fragment;



import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.activity.ClassDatailActivity;
import com.example.curriculumdesign.adapter.MessageAdapter;
import com.example.curriculumdesign.adapter.StuSignAdapter;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.ClassEntity;
import com.example.curriculumdesign.entity.MessageEntity;
import com.example.curriculumdesign.entity.MessageResponse;
import com.example.curriculumdesign.entity.TblUser;
import com.example.curriculumdesign.entity.UserResponse;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;


public class MessageFragment extends BaseFragment {


    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private List<MessageEntity> datas = new ArrayList<>();
    private MessageAdapter adapter ;

    @Override
    protected int initLayout() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView() {
        refreshLayout=mRootView.findViewById(R.id.refreshMessageLayout);
        recyclerView=mRootView.findViewById(R.id.recyclerMessageView);
        adapter = new MessageAdapter(getActivity());
        initRecyclerView();
        getStu();

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    private void getStu(){
        getMessage();
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
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
            getMessage();
        });
    }
    void getMessage() {
        HashMap<String, Object> params = new HashMap<>();
        Api.config(ApiConfig.MESSAGELIST, params).getRequest(getActivity(), new CallBack() {
            @Override
            public void OnSuccess(final String res, Response response) {
                refreshLayout.finishRefresh(true);
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                MessageResponse messageResponse = gson.fromJson(res, MessageResponse.class);
//                ShowToastAsyn(userResponse.toString());
                if (messageResponse.getCode() == 200) {
                   datas= messageResponse.getResult();

                } else {
                    showToastSync(messageResponse.getMessage());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(datas.size()>0)
                        {
                            Log.e("test","1");
                            adapter.setDatas(datas);
                            adapter.setOnItemClickListener((obj -> {

                            }));
                            adapter.notifyDataSetChanged();//刷新数据
                        }
                        else
                        {
                            showToast("暂时没有获得数据");
                        }
                    }
                });

            }

            @Override
            public void OnFailure(Exception e) {
//                Log.e("onFailure", e.getMessage());
                refreshLayout.finishRefresh(true);
                showToastSync("连接服务器失败！");
            }
        });



}
}