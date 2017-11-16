package com.coderstory.Purify.plugins;


import com.coderstory.Purify.module.CorePatch;
import com.coderstory.Purify.module.HideApp;
import com.coderstory.Purify.module.IsEnable;
import com.coderstory.Purify.module.MiuiHome;
import com.coderstory.Purify.module.MiuiRoot;
import com.coderstory.Purify.module.Others;
import com.coderstory.Purify.module.RemoveAds;
import com.coderstory.Purify.module.RemoveSearchBar;
import com.coderstory.Purify.module.ThemePather8;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class start implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        new RemoveSearchBar().handleInitPackageResources(resparam);
        new MiuiRoot().handleInitPackageResources(resparam);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        new IsEnable().handleLoadPackage(lpparam);
        new RemoveAds().handleLoadPackage(lpparam);
        new HideApp().handleLoadPackage(lpparam);
        new Others().handleLoadPackage(lpparam);
        new MiuiRoot().handleLoadPackage(lpparam);
        new MiuiHome().handleLoadPackage(lpparam);
        new CorePatch().handleLoadPackage(lpparam);
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XposedBridge.log("小米净化 2.x 开始Patch");
        new ThemePather8().initZygote(startupParam);
        new CorePatch().initZygote(startupParam);
    }
}
