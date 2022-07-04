package com.example.curriculumdesign.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.curriculumdesign.entity.TblUser;
import com.example.curriculumdesign.utils.SPUtils;
import com.google.gson.Gson;

public abstract class BaseActivity extends FragmentActivity {

    public Context mContext;
    public Bundle bundle;
    public TblUser currentUser;
    Gson gson = new Gson();
    public SPUtils sp =new SPUtils();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        bundle=getIntent().getExtras();
        currentUser=sp.getUserFromSP(mContext);
        setContentView(initLayout());
        initView();
        initData();
    }
    public void navigateToWithBundle(Class cls, Bundle bundle) {
        Intent in = new Intent(mContext, cls);
        in.putExtras(bundle);
        startActivity(in);
    }

    /**
     * 启动的界面如 R.layout.activity_home
     * @return R.layout.activity_home
     */
    protected abstract int initLayout();

    /**
     * 在这个绑定视图
     */
    protected abstract void initView();

    /**
     * 加载绑定数据
     */
    protected abstract void initData();
    public void ShowToast(String msg)
    {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
    //跳转
    public void navgateTo(Class cls)
    {
        Intent it=new Intent(mContext,cls);
        startActivity(it);
    }
    //跳转杀死前面的
    public void navgateToWithFlag(Class cls,int flags)
    {
        Intent it=new Intent(mContext,cls);
        it.setFlags(flags);
        startActivity(it);
    }
    //
    public void ShowToastAsyn(String msg)
    {
        Looper.prepare();
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
    protected void SaveToSP(String key,String val)
    {
        SharedPreferences sp=getSharedPreferences("sp_znjz",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key,val);
        editor.commit();
    }
    protected String GetStringFromSP(String key)
    {
        SharedPreferences sp= getSharedPreferences("sp_znjz", MODE_PRIVATE);
        return sp.getString(key,"");
    }

    protected TblUser getUserFromSP()
    {
        SharedPreferences sp= getSharedPreferences("sp_znjz", MODE_PRIVATE);
        String user_if = sp.getString("user_if", "");

        TblUser user=null;
        try {
            user = gson.fromJson(user_if, TblUser.class);
        }catch (Exception e ){
            Log.e("", "GetUserFromSP: 无法错误 " );
        }
        return user;
    }

    protected void setUserToSP(TblUser user)
    {
        SharedPreferences sp=getSharedPreferences("sp_znjz",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        try {
            String json = gson.toJson(user);
            editor.putString("user_if",json);
            editor.commit();
        }catch (Exception e){ }

    }

    public Boolean haveAuth(){
        TblUser user = sp.getUserFromSP(this);
        if (user.getRoleId()==0  || (user.getRoleId()==2)){
            return true;
        }
        else
            return false;
    }


}
