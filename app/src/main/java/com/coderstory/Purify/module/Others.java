package com.coderstory.Purify.module;

import android.view.WindowManager;

import com.coderstory.Purify.plugins.IModule;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.coderstory.Purify.utils.FunctionModule.fixpcb;
import static com.coderstory.Purify.utils.MyConfig.ApplicationName;
import static com.coderstory.Purify.utils.MyConfig.SharedPreferencesName;


public class Others implements IModule {


    private static void findAndHookMethod(String p1, ClassLoader lpparam, String p2, Object... parameterTypesAndCallback) {
        try {
            XposedHelpers.findAndHookMethod(p1, lpparam, p2, parameterTypesAndCallback);

        } catch (Throwable localString3) {
            XposedBridge.log(localString3);
        }
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XSharedPreferences prefs = new XSharedPreferences(ApplicationName, SharedPreferencesName);
        prefs.makeWorldReadable();
        prefs.reload();

        //窗口权限 miui 8
        if (loadPackageParam.packageName.equals("android")) {

            if (prefs.getBoolean(fixpcb, false)) {
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
