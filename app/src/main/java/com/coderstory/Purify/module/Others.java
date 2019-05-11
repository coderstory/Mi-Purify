package com.coderstory.purify.module;

import android.content.pm.PackageManager;
import android.view.View;

import com.coderstory.purify.plugins.IModule;
import com.coderstory.purify.utils.XposedHelper;

import java.util.List;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class Others extends XposedHelper implements IModule {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        if (getPrefs().getBoolean("prevent_freeze_reverse", false)) {
            if (loadPackageParam.packageName.equals("android") || loadPackageParam.packageName.equals("com.miui.system") || loadPackageParam.packageName.equals("miui.system")) {
                findAndHookMethod("com.miui.server.SecurityManagerService", loadPackageParam.classLoader, "checkSysAppCrack", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.server.SecurityManagerService", loadPackageParam.classLoader, "checkEnabled", PackageManager.class, String.class, XC_MethodReplacement.returnConstant(null));
                hookAllMethods("com.miui.server.SecurityManagerService", loadPackageParam.classLoader, "enforcePlatformSignature", XC_MethodReplacement.returnConstant(null));
            }
        }


        // 解锁默认商店
        if (loadPackageParam.packageName.equals("android") && getPrefs().getBoolean("default_store", false)) {
            findAndHookMethod("com.android.server.pm.PackageManagerServiceInjector", loadPackageParam.classLoader, "getMarketResolveInfo", List.class, XC_MethodReplacement.returnConstant(null));
        }

        if (loadPackageParam.packageName.equals("com.android.systemui")) {
            // 低电量警告
            if (getPrefs().getBoolean("lowBatteryWarning", false)) {
                findAndHookMethod("com.android.systemui.power.PowerUI", loadPackageParam.classLoader, "playLowBatterySound", XC_MethodReplacement.returnConstant(null));
            }
            // 隐藏闹钟图标
            if (getPrefs().getBoolean("alarmClockIcon", false)) {
                findAndHookMethod("com.android.systemui.statusbar.phone.PhoneStatusBarPolicy", loadPackageParam.classLoader, "updateAlarm", Boolean.TYPE, XC_MethodReplacement.returnConstant(false));
            }
            // 电量不足警告
            if (getPrefs().getBoolean("noBatteryWarning", false)) {
                findAndHookMethod("com.android.systemui.power.PowerUI", loadPackageParam.classLoader, "start", XC_MethodReplacement.returnConstant(false));
            }
            if (getPrefs().getBoolean("hideNetworkSpeed", false)) {
                // 网速小于100kb/s自动隐藏状态栏网速
                findAndHookMethod("com.android.systemui.statusbar.NetworkSpeedView", loadPackageParam.classLoader, "onTextChanged", CharSequence.class, int.class, int.class, int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                String speed = (String) param.args[0];
                                if (!"".equals(speed)) {
                                    Double speedInt = Double.valueOf(speed.replace("K/s", ""));
                                    if (speedInt < 10 && !(boolean) XposedHelpers.callMethod(param.thisObject, "isNotch")) {
                                        XposedHelpers.callMethod(param.thisObject, "setVisibility", View.GONE);

                                    } else if (!(boolean) XposedHelpers.callMethod(param.thisObject, "isNotch")) {
                                        XposedHelpers.callMethod(param.thisObject, "setVisibility", View.VISIBLE);
                                    }
                                }
                            }
                        });
                //  }
            }

            /**
             *    findAndHookMethod("com.android.systemui.statusbar.NetworkSpeedController", loadPackageParam.classLoader, "updateNetworkSpeed", new XC_MethodReplacement() {
             *                 @Override
             *                 protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
             *                     long currentTime = System.currentTimeMillis();
             *                     long totalBytes = (long) XposedHelpers.callMethod(param.thisObject,"getTotalByte");
             *                     long mLastTime = XposedHelpers.getLongField(param.thisObject,"mLastTime");
             *                     long mTotalBytes =XposedHelpers.getLongField(param.thisObject,"mTotalBytes");
            `     *
             *                     if (!(mLastTime == 0 || currentTime <= mLastTime || mTotalBytes == 0 || totalBytes == 0 || totalBytes <= mTotalBytes)) {
             *                         long currentSpeed = ((totalBytes - mTotalBytes) * 1000) / (currentTime - mLastTime);
             *                         if (currentSpeed/1024 >100){
             *
             *                         }
             *                     }
             *                 }
             *             });
             */

            if (loadPackageParam.packageName.equals("com.android.providers.downloads.ui") && getPrefs().getBoolean("downloadVip", false)) {
                findAndHookMethod("com.android.providers.downloads.ui.auth.AccountInfo", loadPackageParam.classLoader, "isVip", XC_MethodReplacement.returnConstant(Boolean.TRUE));
            }
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {

    }
}
