package com.coderstory.Purify.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.coderstory.Purify.nohttp.CallServer;
import com.coderstory.Purify.nohttp.HttpListener;
import com.yolanda.nohttp.rest.Request;

/**
 * Created by Baby Song on 2016/10/18.
 */

public abstract class BaseActivity extends AppCompatActivity {
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

    protected abstract void setUpView();

    protected abstract int setLayoutResourceID();

    protected <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }


    protected void startActivityWithoutExtras(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void startActivityWithExtras(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(extras);
        startActivity(intent);

    }


    public <T> void request(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading) {
        request.setCancelSign(this);
        CallServer.getRequestInstance().add(this, what, request, callback, canCancel, isLoading);
    }

    @Override
    protected void onDestroy() {
        CallServer.getRequestInstance().cancelBySign(this);
        super.onDestroy();
    }

}
