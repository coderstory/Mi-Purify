package com.coderstory.Purify.adapter;

import android.graphics.drawable.Drawable;

/**
 * Created by cc on 2015/12/11. 单个app info的模型
 */
public class AppInfo {

//    public AppInfo() {
//    }

    private String name;
    private Drawable imageId;
    //private String AppDir;
    private boolean isDisable = true;
    private String packageName = "";
    private  String Version="";

    //public void setAppDir(String AppDir) {this.AppDir=AppDir;}
    public void setDisable(boolean disable) {
        this.isDisable = disable;
    }

    Drawable getImageId() {
        return this.imageId;
    }
    public String getVersion() {
        return this.Version;
    }
    public String getPackageName() {
        return packageName;
    }
    public String getName() {
        return this.name;
    }
//    public String getappdir() {
//        return this.AppDir;
//    }
    public boolean getDisable() {
        return isDisable;
    }

    public AppInfo(String name, Drawable imageId, String packageName, boolean Disable, String version) {
        this.name = name;
        this.imageId = imageId;
        this.packageName = packageName;
        this.isDisable = Disable;
       // this.AppDir = appdir;
        this.Version=version;
    }
}
