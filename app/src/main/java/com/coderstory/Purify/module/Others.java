package com.coderstory.Purify.module;

import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import com.coderstory.Purify.plugins.IModule;

import java.util.Map;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class Others implements IModule {


    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.Purify", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();

        //安全中心
        if (loadPackageParam.packageName.equals("com.miui.securitycenter")) {

            if (prefs.getBoolean("enableadb", false)) {
                findAndHookMethod("com.miui.permcenter.install.c", loadPackageParam.classLoader, "isEnabled", XC_MethodReplacement.returnConstant(false));
              findAndHookMethod("com.miui.permcenter.install.c", loadPackageParam.classLoader, "dK", XC_MethodReplacement.returnConstant(true));
               findAndHookMethod("com.miui.permcenter.install.c", loadPackageParam.classLoader, "dw", XC_MethodReplacement.returnConstant(true));
               findAndHookMethod("com.miui.permcenter.install.AdbInstallVerifyActivity", loadPackageParam.classLoader, "c", Context.class,boolean.class, XC_MethodReplacement.returnConstant(false));
               // findAndHookMethod("com.miui.permcenter.install.c", loadPackageParam.classLoader, "dT", XC_MethodReplacement.returnConstant(true));
               // findAndHookMethod("com.miui.permcenter.install.AdbInstallActivity", loadPackageParam.classLoader, "v", String.class, XC_MethodReplacement.returnConstant(true));
                findAndHookMethod("com.miui.permcenter.install.i", loadPackageParam.classLoader, "onReceive", Context.class, Intent.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        return null;
                    }
                });

                findAndHookMethod("com.miui.permcenter.install.c", loadPackageParam.classLoader, "X",String.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        return null;
                    }
                });

                findAndHookMethod("com.miui.common.persistence.b", loadPackageParam.classLoader, "c", String.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        if (param.args[0].toString().equals("perm_adb_install_notify")) {
                            param.setResult(true);
                        }
                    }
                });


            }

//            findAndHookMethod("com.miui.securitycenter.SysAppProtActivity", loadPackageParam.classLoader, "c", Map.class, new XC_MethodReplacement() {
//
//                @Override
//                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
//                    return null;
//                }
//            });
//            findAndHookMethod("com.miui.securitycenter.SysAppProtActivity", loadPackageParam.classLoader, "lj", Map.class, new XC_MethodReplacement() {
//
//                @Override
//                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
//                    return null;
//                }
//            });

            return;

        }


        //窗口权限 miui 8
        if (loadPackageParam.packageName.equals("android")) {
            if (prefs.getBoolean("fixpcb", false)) {
                XposedHelpers.findAndHookMethod("com.android.server.policy.PhoneWindowManager", loadPackageParam.classLoader, "checkAddPermission", WindowManager.LayoutParams.class, int[].class, new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam) {
                        if ((Integer) paramAnonymousMethodHookParam.getResult() < 0) {
                            paramAnonymousMethodHookParam.setResult(0);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {

    }

    private static void findAndHookMethod(String p1, ClassLoader lpparam, String p2, Object... parameterTypesAndCallback) {
        try {
            XposedHelpers.findAndHookMethod(p1, lpparam, p2, parameterTypesAndCallback);

        } catch (Throwable localString3) {
            XposedBridge.log(localString3.toString());
        }
    }
}
