package com.coderstory.purify.module;

import android.content.Context;
import android.view.View;

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

        // 短信
        if (loadPackageParam.packageName.equals("com.android.mms")) {
            if (prefs.getBoolean("EnableMMS", false)) {
                findAndHookMethod("com.android.mms.util.SmartMessageUtils", loadPackageParam.classLoader, "isMessagingTemplateAllowed", Context.class, XC_MethodReplacement.returnConstant(true));
                findAndHookMethod("com.android.mms.ui.SingleRecipientConversationActivity", loadPackageParam.classLoader, "showMenuMode", boolean.class, XC_MethodReplacement.returnConstant(null));
            }
        }

        if (loadPackageParam.packageName.equals("com.miui.player")) {
            XposedBridge.log("我进来了");
            //   hookAllMethods("com.miui.unifiedAdSdk.UnifiedAdCache", loadPackageParam.classLoader, "get", XC_MethodReplacement.returnConstant(null));
            hookAllMethods("com.miui.player.display.view.cell.AdListItemCell", loadPackageParam.classLoader, "setAdInfo", XC_MethodReplacement.returnConstant(null));
             hookAllMethods("com.miui.player.display.view.cell.AdListItemCell", loadPackageParam.classLoader, "onBindItem", XC_MethodReplacement.returnConstant(null));
            hookAllMethods("com.miui.player.display.view.cell.AdItemCell", loadPackageParam.classLoader, "setAdInfo", XC_MethodReplacement.returnConstant(null));
            //hookAllMethods("com.miui.player.display.view.cell.AdItemCell", loadPackageParam.classLoader, "onBindItem", XC_MethodReplacement.returnConstant(null));
            hookAllMethods("com.miui.player.display.view.cell.AdRecommendImageItemCell", loadPackageParam.classLoader, "setAdInfo", XC_MethodReplacement.returnConstant(null));
            hookAllMethods("com.miui.player.display.view.cell.BannerAdItemCell", loadPackageParam.classLoader, "setAdInfo", XC_MethodReplacement.returnConstant(null));
            hookAllMethods("com.miui.player.display.view.cell.AdListItemCell", loadPackageParam.classLoader, "requestNewAd", XC_MethodReplacement.returnConstant(null));
            hookAllMethods("com.miui.player.display.view.cell.AdItemCell", loadPackageParam.classLoader, "requestNewAd", XC_MethodReplacement.returnConstant(null));
            hookAllConstructors("com.miui.player.display.view.cell.AdItemCell", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    View view = (View) param.thisObject;
                    view.setVisibility(View.GONE);
                    return null;
                }
            });
            hookAllConstructors("com.miui.player.display.view.cell.AdListItemCell", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    View view = (View) param.thisObject;
                    view.setVisibility(View.GONE);
                    return null;
                }
            });
            //    hookAllMethods("com.miui.player.util.SkinUtils", loadPackageParam.classLoader, "getSkinLocalInfo", XC_MethodReplacement.returnConstant(null));
//
//            findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "isAdEnable", XC_MethodReplacement.returnConstant(false));
            findAndHookMethod("com.miui.player.display.model.MediaData", loadPackageParam.classLoader, "toAdvertisment", XC_MethodReplacement.returnConstant(null));
//            findAndHookMethod("com.miui.player.display.view.LightNowplayingNewAlbumPage", loadPackageParam.classLoader, "isShowAlbumAd", XC_MethodReplacement.returnConstant(false));
//            findAndHookMethod("com.miui.player.display.view.LightNowplayingNewAlbumPage", loadPackageParam.classLoader, "infateAdMark", XC_MethodReplacement.returnConstant(false));
//            findAndHookMethod("com.miui.player.display.view.LightNowplayingAlbumPage", loadPackageParam.classLoader, "isShowAlbumAd", XC_MethodReplacement.returnConstant(false));
//            findAndHookMethod("com.miui.player.display.view.LightNowplayingAlbumPage", loadPackageParam.classLoader, "infateAdMark", XC_MethodReplacement.returnConstant(false));
//            findAndHookMethod("com.xiaomi.ad.entity.globalGuessULike.isShowAdMark", loadPackageParam.classLoader, "infateAdMark", XC_MethodReplacement.returnConstant(false));
//            findAndHookMethod("com.xiaomi.ad.entity.internalPreinstall.DesktopRecommendAdInfo", loadPackageParam.classLoader, "isShowAdMark", XC_MethodReplacement.returnConstant(false));
//            hookAllMethods("com.miui.player.hybrid.feature.GetAdInfo", loadPackageParam.classLoader, "getAdParams", XC_MethodReplacement.returnConstant(null));
//            findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "isDebug", XC_MethodReplacement.returnConstant(true));
//            hookAllMethods("com.miui.player.stat.AdvertiseTrackEvent", loadPackageParam.classLoader, "applyToAdvertisementStat", new XC_MethodReplacement() {
//                @Override
//                protected Object replaceHookedMethod(MethodHookParam param) {
//                    return null;
//                }
//            });
//            hookAllMethods("com.miui.player.view.core.AlbumObservable$NowplayingAdIm11ageLoader", loadPackageParam.classLoader, "loadAdImage", new XC_MethodReplacement() {
//                @Override
//                protected Object replaceHookedMethod(MethodHookParam param) {
//                    return null;
//                }
//            });
        }
    }

}


