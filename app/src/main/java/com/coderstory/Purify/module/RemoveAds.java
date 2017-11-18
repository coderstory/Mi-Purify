package com.coderstory.Purify.module;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;

import com.coderstory.Purify.plugins.IModule;
import com.coderstory.Purify.utils.XposedHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class RemoveAds extends XposedHelper implements IModule {

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {

        if (resparam.packageName.equals("com.miui.cleanmaster")) {

            if (prefs.getBoolean("EnableSafeCenter", false)) {
                resparam.res.setReplacement(resparam.packageName, "string", "no_network", "");
            }
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        if (!prefs.getBoolean("EnableBlockAD", false)) {
            return;
        }
        //核心模块
        if (loadPackageParam.packageName.equals("com.miui.core")) {
            findAndHookMethod("miui.os.SystemProperties", loadPackageParam.classLoader, "get", String.class, String.class, new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    if (paramAnonymousMethodHookParam.args[0].toString().equals("ro.product.mod_device")) {
                        paramAnonymousMethodHookParam.setResult("gemini_global");
                    }
                }

                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    if (paramAnonymousMethodHookParam.args[0].toString().equals("ro.product.mod_device")) {
                        paramAnonymousMethodHookParam.setResult("gemini_global");
                    }
                }
            });
            return;
        }

        //下载管理
        if (loadPackageParam.packageName.equals("com.android.providers.downloads.ui")) {
            if (prefs.getBoolean("EnableDownload", false)) {
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "getAdButtonType", XC_MethodReplacement.returnConstant(0));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowAppSubject", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowExtraAd", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowRecommendInfo", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isStableShowActivateNotify", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "supportRank", XC_MethodReplacement.returnConstant(false));
            }
        }

        //个性主题
        if (loadPackageParam.packageName.equals("com.android.thememanager")) {
            if (prefs.getBoolean("EnableTheme", false)) {
                findAndHookMethod("com.android.thememanager.model.AdInfo", loadPackageParam.classLoader, "parseAdInfo", String.class, XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.android.thememanager.model.AdInfo", loadPackageParam.classLoader, "isSupported", "com.android.thememanager.model.AdInfo", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.thememanager.view.AdBannerView", loadPackageParam.classLoader, "showAdMark", new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.args[0] = false;
                    }
                });

                Class<?> clsPageItem = XposedHelpers.findClass("com.android.thememanager.model.PageItem", loadPackageParam.classLoader);
                if (clsPageItem != null) {
                    findAndHookMethod("com.android.thememanager.controller.online.PageItemViewConverter", loadPackageParam.classLoader, "buildAdView", clsPageItem, XC_MethodReplacement.returnConstant(null));
                }

                //*********//

                findAndHookMethod("com.android.thememanager.e.a", loadPackageParam.classLoader, "parseAdInfo", String.class, XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.android.thememanager.e.a", loadPackageParam.classLoader, "isSupported", "com.android.thememanager.e.a", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.thememanager.view.b", loadPackageParam.classLoader, "c", new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.args[0] = false;
                    }
                });

                clsPageItem = XposedHelpers.findClass("com.android.thememanager.e.h", loadPackageParam.classLoader);
                if (clsPageItem != null) {
                    findAndHookMethod("com.android.thememanager.a.b.m", loadPackageParam.classLoader, "k", clsPageItem, XC_MethodReplacement.returnConstant(null));
                }
                return;
            }
        }

        // 短信
        if (loadPackageParam.packageName.equals("com.android.mms")) {
            if (prefs.getBoolean("EnableMMS", false)) {

                findAndHookMethod("com.android.mms.util.SmartMessageUtils", loadPackageParam.classLoader, "isMessagingTemplateAllowed", Context.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        Context mc = (Context) paramAnonymousMethodHookParam.args[0];
                        if (mc.getClass().getName().toLowerCase().contains("app")) {
                            paramAnonymousMethodHookParam.setResult(false);
                        } else {
                            paramAnonymousMethodHookParam.setResult(true);
                        }
                    }
                });
                findAndHookMethod("com.android.mms.ui.SingleRecipientConversationActivity", loadPackageParam.classLoader, "showMenuMode", boolean.class, XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.android.mms.util.MiStatSdkHelper", loadPackageParam.classLoader, "recordBottomMenuShown", String.class, XC_MethodReplacement.returnConstant(null));
            }
        }
    }

}


