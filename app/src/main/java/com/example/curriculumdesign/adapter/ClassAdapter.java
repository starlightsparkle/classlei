package com.example.curriculumdesign.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.curriculumdesign.R;
import com.example.curriculumdesign.entity.ClassEntity;
import java.io.Serializable;
import java.util.List;


public class ClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<ClassEntity> datas;
    private OnItemClickListener mOnItemClickListener;

    public ClassAdapter(Context context) {
        this.mContext = context;
    }


    public interface OnItemClickListener {
        void onItemClick(Serializable obj);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public List<ClassEntity> getDatas() {
        return datas;
    }

    public void setDatas(List<ClassEntity> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_class_layout_new, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }




    /**
     * 绑定数据
     * @param holder 当前的holder对象
     * @param position 下标
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder=(ViewHolder)holder;
        ClassEntity entity = datas.get(position);
        viewHolder.class_name.setText(entity.getClassName());
        viewHolder.class_content.setText(entity.getClassContent());
        viewHolder.classEntity=entity;
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
        private TextView class_name;
        private TextView class_content;
        private ClassEntity classEntity;

        public ViewHolder(@NonNull View view) {
            super(view);
            class_name=view.findViewById(R.id.class_name);
            class_content=view.findViewById(R.id.class_content);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(classEntity);
                }
            });

        }
    }
}
