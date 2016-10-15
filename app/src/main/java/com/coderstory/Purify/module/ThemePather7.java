package com.coderstory.Purify.module;

import android.content.Context;

import com.coderstory.Purify.plugins.IModule;

import java.io.File;
import java.io.InputStream;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class ThemePather7 implements IModule {


    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        DRM();
        thmemHook(lpparam);
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {

    }

    private static void thmemHook(XC_LoadPackage.LoadPackageParam paramLoadPackageParam) {
        if (paramLoadPackageParam.packageName.equals("com.android.thememanager")) {

            patchCodeTwo("miui.resourcebrowser.view.ResourceOperationHandler", "isAuthorizedResource", XC_MethodReplacement.returnConstant(true));

            patchCodeTwo("miui.resourcebrowser.controller.online.NetworkHelper", "validateResponseResult", Integer.TYPE, InputStream.class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    paramAnonymousMethodHookParam.setResult( paramAnonymousMethodHookParam.args[1]);
                }
            });

            patchCodeTwo("miui.resourcebrowser.controller.online.DrmService", "isLegal", "miui.resourcebrowser.model.Resource", new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    return true;
                }
            });

            patchCodeTwo("miui.resourcebrowser.view.ResourceOperationHandler", "isLegal", new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    return true;
                }
            });

            patchCodeTwo("miui.resourcebrowser.view.ResourceOperationHandler", "onCheckResourceRightEventBeforeRealApply", new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    XposedHelpers.setBooleanField(paramAnonymousMethodHookParam.thisObject, "mIsLegal", true);
                }
            });

            patchCodeTwo("miui.resourcebrowser.model.Resource", "isProductBought", XC_MethodReplacement.returnConstant(true));

            patchCodeTwo("miui.resourcebrowser.model.ResourceOnlineProperties", "setProductBought", Boolean.TYPE, new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    Object[] arrayOfObject = new Object[1];
                    arrayOfObject[0] = true;
                    paramAnonymousMethodHookParam.args = arrayOfObject;
                    return XposedBridge.invokeOriginalMethod(paramAnonymousMethodHookParam.method, paramAnonymousMethodHookParam.thisObject, arrayOfObject);
                }
            });

            patchCodeTwo("miui.resourcebrowser.model.ResourceOnlineProperties", "isProductBought", XC_MethodReplacement.returnConstant(true));

        }
    }

    //主题-DRM验证
    private static void DRM() {
        patchCodeTwo("miui.drm.DrmManager", "isLegal", Context.class, String.class, "miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isLegal", Context.class, File.class, File.class, XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isLegal", Context.class, String.class, File.class, XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isPermanentRights", "miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isPermanentRights", File.class, XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isLegal", File.class, File.class, XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isLegal", String.class, File.class, XC_MethodReplacement.returnConstant(true));
    }


    //patch函数
    private static void patchCodeTwo(String paramString1, String paramString2, Object... paramVarArgs) {
        try {
            XposedHelpers.findAndHookMethod(Class.forName(paramString1), paramString2, paramVarArgs);
        } catch (ClassNotFoundException paramString3) {
            XposedBridge.log(paramString3.toString());
        } catch (NoSuchMethodError paramString4) {
            XposedBridge.log(paramString4.toString());
        }
    }

    //log记录
    private static void log(String paramString) {
        XposedBridge.log("assistant  " + paramString);
    }


}
