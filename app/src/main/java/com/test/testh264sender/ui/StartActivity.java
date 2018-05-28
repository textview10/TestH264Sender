package com.test.testh264sender.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton btn_living, btn_record;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initialView();
    }

    private void initialView() {
        btn_living = findViewById(R.id.btn_test_living);
        btn_record = findViewById(R.id.btn_test_record);

        btn_living.setOnClickListener(this);
        btn_record.setOnClickListener(this);
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
}
