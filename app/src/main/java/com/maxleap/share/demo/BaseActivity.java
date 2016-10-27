package com.maxleap.share.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 */
public class BaseActivity extends AppCompatActivity{

    public static final String TITLE = "title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getIntent().getExtras() && getIntent().getExtras().containsKey(TITLE)) {
            Bundle extras = getIntent().getExtras();
            setTitle(extras.getString(TITLE));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
