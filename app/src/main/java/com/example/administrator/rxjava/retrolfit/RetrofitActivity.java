package com.example.administrator.rxjava.retrolfit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.rxjava.R;

/**
 * Created by Administrator on 2017/1/7.
 */

public class RetrofitActivity extends Activity {

    private TextView netmap;
    private TextView netflatmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
    }
}
