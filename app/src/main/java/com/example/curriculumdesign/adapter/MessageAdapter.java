package com.example.curriculumdesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.entity.ClassEntity;
import com.example.curriculumdesign.entity.MessageEntity;

import java.io.Serializable;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Context mContext;
    private List<MessageEntity> datas;
    private ClassAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Serializable obj);
    }
    public void setOnItemClickListener(ClassAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public MessageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_message, parent, false);
        MessageAdapter.ViewHolder viewHolder = new MessageAdapter.ViewHolder(view);
        return viewHolder;
    }

    public void setDatas(List<MessageEntity> datas) {
        this.datas = datas;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageAdapter.ViewHolder viewHolder=(MessageAdapter.ViewHolder)holder;
        MessageEntity entity = datas.get(position);
        viewHolder.message_title.setText(entity.getMessageTitle());
        viewHolder.message_content.setText(entity.getMessageContent());
        viewHolder.message_date.setText(entity.getGmtCreated());
        viewHolder.entity=entity;
    }

    @Override
    public int getItemCount() {
        if (datas!=null&& datas.size()>0)
            return datas.size();
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private long id;
        private TextView message_title;
        private TextView message_content;
        private MessageEntity entity;
        private TextView message_date;
        public ViewHolder(@NonNull View view) {
            super(view);
            message_title=view.findViewById(R.id.tv_new_message_title);
            message_content=view.findViewById(R.id.tv_new_message_content);
            message_date=view.findViewById(R.id.tv_sign_time);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(entity);
                }
            });

        }
    }
}
