package com.coderstory.Purify.module;

import android.content.ComponentName;

import com.coderstory.Purify.plugins.IModule;
import com.coderstory.Purify.utils.XposedHelper;

import java.util.ArrayList;
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
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        if (lpparam.packageName.equals("com.miui.home")) {
            findAndHookMethod("com.miui.home.launcher.LauncherProvider", lpparam.classLoader, "isSkippedItem", ComponentName.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    ComponentName componentName = (ComponentName) param.args[0];
                    List<String> list = new ArrayList<>();
                    list.add("com.android.chrome");
                    list.add("org.adaway");
                    if (list.contains(componentName.getPackageName())) {
                        param.setResult(true);
                    }
                }
            });
        };
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {

    }
}
