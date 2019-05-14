package com.coderstory.purify.module;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.coderstory.purify.plugins.IModule;
import com.coderstory.purify.utils.XposedHelper;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
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
                        paramAnonymousMethodHookParam.setResult("gemini_global");
                    }
                }

                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam) {
                    if (paramAnonymousMethodHookParam.args[0].toString().equals("ro.product.mod_device")) {
                        paramAnonymousMethodHookParam.setResult("gemini_global");
                    }
                }
            });
            return;
        }


        //下载管理
        if (loadPackageParam.packageName.equals("com.android.providers.downloads.ui")) {
            if (prefs.getBoolean("enableDownload", false)) {
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "getAdButtonType", XC_MethodReplacement.returnConstant(0));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowAppSubject", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowExtraAd", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowRecommendInfo", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isStableShowActivateNotify", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "supportRank", XC_MethodReplacement.returnConstant(false));
            }
        }

        // 短信
        if (loadPackageParam.packageName.equals("com.android.mms")) {
            if (prefs.getBoolean("enableMMS", false)) {
                findAndHookMethod("com.android.mms.util.SmartMessageUtils", loadPackageParam.classLoader, "isMessagingTemplateAllowed", Context.class, XC_MethodReplacement.returnConstant(true));
                findAndHookMethod("com.android.mms.ui.SingleRecipientConversationActivity", loadPackageParam.classLoader, "showMenuMode", boolean.class, XC_MethodReplacement.returnConstant(null));
            }
        }

        // 音乐
        if (loadPackageParam.packageName.equals("com.miui.player")) {
            findAndHookMethod("com.miui.player.display.model.MediaData", loadPackageParam.classLoader, "toAdvertisment", XC_MethodReplacement.returnConstant(null));
            hookAllMethods("com.miui.player.display.view.cell.TopNewsAdListItemCell", loadPackageParam.classLoader, "onBindItem", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    ((LinearLayout) param.thisObject).setVisibility(View.GONE);
                }
            });
            hookAllMethods("com.miui.player.display.view.cell.AdRecommendImageItemCell", loadPackageParam.classLoader, "setAdInfo", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    ((LinearLayout) param.thisObject).setVisibility(View.GONE);
                }
            });
        }

        // 安全中心
        if (loadPackageParam.packageName.equals("com.miui.securitycenter")) {
            XposedBridge.log("w哦进来了");
            hookAllMethods("com.miui.securityscan.utils.e", loadPackageParam.classLoader, "aE", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("分发内");
                    XposedBridge.log("参数" + param.args[1]);
                    // if ("securityscan_homelist_cache".equals(param.args[1].toString())) {
                    param.setResult("");
                    // }
                }
            });
        }

    }
}


