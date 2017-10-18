package com.coderstory.Purify.config;

import android.os.Environment;

public class Misc {
    public static final String BackPath = Environment.getExternalStorageDirectory().getPath() + "/MIUI_Purify/Backup/";
    public static final String MyBlogUrl = "http://blog.coderstory.cn";
    public static final String BackUpFileName = Environment.getExternalStorageDirectory().getPath() + "/MIUI_Purify/Backup/";
    public static final String ApplicationName = "com.coderstory.Purify";
    public static final String SharedPreferencesName = "UserSettings";
    public static final String HostFileTmpName = "/hosts";
    public static boolean isProcessing = false;
}
