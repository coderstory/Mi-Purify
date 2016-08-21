package com.coderstory.Purify.module;

import com.coderstory.Purify.plugins.IModule;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Baby Song on 2016/8/17.
 */

public class RemoveSearchBar implements IModule {
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {

            if (resparam.packageName.equals("com.android.systemui")) {

                resparam.res.setReplacement(resparam.packageName, "bool", "config_show_statusbar_search", false);
            }

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {

    }
}
