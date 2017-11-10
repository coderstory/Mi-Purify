package com.coderstory.Purify.adapter;

import android.graphics.drawable.Drawable;

public class AppInfo {

    private String name;
    private Drawable imageId;
    private boolean isDisable = true;
    private String packageName = "";
    private String Version = "";
    private String AppDir;
    private int VersionCode = 0;

    public AppInfo() {

    }

    public AppInfo(String name, Drawable imageId, String packageName, boolean Disable, String version) {
        this.name = name;
        this.imageId = imageId;
        this.packageName = packageName;
        this.isDisable = Disable;
        this.Version = version;
    }

    public AppInfo(String name, Drawable imageId, String packageName, boolean Disable, String appDir, String version, int VersionCode) {
        this.name = name;
        this.imageId = imageId;
        this.packageName = packageName;
        this.isDisable = Disable;
        this.setAppDir(appDir);
        this.Version = version;
        this.setVersionCode(VersionCode);
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

    public boolean getDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        this.isDisable = disable;
    }

    public String getAppDir() {
        return AppDir;
    }

    public void setAppDir(String appDir) {
        AppDir = appDir;
    }

    public int getVersionCode() {
        return VersionCode;
    }

    private void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }
}
