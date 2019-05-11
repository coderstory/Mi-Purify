package com.coderstory.purify.plugins;

import java.lang.reflect.InvocationTargetException;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public interface IModule {
    void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException;

    void initZygote(IXposedHookZygoteInit.StartupParam startupParam);
}
