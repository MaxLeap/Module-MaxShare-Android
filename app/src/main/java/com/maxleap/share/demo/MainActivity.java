package com.maxleap.share.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private Button btn_in_share;
    private Button btn_thrid_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_in_share = (Button) findViewById(R.id.btn_in_share);
        btn_thrid_share = (Button) findViewById(R.id.btn_thrid_share);
        btn_in_share.setOnClickListener(this);
        btn_thrid_share.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_thrid_share:
                goShare(ThridShareActivity.class,"第三方分享");
                break;
            case R.id.btn_in_share:
                goShare(InnerShareActivity.class,"应用内分享");
                break;
        }

    }


    private void goShare(Class clazz,String title) {
        Intent i = new Intent(this,clazz);
        i.putExtra(BaseActivity.TITLE,title);
        startActivity(i);
    }


}
