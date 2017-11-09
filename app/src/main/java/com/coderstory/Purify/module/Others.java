package com.coderstory.Purify.module;

import android.view.WindowManager;

import com.coderstory.Purify.plugins.IModule;
import com.coderstory.Purify.utils.XposedHelper;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class Others extends XposedHelper implements IModule {


    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        //窗口权限 miui 8
        if (loadPackageParam.packageName.equals("android")) {

            if (prefs.getBoolean("fixpcb", false)) {
                findAndHookMethod("com.android.server.policy.PhoneWindowManager", loadPackageParam.classLoader, "checkAddPermission", WindowManager.LayoutParams.class, int[].class, new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam) {
                        if ((Integer) paramAnonymousMethodHookParam.getResult() < 0) {
                            paramAnonymousMethodHookParam.setResult(0);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
    }

}
