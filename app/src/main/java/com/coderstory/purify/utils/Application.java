package com.coderstory.purify.utils;


import java.lang.reflect.Field;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        try {
            Field v0_1 = ClassLoader.getSystemClassLoader()
                    .loadClass("de.robv.android.xposed.XposedBridge")
                    .getDeclaredField("disableHooks");
            v0_1.setAccessible(true);
            v0_1.set(null, true);
        } catch (Throwable v0) {
        }
    }

}
