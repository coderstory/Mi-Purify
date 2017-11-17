package com.coderstory.Purify.module;

import android.content.pm.PackageManager;
import android.view.WindowManager;

import com.coderstory.Purify.plugins.IModule;
import com.coderstory.Purify.utils.XposedHelper;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
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

        if (prefs.getBoolean("prevent_freeze_reverse", false)) {
            if (loadPackageParam.packageName.equals("android") || loadPackageParam.packageName.equals("com.miui.system") || loadPackageParam.packageName.equals("miui.system")) {
                findAndHookMethod("com.miui.server.SecurityManagerService", loadPackageParam.classLoader, "checkSysAppCrack", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.server.SecurityManagerService", loadPackageParam.classLoader, "checkEnabled", PackageManager.class, String.class, XC_MethodReplacement.returnConstant(null));
                hookAllMethods("com.miui.server.SecurityManagerService", loadPackageParam.classLoader, "enforcePlatformSignature", XC_MethodReplacement.returnConstant(null));
            }
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
    }

}
