package com.coderstory.Purify.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * Created by Baby Song on 2016/10/18.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

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

    protected SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = prefs.edit();
        }
        return editor;

    }

    protected SharedPreferences getPrefs() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            prefs = getBaseContext().getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
        } else {
            prefs = getBaseContext().getSharedPreferences("UserSettings", Context.MODE_WORLD_READABLE);
        }

        return prefs;
    }

}
