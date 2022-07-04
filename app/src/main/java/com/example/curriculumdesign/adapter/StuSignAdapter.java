package com.example.curriculumdesign.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.api.Api;
import com.example.curriculumdesign.api.ApiConfig;
import com.example.curriculumdesign.api.CallBack;
import com.example.curriculumdesign.entity.BaseResponse;
import com.example.curriculumdesign.entity.ClassEntity;
import com.example.curriculumdesign.entity.SignEntity;
import com.example.curriculumdesign.entity.TblUser;
import com.example.curriculumdesign.entity.TblUserSign;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

public class StuSignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<TblUser> datas;
    private int categoryId;
    private SignEntity currentSign;
    private StuSignAdapter.OnItemClickListener mOnItemClickListener;

    public StuSignAdapter(Context context, int categoryId, SignEntity currentSign) {
        this.mContext = context;
        this.currentSign=currentSign;
        this.categoryId=categoryId;
    }


    public interface OnItemClickListener {
        void onItemClick(Serializable obj);
    }
    public void setOnItemClickListener(StuSignAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public List<TblUser> getDatas() {
        return datas;
    }

    public void setDatas(List<TblUser> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sign_stu, parent, false);
        StuSignAdapter.ViewHolder viewHolder = new StuSignAdapter.ViewHolder(view);
        return viewHolder;
    }




    /**
     * 绑定数据
     * @param holder 当前的holder对象
     * @param position 下标
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StuSignAdapter.ViewHolder viewHolder=(StuSignAdapter.ViewHolder)holder;
        TblUser entity = datas.get(position);
        viewHolder.id=position;
        viewHolder.Stu_name.setText(entity.getUsername());
        viewHolder.entity=entity;

        //
//        Picasso.with(mContext).load(entity.getURl).into(viewHolder.class_url);
    }

    @Override
    public int getItemCount() {
        if (datas!=null&& datas.size()>0)
            return datas.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private long id;
        private TextView Stu_name;
        private ImageView imageView;
        private Button change_sign_btn;
        private TblUser entity;

        public ViewHolder(@NonNull View view) {
            super(view);
            Stu_name=view.findViewById(R.id.stu_name);
            imageView=view.findViewById(R.id.stu_state);
            change_sign_btn=view.findViewById(R.id.change_sign);
            if (categoryId==1){
                change_sign_btn.setText("签到");
                change_sign_btn.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_color_green));
                Picasso.with(mContext).load(R.mipmap.absence).resize(30,30).centerCrop().into(imageView);
            }
            else{
                change_sign_btn.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_color_red));
                Picasso.with(mContext).load(R.mipmap.absence1).resize(30,30).centerCrop().into(imageView);
            }
            change_sign_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(entity);
                }
            });
        }
    }
}
