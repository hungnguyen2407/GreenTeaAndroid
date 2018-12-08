package com.greentea.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greentea.R;
import com.greentea.model.MemoryName;
import com.greentea.model.NameOfResources;

public class GroupDetailActivity extends AppCompatActivity {
    private Activity groupDetailActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        groupDetailActivity = this;
        ImageButton btnReturn = findViewById(R.id.group_detail_back_btn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        groupDetailHandler();
    }

    private void groupDetailHandler() {
        TextView tvName = findViewById(R.id.group_detail_tv_name);
        TextView tvMemNum = findViewById(R.id.group_detail_mem_num);
        SharedPreferences sp = getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
        tvName.setText(sp.getString(NameOfResources.GROUP_NAME.toString(), ""));
        tvMemNum.setText("Số thành viên " + sp.getString(NameOfResources.GROUP_MEM_NUM.toString(), ""));
        sp.getString(NameOfResources.GROUP_ID.toString(), "");
        sp.getString(NameOfResources.GROUP_FACULTY_ID.toString(), "");

    }

}
