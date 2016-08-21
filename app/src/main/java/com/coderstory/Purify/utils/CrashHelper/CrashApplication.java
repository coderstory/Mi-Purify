package com.coderstory.Purify.utils.CrashHelper;

/**
 * Created by Baby Song on 2016/8/18.
 */

import android.app.Application;

public class CrashApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
