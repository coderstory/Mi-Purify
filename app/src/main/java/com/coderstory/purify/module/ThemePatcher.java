package com.coderstory.purify.module;

import android.content.Context;

import com.coderstory.purify.plugins.IModule;
import com.coderstory.purify.utils.XposedHelper;

import java.io.File;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.coderstory.purify.config.Misc.isEnable;

public class ThemePatcher extends XposedHelper implements IModule {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        if (!isEnable()) {
            return;
        }
        if (lpparam.packageName.equals("miui.drm")) {
            findAndHookMethod("miui.drm.DrmManager", lpparam.classLoader, "isLegal", Context.class, File.class, File.class, XC_MethodReplacement.returnConstant(getDrmResultSUCCESS()));
            findAndHookMethod("miui.drm.DrmManager", lpparam.classLoader, "isLegal", Context.class, String.class, File.class, XC_MethodReplacement.returnConstant(getDrmResultSUCCESS()));
            hookAllMethods("miui.drm.DrmManager", lpparam.classLoader, "getMorePreciseDrmResult", XC_MethodReplacement.returnConstant(getDrmResultSUCCESS()));
            findAndHookMethod("miui.drm.DrmManager", lpparam.classLoader, "isPermanentRights", File.class, XC_MethodReplacement.returnConstant(true));
        }

        if (lpparam.packageName.equals("com.android.thememanager")) {
            //1.6.2.0
            findAndHookMethod("com.android.thememanager.basemodule.resource.model.ResourceOnlineProperties", lpparam.classLoader, "isProductBought", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    int productPrice = XposedHelpers.getIntField(param.thisObject, "productPrice");
                    if (productPrice == 0) {
                        return XposedHelpers.getBooleanField(param.thisObject, "productBought");
                    } else {
                        return true;
                    }
                }
            });

            //isProductBought
            findAndHookMethod("com.android.thememanager.basemodule.resource.model.Resource", lpparam.classLoader, "isProductBought", XC_MethodReplacement.returnConstant(true));
            findAndHookMethod("com.android.thememanager.basemodule.resource.model.ResourceOnlineProperties", lpparam.classLoader, "isProductBought", XC_MethodReplacement.returnConstant(true));

            //isAuthorizedResource
            findAndHookMethod("com.android.thememanager.basemodule.resource.model.Resource", lpparam.classLoader, "isAuthorizedResource", XC_MethodReplacement.returnConstant(true));

            // return this.eV.getTrialTime() > 0; isProductBought isAuthorizedResource return this.eV.getTrialTime() > 0;
            //1.6.5.0
            findAndHookMethod("com.android.thememanager.util.ThemeOperationHandler", lpparam.classLoader, "r", XC_MethodReplacement.returnConstant(true));
            findAndHookMethod("com.android.thememanager.util.ThemeOperationHandler", lpparam.classLoader, "Q", XC_MethodReplacement.returnConstant(true));

            // stringBuilder.append("   check rights file: ");
            // if (new File(uVar.b()).getAbsolutePath().startsWith("/system")) {
            // if (new File(resourceResolver.getContentPath()).getAbsolutePath().startsWith("/system")) {
            findAndHookMethod("com.android.thememanager.g.a.e", lpparam.classLoader, "a", findClass("com.android.thememanager.basemodule.resource.model.Resource", lpparam.classLoader), XC_MethodReplacement.returnConstant(getDrmResultSUCCESS()));
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
        findAndHookMethod("miui.drm.DrmManager", null, "isLegal", Context.class, File.class, File.class, XC_MethodReplacement.returnConstant(getDrmResultSUCCESS()));
        findAndHookMethod("miui.drm.DrmManager", null, "isLegal", Context.class, String.class, File.class, XC_MethodReplacement.returnConstant(getDrmResultSUCCESS()));
        hookAllMethods("miui.drm.DrmManager", null, "getMorePreciseDrmResult", XC_MethodReplacement.returnConstant(getDrmResultSUCCESS()));
        findAndHookMethod("miui.drm.DrmManager", null, "isPermanentRights", File.class, XC_MethodReplacement.returnConstant(true));
    }
}