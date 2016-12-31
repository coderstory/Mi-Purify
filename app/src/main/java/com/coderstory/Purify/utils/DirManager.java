package com.coderstory.Purify.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.coderstory.Purify.utils.Adapter.Application.AppInfo;

import java.io.File;
import java.util.Vector;

import static com.coderstory.Purify.utils.MyConfig.BackPath;

/**
 * Created by cc on 2015/12/28.
 */
public class DirManager {
    public static Vector<String> apkAll = null;
    public static boolean needReload=false;
    // 获取当前目录下所有的mp4文件
    public static Vector<String> GetApkFileName(String fileAbsolutePath) {
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();

     if (subFile!=null){

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                // 判断是否为apk结尾
                if (filename.trim().toLowerCase().endsWith(".apk")) {
                    vecFile.add(filename);
                }
            }
        }}
        return vecFile;
    }


    public static PackageInfo loadAppInfo(String appPath, Activity content) {
        AppInfo app = new AppInfo();
        app.setAppDir(BackPath+ appPath);
       // Log.e("11",app.appdir);
        PackageManager pm = content.getPackageManager();
       // Log.e("22",app.appdir);
        PackageInfo info = pm.getPackageArchiveInfo(app.getappdir(), PackageManager.GET_ACTIVITIES);
        return info;
    }


}

