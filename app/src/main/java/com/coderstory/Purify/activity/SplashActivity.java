package com.coderstory.Purify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.coderstory.Purify.R;

public class SplashActivity extends Activity {

    private static final int SHOW_TIME_MIN = 1200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //倒计时返回主界面
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPostExecute(Integer result) {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            protected Integer doInBackground(Void... params) {
                long startTime = System.currentTimeMillis();
                long loadingTime = System.currentTimeMillis() - startTime;
                if (loadingTime < SHOW_TIME_MIN) {
                    try {
                        Thread.sleep(SHOW_TIME_MIN - loadingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return 1;
            }
        }.execute();
    }


}
