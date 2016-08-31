package com.coderstory.Purify.plugins;


import com.coderstory.Purify.module.RemoveAds;
import com.coderstory.Purify.module.RemoveSearchBar;
import com.coderstory.Purify.module.ThemePather7;
import com.coderstory.Purify.module.ThemePather8;
import com.coderstory.Purify.module.isEnable;
import com.coderstory.Purify.module.miuiMusic;
import com.coderstory.Purify.module.miuiroot;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class start implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {

        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.Purify", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();

        if (prefs.getBoolean("enablemiuiRoot", false)) {
            new miuiroot().handleInitPackageResources(resparam);
        }

        if (prefs.getBoolean("RemoveSearchBar", false)) {

            new RemoveSearchBar().handleInitPackageResources(resparam);
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.Purify", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();

        new isEnable().handleLoadPackage(lpparam);

        if (prefs.getBoolean("enablemiuiRoot", false)) {
            new miuiroot().handleLoadPackage(lpparam);
        }

        if (prefs.getBoolean("enableBlockAD", false)) {
            new RemoveAds().handleLoadPackage(lpparam);
        }

        if (prefs.getBoolean("CreakMIUI7", false)) {
            new ThemePather7().handleLoadPackage(lpparam);
        }

        if (prefs.getBoolean("CreakMIUI8", false)) {
            new ThemePather8().handleLoadPackage(lpparam);
        }

        if (prefs.getBoolean("miuiMusicCustomization", false)) {
            new miuiMusic().handleLoadPackage(lpparam);
        }

    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.Purify", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (prefs.getBoolean("enableCrackTheme", false) && prefs.getBoolean("CreakMIUI8", false)) {
            new ThemePather8().initZygote(startupParam);
        }

    }
}
