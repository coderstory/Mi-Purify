package com.coderstory.Purify.plugins;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Baby Song on 2016/8/17.
 */

public interface IModule {
    void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam);

    void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam);

    void initZygote(IXposedHookZygoteInit.StartupParam startupParam);
}
