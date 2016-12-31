package com.coderstory.Purify.plugins;


import com.coderstory.Purify.module.Others;
import com.coderstory.Purify.module.RemoveAds;
import com.coderstory.Purify.module.RemoveSearchBar;
import com.coderstory.Purify.module.ThemePather8;
import com.coderstory.Purify.module.isEnable;
import com.coderstory.Purify.module.MiuiMusicCustomization;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.coderstory.Purify.utils.MyConfig.ApplicationName;
import static com.coderstory.Purify.utils.MyConfig.SharedPreferencesName;


public class start implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.Purify", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (prefs.getBoolean("RemoveSearchBar", false)) {
            new RemoveSearchBar().handleInitPackageResources(resparam);
        }
    }
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XSharedPreferences prefs = new XSharedPreferences(ApplicationName, SharedPreferencesName);
        prefs.makeWorldReadable();
        prefs.reload();
        new isEnable().handleLoadPackage(lpparam);
        if (prefs.getBoolean("enableBlockAD", false)) {
            new RemoveAds().handleLoadPackage(lpparam);
        }
        new ThemePather8().handleLoadPackage(lpparam);
        if (prefs.getBoolean("MiuiMusicCustomization", false)) {
            new MiuiMusicCustomization().handleLoadPackage(lpparam);
        }
        new Others().handleLoadPackage(lpparam);
    }
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
            new ThemePather8().initZygote(startupParam);
    }
}
