package com.example.curriculumdesign.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.curriculumdesign.R;

public class getMsgActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_msg);

        tv = findViewById(R.id.tv);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        tv.setText("获取到新消息：" + data);
    }

}