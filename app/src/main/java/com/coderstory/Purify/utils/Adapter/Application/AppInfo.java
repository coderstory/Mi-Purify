package com.coderstory.Purify.utils.Adapter.Application;

import android.graphics.drawable.Drawable;

/**
 * Created by cc on 2015/12/11. 单个app info的模型
 */
public class AppInfo {

    public AppInfo() {
    }

    private String name;
    private Drawable imageId;
    private String AppDir;
    private boolean isDisable = true;
    private String packageName = "";
    private String Version="";
    private int VersionCode=0;
    public void setAppDir(String AppDir) {this.AppDir=AppDir;}
    public void setDisable(boolean disable) {
        this.isDisable = disable;
    }

    public Drawable getImageId() {
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
    public String getappdir() {
        return this.AppDir;
    }
    public boolean getDisable() {
        return isDisable;
    }

    public AppInfo(String name, Drawable imageId, String packageName, boolean Disable, String appdir, String version,int VersionCode) {
        this.name = name;
        this.imageId = imageId;
        this.packageName = packageName;
        this.isDisable = Disable;
        this.AppDir = appdir;
        this.Version=version;
        this.VersionCode=VersionCode;
    }

    public int getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }
}
