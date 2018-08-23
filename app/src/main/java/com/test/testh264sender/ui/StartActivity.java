package com.test.testh264sender.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.test.testh264sender.R;
import com.test.testh264sender.ui.LaifengLivingActivity;
import com.test.testh264sender.ui.LaifengScreenRecordActivity;

/**
 * Created by xu.wang
 * Date on  2018/5/28 09:41:00.
 *
 * @Desc
 */

public class StartActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private AppCompatButton btn_living, btn_record;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initialView();
        initialUdpServer();
    }


    private void initialView() {
        btn_living = findViewById(R.id.btn_test_living);
        btn_record = findViewById(R.id.btn_test_record);
        mSwipeRefreshLayout = findViewById(R.id.srf_refresh);
        recyclerView = findViewById(R.id.rv_choice_ip);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        btn_living.setOnClickListener(this);
        btn_record.setOnClickListener(this);
    }

    private void initialUdpServer() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test_living:
                Intent livingIntent = new Intent(this, LaifengLivingActivity.class);
                startActivity(livingIntent);
                break;
            case R.id.btn_test_record:
                Intent intent = new Intent(this, LaifengScreenRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {

    }
}
