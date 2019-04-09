package com.coderstory.purify.module;


import com.coderstory.purify.plugins.IModule;
import com.coderstory.purify.utils.XposedHelper;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.coderstory.purify.config.Misc.ApplicationName;


public class IsEnable extends XposedHelper implements IModule {



    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals(ApplicationName)) {
            findAndHookMethod("com.coderstory.purify.activity.MainActivity", lpparam.classLoader, "isEnable", XC_MethodReplacement.returnConstant(true));
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam paramStartupParam) {
    }

}
