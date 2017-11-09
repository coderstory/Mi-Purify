package com.coderstory.Purify.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract void setUpView();

    protected abstract int setLayoutResourceID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(setLayoutResourceID());
        setUpView();
        setUpData();
    }

    protected void setUpData() {
    }

    /***
     * 用于在初始化View之前做一些事
     */
    protected void init() {

    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }
}
