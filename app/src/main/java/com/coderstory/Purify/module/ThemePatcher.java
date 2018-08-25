package com.coderstory.Purify.module;

import android.content.Context;

import com.coderstory.Purify.plugins.IModule;
import com.coderstory.Purify.utils.XposedHelper;

import java.io.File;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.coderstory.Purify.config.Misc.isEnable;

public class ThemePatcher extends XposedHelper implements IModule {

    private final String[] CLASSES = new String[]{"ThemeOperationHandler", "ds", "du","ek"};
    private final String Base_Package = "com.android.thememanager.util";

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        if (!isEnable()) {
            return;
        }
        if ((lpparam.packageName.equals("miui.drm")) || (lpparam.packageName.equals("com.miui.system")) || (lpparam.packageName.equals("miui.system"))) {
            this.MIUI_DRM();
        }

        if (lpparam.packageName.equals("com.android.thememanager")) {
            for (String aClass : CLASSES) {
                try {
                    XposedHelpers.findClass(Base_Package + aClass, lpparam.classLoader);
                    patch(lpparam, Base_Package + aClass);

                } catch (XposedHelpers.ClassNotFoundError e) {
                }
            }
            // 修改为已购买
            CorePatch.findAndHookMethod("com.android.thememanager.e.p", lpparam.classLoader, "isProductBought", XC_MethodReplacement.returnConstant(true));
            CorePatch.findAndHookMethod("com.android.thememanager.e.s", lpparam.classLoader, "isProductBought", XC_MethodReplacement.returnConstant(true));
            CorePatch.findAndHookMethod("com.android.thememanager.e.v", lpparam.classLoader, "isProductBought", XC_MethodReplacement.returnConstant(true));
            CorePatch.findAndHookMethod("com.android.thememanager.util.dv", lpparam.classLoader, "k", XC_MethodReplacement.returnConstant(true));
        }
    }

    private void patch(XC_LoadPackage.LoadPackageParam lpparam, String classStr) {
        if (!isEnable()) {
            return;
        }
        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "isTrialable", XC_MethodReplacement.returnConstant(true));
        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "v", XC_MethodReplacement.returnConstant(true));

        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "isLegal", XC_MethodReplacement.returnConstant(true));
        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "a", XC_MethodReplacement.returnConstant(true));
        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "C", XC_MethodReplacement.returnConstant(true));
        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "isAuthorizedResource", XC_MethodReplacement.returnConstant(true));
        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "k", XC_MethodReplacement.returnConstant(true));

        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "isPermanentRights", XC_MethodReplacement.returnConstant(true));
        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "x", XC_MethodReplacement.returnConstant(true));
        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "w", XC_MethodReplacement.returnConstant(true));

        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "y", XC_MethodReplacement.returnConstant(true));

        //  尝试修改主题价格
        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "l", XC_MethodReplacement.returnConstant(0));
        CorePatch.findAndHookMethod(classStr, lpparam.classLoader, "G", XC_MethodReplacement.returnConstant(0));
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
        MIUI_DRM();
    }

    private void MIUI_DRM() {
        CorePatch.findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[]{Context.class, File.class, File.class, XC_MethodReplacement.returnConstant(CorePatch.getDrmResultSUCCESS())});
        CorePatch.findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[]{Context.class, String.class, File.class, XC_MethodReplacement.returnConstant(CorePatch.getDrmResultSUCCESS())});
        CorePatch.findAndHookMethod("miui.drm.DrmManager", "isPermanentRights", new Object[]{File.class, XC_MethodReplacement.returnConstant(true)});
    }
}