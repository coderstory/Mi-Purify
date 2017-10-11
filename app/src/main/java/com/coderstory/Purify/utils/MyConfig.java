package com.coderstory.Purify.utils;

import android.os.Environment;

/**
 * Created by Baby Song on 2016/9/7.
 */

public class MyConfig {
    public static final String BackPath = Environment.getExternalStorageDirectory().getPath() + "/MIUI Purify/backupAPP/";
    public static final String MyBlogUrl = "http://blog.coderstory.cn";
    public static final String BackUpFileName = Environment.getExternalStorageDirectory().getPath() + "/MIUI Purify/backup/";
    public static final String ApplicationName = "com.coderstory.Purify";
    public static final String SharedPreferencesName = "UserSettings";
    public static final String HostFileTmpName = "/hosts";
    public static boolean isProcessing = false;
}
