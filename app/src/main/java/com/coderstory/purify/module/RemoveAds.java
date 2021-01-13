package com.coderstory.purify.module;

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
                // isShouldShowAppSubject
                findAndHookMethod("com.android.providers.downloads.ui.utils.g", loadPackageParam.classLoader, "r", XC_MethodReplacement.returnConstant(false));
                //isShouldShowExtraAd
                findAndHookMethod("com.android.providers.downloads.ui.utils.g", loadPackageParam.classLoader, "t", XC_MethodReplacement.returnConstant(false));
                // isStableShowActivateNotify
                findAndHookMethod("com.android.providers.downloads.ui.utils.g", loadPackageParam.classLoader, "n", XC_MethodReplacement.returnConstant(false));
                //supportRank
                findAndHookMethod("com.android.providers.downloads.ui.utils.g", loadPackageParam.classLoader, "h", XC_MethodReplacement.returnConstant(false));
            }
        }

        //个性主题
        if (loadPackageParam.packageName.equals("com.android.thememanager") && prefs.getBoolean("EnableTheme", true)) {
            if (findClassWithOutLog("com.android.thememanager.basemodule.ad.model.AdInfoResponse", loadPackageParam.classLoader) != null) {
                hookAllMethods("com.android.thememanager.basemodule.ad.model.AdInfoResponse", loadPackageParam.classLoader, "isAdValid", XC_MethodReplacement.returnConstant(false));
                hookAllMethods("com.android.thememanager.basemodule.ad.model.AdInfoResponse", loadPackageParam.classLoader, "checkAndGetAdInfo", XC_MethodReplacement.returnConstant(null));
            }
        }

        // 全局mistatistic
        /**if (findClassWithOutLog("com.xiaomi.mistatistic.sdk.BuildSetting",loadPackageParam.classLoader)!=null){
         findAndHookMethod("com.xiaomi.mistatistic.sdk.BuildSetting", loadPackageParam.classLoader, "isInternationalBuild", XC_MethodReplacement.returnConstant(true));
         findAndHookMethod("com.xiaomi.mistatistic.sdk.BuildSetting", loadPackageParam.classLoader, "isCTABuild", XC_MethodReplacement.returnConstant(true));
         findAndHookMethod("com.xiaomi.mistatistic.sdk.BuildSetting", loadPackageParam.classLoader, "isDisabled", XC_MethodReplacement.returnConstant(true));
         findAndHookMethod("com.xiaomi.mistatistic.sdk.MiStatInterface", loadPackageParam.classLoader, "enableStatistics", new XC_MethodHook() {
        @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        param.args[0]=false;
        }
        });
         }**/
    }
}


