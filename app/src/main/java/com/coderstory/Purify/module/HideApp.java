package com.coderstory.Purify.module;

import android.content.ComponentName;

import com.coderstory.Purify.plugins.IModule;
import com.coderstory.Purify.utils.XposedHelper;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by CoderStory on 2017/10/20 0020.
 */

public class HideApp extends XposedHelper implements IModule {
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        final String value = prefs.getString("Hide_App_List", "");
        if (!value.equals("")) {
            final List<String> hideAppList = Arrays.asList(value.split(":"));
            if (loadPackageParam.packageName.equals("com.miui.home")) {
                XposedBridge.log("load config" + value);
                findAndHookMethod("com.miui.home.launcher.LauncherProvider", loadPackageParam.classLoader, "isSkippedItem", ComponentName.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        ComponentName componentName = (ComponentName) param.args[0];
                        if (hideAppList.contains(componentName.getPackageName())) {
                            XposedBridge.log("hide app " + componentName.getPackageName());
                            param.setResult(true);
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
