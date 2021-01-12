package com.coderstory.purify.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import java.io.File;
import java.lang.reflect.Field;

public class Utils {

    public static int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    public static SharedPreferences getMySharedPreferences(Context context, String dir, String fileName) {
        SharedPreferences result;
        try {
            result = context.getSharedPreferences(fileName, Context.MODE_WORLD_READABLE);
        } catch (SecurityException e) {

            try {
                // 获取 ContextWrapper对象中的mBase变量。该变量保存了 ContextImpl 对象
                Field field_mBase = ContextWrapper.class.getDeclaredField("mBase");
                field_mBase.setAccessible(true);
                // 获取 mBase变量
                Object obj_mBase = field_mBase.get(context);
                // 获取 ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
                Field field_mPreferencesDir = obj_mBase.getClass().getDeclaredField("mPreferencesDir");
                field_mPreferencesDir.setAccessible(true);
                // 创建自定义路径
                //  String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android";
                File file = new File(dir);
                // 修改mPreferencesDir变量的值
                field_mPreferencesDir.set(obj_mBase, file);
                // 返回修改路径以后的 SharedPreferences :%FILE_PATH%/%fileName%.xml
                //Log.e(TAG, "getMySharedPreferences filep=" + file.getAbsolutePath() + "| fileName=" + fileName);
                return context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException f) {
                f.printStackTrace();
            }
            //Log.e(TAG, "getMySharedPreferences end filename=" + fileName);
            // 返回默认路径下的 SharedPreferences : /data/data/%package_name%/shared_prefs/%fileName%.xml
            result = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return result;
    }
}
