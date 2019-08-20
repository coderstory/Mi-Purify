package com.coderstory.purify.module;

import android.content.Context;

import com.coderstory.purify.plugins.IModule;
import com.coderstory.purify.utils.XposedHelper;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class RemoveAds extends XposedHelper implements IModule {

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        //核心模块
        if (loadPackageParam.packageName.equals("com.miui.core")) {
            findAndHookMethod("miui.os.SystemProperties", loadPackageParam.classLoader, "get", String.class, String.class, new XC_MethodHook() {

                // # System property for AD
                //ro.vendor.display.ad=1
                //ro.vendor.display.ad.sdr_calib_data=/vendor/etc/sdr_config.cfg
                //ro.vendor.display.ad.hdr_calib_data=/vendor/etc/hdr_config.cfg
                //ro.vendor.display.sensortype=2
                protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam) {
                    if (paramAnonymousMethodHookParam.args[0].toString().equals("ro.product.mod_device")) {
                        paramAnonymousMethodHookParam.setResult("cepheus_global");
                    }
                }

                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam) {
                    if (paramAnonymousMethodHookParam.args[0].toString().equals("ro.product.mod_device")) {
                        paramAnonymousMethodHookParam.setResult("cepheus_global");
                    }
                }
            });
            return;
        }


        //下载管理
        if (loadPackageParam.packageName.equals("com.android.providers.downloads.ui")) {
            if (prefs.getBoolean("enableDownload", true)) {
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "getAdButtonType", XC_MethodReplacement.returnConstant(0));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowAppSubject", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowExtraAd", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowRecommendInfo", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isStableShowActivateNotify", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "supportRank", XC_MethodReplacement.returnConstant(false));
            }
        }

        //个性主题
        if (loadPackageParam.packageName.equals("com.android.thememanager") && prefs.getBoolean("EnableTheme", true)) {
            findAndHookMethod("com.xiaomi.mistatistic.ad.d", loadPackageParam.classLoader, "b", String.class, XC_MethodReplacement.returnConstant(null));
            if (findClassWithOutLog("com.android.thememanager.basemodule.ad.model.AdInfoResponse", loadPackageParam.classLoader) != null) {
                hookAllMethods("com.android.thememanager.basemodule.ad.model.AdInfoResponse", loadPackageParam.classLoader, "isAdValid", XC_MethodReplacement.returnConstant(false));
                hookAllMethods("com.android.thememanager.basemodule.ad.model.AdInfoResponse", loadPackageParam.classLoader, "checkAndGetAdInfo", XC_MethodReplacement.returnConstant(null));
            }
        }

        // 全局mistatistic
        if (findClassWithOutLog("com.xiaomi.mistatistic.sdk.BuildSetting",loadPackageParam.classLoader)!=null){
            findAndHookMethod("com.xiaomi.mistatistic.sdk.BuildSetting", loadPackageParam.classLoader, "isInternationalBuild", XC_MethodReplacement.returnConstant(true));
            findAndHookMethod("com.xiaomi.mistatistic.sdk.BuildSetting", loadPackageParam.classLoader, "isCTABuild", XC_MethodReplacement.returnConstant(true));
            findAndHookMethod("com.xiaomi.mistatistic.sdk.BuildSetting", loadPackageParam.classLoader, "isDisabled", XC_MethodReplacement.returnConstant(true));
            findAndHookMethod("com.xiaomi.mistatistic.sdk.MiStatInterface", loadPackageParam.classLoader, "enableStatistics", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    param.args[0]=false;
                }
            });
        }
    }
}


