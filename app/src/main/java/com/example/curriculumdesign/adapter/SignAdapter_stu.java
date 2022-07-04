package com.example.curriculumdesign.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.entity.ClassEntity;
import com.example.curriculumdesign.entity.SignEntity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class SignAdapter_stu extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<SignEntity> datas;
    private SignAdapter_stu.OnItemClickListener mOnItemClickListener;


    public SignAdapter_stu(Context context) {
        mContext=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_class_detail_for_stu, parent, false);
        SignAdapter_stu.ViewHolder viewHolder = new SignAdapter_stu.ViewHolder(view);
        return viewHolder;
    }

    public List<SignEntity> getDatas() {
        return datas;
    }

    public void setDatas(List<SignEntity> datas) {
        this.datas = datas;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SignAdapter_stu.ViewHolder viewHolder=(SignAdapter_stu.ViewHolder)holder;
        SignEntity entity = datas.get(position);
        viewHolder.sign_name.setText(entity.getSignName());
        viewHolder.sign_createTime.setText(entity.getGmtCreated());
        if (entity.getStatus()==0){
            Picasso.with(mContext).load(R.mipmap.absence).resize(40,40).into(viewHolder.status);
            viewHolder.sign_isClose.setText("已结束");
            viewHolder.sign_isClose.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else{
            Picasso.with(mContext).load(R.mipmap.absence1).resize(40,40).into(viewHolder.status);
        }
        viewHolder.signEntity=entity;
    }

    @Override
    public int getItemCount() {
        if (datas!=null&& datas.size()>0)
            return datas.size();
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private long id;
        private TextView sign_name;
        private TextView sign_createTime;
        private TextView sign_isClose;
        private ImageView status;
        private SignEntity signEntity;


        public ViewHolder(@NonNull View view) {
            super(view);
            sign_name=view.findViewById(R.id.sign_stu_name);
            sign_createTime=view.findViewById(R.id.sign_stu_create_time);
            sign_isClose=view.findViewById(R.id.is_close);
            status=view.findViewById(R.id.status);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(signEntity,signEntity.getStatus());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Serializable obj,int status);
    }
    public void setOnItemClickListener(SignAdapter_stu.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
