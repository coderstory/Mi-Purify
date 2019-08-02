package com.coderstory.purify.module;

import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.coderstory.purify.plugins.IModule;
import com.coderstory.purify.utils.XposedHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.util.Config.LOGD;

public class HideApp extends XposedHelper implements IModule {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        final String value = prefs.getString("Hide_App_List", "");
        if (!value.equals("")) {
            final List<String> hideAppList = Arrays.asList(value.split(":"));
            if (loadPackageParam.packageName.equals("com.miui.home")) {
                XposedBridge.log("load config" + value);
                findAndHookMethod("com.miui.home.launcher.LauncherProvider", loadPackageParam.classLoader, "loadSkippedItems", Context.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        HashSet<ComponentName> mSkippedItems = (HashSet<ComponentName>) XposedHelpers.getObjectField(param.thisObject, "mSkippedItems");
                        for (int i = 1; i < hideAppList.size(); i++) {
                            mSkippedItems.add(new ComponentName(hideAppList.get(i).split("&")[0], hideAppList.get(i).split("&")[1]));
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
