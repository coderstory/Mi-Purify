package com.coderstory.Purify.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.coderstory.Purify.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import static com.coderstory.Purify.utils.root.SuHelper.canRunRootCommands;

public class MyAppIntro extends AppIntro {
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    protected SharedPreferences.Editor getEditor() {
        getPrefs();
        if (editor == null) {
            editor = prefs.edit();
        }
        return editor;

    }

      protected SharedPreferences getPrefs() {
        if (prefs == null) {

            prefs = getApplicationContext().getSharedPreferences("UserSettings", Context.MODE_WORLD_READABLE);
        }
        return prefs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getPrefs().getBoolean("showGuide", true)) {
            addSlide(AppIntroFragment.newInstance("欢迎使用!", "欢迎使用本软件！\n本软件可是屌丝宅男帅哥美女必备的呀~\n", R.drawable.ic_slide1, Color.parseColor("#098c01")));
            addSlide(AppIntroFragment.newInstance("电话权限", "软件在提交应用奔溃数据的时候需要读取IMEI信息.\n", R.drawable.ic_slide1, Color.parseColor("#098c01")));
            addSlide(AppIntroFragment.newInstance("存储权限", "软件更新的时候需要下载新版本的APP到存储器中. \n", R.drawable.ic_slide2, Color.parseColor("#098c01")));
            addSlide(AppIntroFragment.newInstance("ROOT权限", "软件的人Hosts功能和去感叹号需要ROOT权限才能使用. \n", R.drawable.ic_slide2, Color.parseColor("#098c01")));
            addSlide(AppIntroFragment.newInstance("使用须知!", "软件的正常运行，你需要确保MIUI系统未安装PCB软件（变色状态栏）之类的不兼容软件. \n", R.drawable.ic_slide3, Color.parseColor("#098c01")));
            addSlide(AppIntroFragment.newInstance("使用须知!", "你需要确保Xposed安装器和本软件的自启权限不被干扰，否则会导致本软件功能异常和软件更新后失效。 \n", R.drawable.ic_slide3, Color.parseColor("#098c01")));
            addSlide(AppIntroFragment.newInstance("设置完毕!", "Enjoy Yourself! ", R.drawable.ic_slide4, Color.parseColor("#098c01")));

            // Ask Camera permission in the second slide
            askForPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 2);

            // Ask Storage permission in the third slide
            askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);

            // Ask Location permission in the fifth slide
            // askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

            //禁用skip
            showSkipButton(false);
            //允许滑动
            setNextPageSwipeLock(false);
            setSwipeLock(false);
            setDoneText("完成");
        }else{
            SplashActivity();
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        getEditor().putBoolean("showGuide",false);
        getEditor().apply();
        loadMainActivity();
    }


    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
       // Toast.makeText(getApplicationContext(), getString(R.string.skip), Toast.LENGTH_SHORT).show();

    }
    int count=0;
    @Override
    public void onSlideChanged(Fragment oldFragment, Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

        if (count == 4) {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    canRunRootCommands();
                }
            }.start();
        }
        count++;
    }

    public void getStarted(View v) {
        loadMainActivity();
    }

    void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    void SplashActivity() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        this.finish();
    }
}
