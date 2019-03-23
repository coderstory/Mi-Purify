package com.coderstory.purify.plugins;


import com.coderstory.purify.module.CorePatch;
import com.coderstory.purify.module.HideApp;
import com.coderstory.purify.module.IsEnable;
import com.coderstory.purify.module.MiuiHome;
import com.coderstory.purify.module.Others;
import com.coderstory.purify.module.RemoveAds;
import com.coderstory.purify.module.ThemePatcher;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class start implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        new IsEnable().handleLoadPackage(lpparam);
        new RemoveAds().handleLoadPackage(lpparam);
        new HideApp().handleLoadPackage(lpparam);
        new Others().handleLoadPackage(lpparam);
        new MiuiHome().handleLoadPackage(lpparam);
        new CorePatch().handleLoadPackage(lpparam);
        new ThemePatcher().handleLoadPackage(lpparam);
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        XposedBridge.log("小米净化 2.x 开始Patch");
        new CorePatch().initZygote(startupParam);
        new ThemePatcher().initZygote(startupParam);
    }
}
