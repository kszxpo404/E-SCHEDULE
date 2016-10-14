package com.e_schedule.e_schedule;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }




}
