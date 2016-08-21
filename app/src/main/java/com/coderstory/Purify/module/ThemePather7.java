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

    private static ClassLoader mClassLoader;
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.log("开始iui7主题破解");
        mClassLoader = lpparam.classLoader;
        DRM();
        thmemHook(lpparam);
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {

    }
    //主题-入口
    private static void thmemHook(XC_LoadPackage.LoadPackageParam paramLoadPackageParam)
    {
        if (paramLoadPackageParam.packageName.equals("com.android.thememanager")) {
            zhifu();
            yanzheng();
            tongzhi();
        }
    }
    //主题-DRM验证
    private static void DRM() {

        patchCodeTwo("miui.drm.DrmManager", "isLegal",Context.class, String.class, "miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isLegal",Context.class, File.class, File.class, XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isLegal",Context.class, String.class, File.class, XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isPermanentRights", "miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isPermanentRights",File.class, XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isLegal",File.class, File.class, XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isLegal",String.class, File.class, XC_MethodReplacement.returnConstant(true));
    }
    //主题-本地验证
    private static void yanzheng() {
        try {
            Class.forName("miui.resourcebrowser.controller.online.DrmService", false, mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.controller.online.DrmService", mClassLoader, "isLegal", "miui.resourcebrowser.model.Resource", new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    return true;
                }
            });
            try {

                Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false, mClassLoader);
                XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler", mClassLoader, "isLegal",new XC_MethodReplacement() {
                    protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        return true;
                    }
                });
                try {

                    Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false, mClassLoader);
                    XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler", mClassLoader, "onCheckResourceRightEventBeforeRealApply", new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                                throws Throwable {
                            XposedHelpers.setBooleanField(paramAnonymousMethodHookParam.thisObject, "mIsLegal", true);
                        }
                    });

                } catch (NoSuchMethodError | ClassNotFoundException localNoSuchMethodError1) {
                    log(localNoSuchMethodError1.getMessage());
                }
            } catch (NoSuchMethodError | ClassNotFoundException localClassNotFoundException2) {
                log(localClassNotFoundException2.getMessage());
            }
        } catch (NoSuchMethodError | ClassNotFoundException localNoSuchMethodError3) {
            log(localNoSuchMethodError3.getMessage());

        }
    }
    //主题-设置为已购买
    private static void tongzhi() {
        try {
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.Resource", mClassLoader, "isProductBought", XC_MethodReplacement.returnConstant(true));
            try {
                XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.ResourceOnlineProperties", mClassLoader, "setProductBought", Boolean.TYPE, new XC_MethodReplacement() {
                    protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        Object[] arrayOfObject = new Object[1];
                        arrayOfObject[0] = true;
                        paramAnonymousMethodHookParam.args = arrayOfObject;
                        return XposedBridge.invokeOriginalMethod(paramAnonymousMethodHookParam.method, paramAnonymousMethodHookParam.thisObject, arrayOfObject);
                    }
                });
                try {
                    XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.ResourceOnlineProperties", mClassLoader, "isProductBought", XC_MethodReplacement.returnConstant(true));
                } catch (NoSuchMethodError | Exception localNoSuchMethodError1) {

                    log(localNoSuchMethodError1.getMessage());
                }
            } catch (NoSuchMethodError | Exception localNoSuchMethodError2) {
                log(localNoSuchMethodError2.getMessage());
            }
        } catch (NoSuchMethodError | Exception localNoSuchMethodError3) {
            log(localNoSuchMethodError3.getMessage());
        }
    }
    //主题-在线验证
    private static void zhifu() {
        try {
            Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false, mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler", mClassLoader, "isAuthorizedResource", XC_MethodReplacement.returnConstant(true));
            try {

                Class.forName("miui.resourcebrowser.controller.online.NetworkHelper", false, mClassLoader);
                XposedHelpers.findAndHookMethod("miui.resourcebrowser.controller.online.NetworkHelper", mClassLoader, "validateResponseResult",Integer.TYPE, InputStream.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult((InputStream) paramAnonymousMethodHookParam.args[1]);
                    }
                });
            } catch (NoSuchMethodError | ClassNotFoundException localNoSuchMethodError1) {
                log(localNoSuchMethodError1.getMessage());
            }
        } catch (NoSuchMethodError | ClassNotFoundException localNoSuchMethodError2) {
            log(localNoSuchMethodError2.getMessage());
        }
    }

    //patch函数
    private static void patchCodeTwo(String paramString1, String paramString2, Object... paramVarArgs) {
        try {
            XposedHelpers.findAndHookMethod(Class.forName(paramString1), paramString2, paramVarArgs);
        } catch (ClassNotFoundException paramString3) {
            log(" Class "+paramString1+"not found! Skipping...");
        } catch (NoSuchMethodError paramString4) {
            log(" Method "+paramString2+"found! Skipping...");
        }
    }
    //log记录
    private static void log(String paramString) {
        XposedBridge.log("assistant  " + paramString);
    }
}
