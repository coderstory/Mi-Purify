package com.coderstory.Purify.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.coderstory.Purify.plugins.IModule;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.coderstory.Purify.utils.FunctionModule.*;
import static com.coderstory.Purify.utils.MyConfig.*;
import static com.coderstory.Purify.utils.packageNameEntries.*;


public class RemoveAds implements IModule {


    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
        XSharedPreferences prefs = new XSharedPreferences(ApplicationName, SharedPreferencesName);
        prefs.makeWorldReadable();
        prefs.reload();

        if (resparam.packageName.equals(cleanmaster_packageName)) {

            if (prefs.getBoolean(enableSafeCenter, false)) {
                resparam.res.setReplacement(resparam.packageName, "string", "no_network", "");
            }
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {



        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.Purify", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean(enableBlockAD, false)) {
            return ;
        }
        //核心模块
        if (loadPackageParam.packageName.equals(core_packageName)) {
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

        //垃圾清理
        if (loadPackageParam.packageName.equals("com.miui.cleanmaster")) {

            if (prefs.getBoolean(enableSafeCenter, false)) {
                findAndHookMethod("com.miui.optimizecenter.result.DataModel", loadPackageParam.classLoader, "post", Map.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult("");
                    }
                });
                findAndHookMethod("com.miui.optimizecenter.config.MiStat", loadPackageParam.classLoader, "getChannel", new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult("international");
                    }
                });
            }
        }
        if (loadPackageParam.packageName.equals("com.android.browser")) {
            findAndHookMethod("miui.browser.a.a", loadPackageParam.classLoader, "a", String.class, String.class, String.class, List.class, HashMap.class, XC_MethodReplacement.returnConstant(null));
            Class<?> clsA = XposedHelpers.findClass("com.a.a.d.a", loadPackageParam.classLoader);
            if (clsA != null) {
                findAndHookMethod("com.android.browser.suggestion.SuggestItem$AdsInfo", loadPackageParam.classLoader, "deserialize", clsA, XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.android.browser.homepage.HomepageBannerProvider$AdTrackingInfo", loadPackageParam.classLoader, "deserialize", clsA, XC_MethodReplacement.returnConstant(null));
            }
        }


        //日历
        if (loadPackageParam.packageName.equals(calendar_packageName)) {

            findAndHookMethod("com.miui.calendar.ad.AdService", loadPackageParam.classLoader, "onHandleIntent", Intent.class, XC_MethodReplacement.returnConstant(null));
            findAndHookMethod("com.miui.calendar.card.single.local.SummarySingleCard", loadPackageParam.classLoader, "needShowAdBanner", XC_MethodReplacement.returnConstant(false));
            findAndHookMethod("com.miui.calendar.card.single.custom.ad.AdSingleCard", loadPackageParam.classLoader, "needDisplay", XC_MethodReplacement.returnConstant(false));
            findAndHookMethod("com.miui.calendar.ad.AdUtils", loadPackageParam.classLoader, "getAdConfigJson", Context.class, XC_MethodReplacement.returnConstant(null));
            findAndHookMethod("com.miui.calendar.card.single.custom.RecommendSingleCard", loadPackageParam.classLoader, "needDisplay", XC_MethodReplacement.returnConstant(false));
            findAndHookMethod("com.miui.calendar.card.single.custom.ad.LargeImageAdSingleCard", loadPackageParam.classLoader, "needDisplay", XC_MethodReplacement.returnConstant(false));
            Class<?> clsC = XposedHelpers.findClass("com.xiaomi.ad.internal.common.module.a$c", loadPackageParam.classLoader);
            if (clsC != null) {
                findAndHookMethod("com.xiaomi.ad.internal.common.module.a", loadPackageParam.classLoader, "b", clsC, XC_MethodReplacement.returnConstant(null));
            }
            findAndHookMethod("com.xiaomi.ad.common.pojo.Ad", loadPackageParam.classLoader, "parseJson", JSONObject.class, XC_MethodReplacement.returnConstant(null));
            findAndHookMethod("com.xiaomi.ad.internal.a.e", loadPackageParam.classLoader, "onAdInfo", String.class, XC_MethodReplacement.returnConstant(null));
            findAndHookMethod("com.miui.calendar.util.DiskStringCache", loadPackageParam.classLoader, "getString", Context.class, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String key = (String) param.args[1];

                    if (key.startsWith("bottom_banner_is_closed_today")) {
                        param.setResult("true");
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String key = (String) param.args[1];
                    if (key.startsWith("bottom_banner_is_closed_today")) {
                        param.setResult("true");
                    }
                }
            });
            return;

        }

        //视频
        if (loadPackageParam.packageName.equals(video_packageName)) {
            if (prefs.getBoolean(enablemiuividio, false)) {

                findAndHookMethod("com.miui.videoplayer.ads.DynamicAd", loadPackageParam.classLoader, "replaceList", List.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam) {
                        paramAnonymousMethodHookParam.args[0] = null;
                        paramAnonymousMethodHookParam.args[1] = null;
                    }
                });
                findAndHookMethod("com.video.ui.view.AdView", loadPackageParam.classLoader, "getAdsBlock", Context.class, XC_MethodReplacement.returnConstant(null));
                Class clsCallback = XposedHelpers.findClass("com.video.ui.idata.SyncServiceHelper$Callback", loadPackageParam.classLoader);
                if (clsCallback != null) {
                    findAndHookMethod("com.video.ui.idata.SyncServiceHelper", loadPackageParam.classLoader, "fetchAds", Context.class, clsCallback, XC_MethodReplacement.returnConstant(null));
                }
                findAndHookMethod("com.video.ui.idata.iDataORM", loadPackageParam.classLoader, "getBooleanValue", Context.class, String.class, java.lang.Boolean.TYPE, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                        String key = (String) param.args[1];
                        if (Objects.equals(key, "debug_mode") || Objects.equals(key, "show_first_ads") || Objects.equals(key, "ads_show_homekey") || Objects.equals(key, "startup_ads_loop") || Objects.equals(key, "app_upgrade_splash")) {
                            param.setResult(false);
                        }
                    }
                });
                findAndHookMethod("com.video.ui.idata.iDataORM", loadPackageParam.classLoader, "getStringValue", Context.class, String.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                        String key = (String) param.args[1];
                        if (Objects.equals(key, "startup_ads")) {
                            param.setResult(null);
                        }
                    }
                });
                findAndHookMethod("com.video.ui.idata.iDataORM", loadPackageParam.classLoader, "getBooleanValue", Context.class, String.class, java.lang.Boolean.TYPE, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                        String key = (String) param.args[1];
                        if (Objects.equals(key, "show_title_ads") || Objects.equals(key, "show_channel_title_ads")) {
                            param.setResult(false);
                        }
                    }
                });
                findAndHookMethod("com.video.ui.idata.iDataORM", loadPackageParam.classLoader, "enabledAds", Context.class, XC_MethodReplacement.returnConstant(false));
                Class clsAdListener = XposedHelpers.findClass("com.miui.systemAdSolution_packageName.splashAd.IAdListener", loadPackageParam.classLoader);
                if (clsAdListener != null) {
                    findAndHookMethod("com.miui.ad.sdk.api.RemoteSystemSplashAdService", loadPackageParam.classLoader, "requestSystemSplashAd", clsAdListener, XC_MethodReplacement.returnConstant(false));
                    findAndHookMethod("com.miui.ad.sdk.api.SystemSplashAd", loadPackageParam.classLoader, "requestAd", Context.class, clsAdListener, XC_MethodReplacement.returnConstant(null));
                    findAndHookMethod("com.miui.ad.sdk.api.SystemSplashAd", loadPackageParam.classLoader, "requestAd", clsAdListener, XC_MethodReplacement.returnConstant(null));
                }

                findAndHookMethod("com.miui.videoplayer.model.OnlineUri", loadPackageParam.classLoader, "supportFrontAD", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.videoplayer.model.OnlineUri", loadPackageParam.classLoader, "supportPauseAD", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.videoplayer.model.OnlineUri", loadPackageParam.classLoader, "supportCornerAD", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.videoplayer.model.OnlineUri", loadPackageParam.classLoader, "skipAllAD", XC_MethodReplacement.returnConstant(true));
                findAndHookMethod("com.miui.videoplayer.model.OnlineUri", loadPackageParam.classLoader, "skipSDKAD", XC_MethodReplacement.returnConstant(true));
                findAndHookMethod("com.miui.videoplayer.model.OnlineUri", loadPackageParam.classLoader, "getMiAdFlag", XC_MethodReplacement.returnConstant(-1));

                findAndHookMethod("com.miui.videoplayer.ads.AdsContainer", loadPackageParam.classLoader, "setCornerAd", XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.miui.videoplayer.ads.AdsContainer", loadPackageParam.classLoader, "enableOfflineAds", XC_MethodReplacement.returnConstant(false));

                findAndHookMethod("com.miui.videoplayer.videoview.VideoViewContainer", loadPackageParam.classLoader, "playAd", XC_MethodReplacement.returnConstant(null));
                Class clsVideoView = XposedHelpers.findClass("com.miui.videoplayer.videoview.IVideoView", loadPackageParam.classLoader);
                if (clsVideoView != null) {
                    findAndHookMethod("com.miui.videoplayer.videoview.VideoViewContainer", loadPackageParam.classLoader, "playRealVideo", clsVideoView, java.lang.Boolean.TYPE, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                            param.args[1] = true;
                        }
                    });
                }

                findAndHookMethod("com.miui.videoplayer.videoview.VideoViewContainer", loadPackageParam.classLoader, "prepareRealVideoView", java.lang.Boolean.TYPE, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                        param.args[0] = true;// haveAd
                    }
                });

                findAndHookMethod("com.miui.videoplayer.ads.AdsService", loadPackageParam.classLoader, "doLaunch", String.class, XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.miui.videoplayer.videoview.MiAdsVideoView", loadPackageParam.classLoader, "haveAd", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.xiaomi.miui.ad.listeners.impl.AdEventListenerImpl", loadPackageParam.classLoader, "onAdRequest", String.class, JSONObject.class, XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.xiaomi.miui.ad.listeners.impl.AdEventListenerImpl", loadPackageParam.classLoader, "onAdClicked", String.class, JSONObject.class, XC_MethodReplacement.returnConstant(null));

                Class clsBlock = XposedHelpers.findClass("com.tv.ui.metro.model.Block", loadPackageParam.classLoader);
                Class clsPool = XposedHelpers.findClass("android.support.v7.widget.RecyclerView$RecycledViewPool", loadPackageParam.classLoader);
                if (clsBlock != null && clsPool != null) {
                    findAndHookConstructor("com.video.ui.view.BlockAdapter", loadPackageParam.classLoader, Context.class, clsBlock, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                            fuckParam(param, "ui_type");

                        }
                    });
                    findAndHookConstructor("com.video.ui.view.BlockAdapter", loadPackageParam.classLoader, Context.class, clsBlock, clsPool, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                            fuckParam(param, "ui_type");
                        }
                    });
                    findAndHookMethod("com.video.ui.view.BlockAdapter", loadPackageParam.classLoader, "addGroup", clsBlock, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                            fuckParam(param, "ui_type");
                        }
                    });
                    findAndHookMethod("com.video.ui.view.block.PortBlockView", loadPackageParam.classLoader, "initUI", clsBlock, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                            fuckParam(param, "blocks");
                        }
                    });
                }

                if (clsBlock != null) {
                    findAndHookMethod("com.video.ui.view.ListFragment", loadPackageParam.classLoader, "setBlockView", clsBlock, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws IllegalAccessException, NoSuchFieldException {
                            Class<?> o = (Class<?>) param.args[0];
                            Field fBlocks = o.getClass().getDeclaredField("blocks");
                            fBlocks.isAccessible();
                            fBlocks.set(o, null);
                            Field fFooters = o.getClass().getDeclaredField("footers");
                            fFooters.isAccessible();
                            fFooters.set(o, null);
                        }
                    });
                }

                Class<?> clsVideo = XposedHelpers.findClass("com.tv.ui.metro.model.VideoItem", loadPackageParam.classLoader);
                if (clsVideo != null) {
                    findAndHookMethod("com.video.ui.view.DetailFragment", loadPackageParam.classLoader, "updateVideo", clsVideo, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws IllegalAccessException, NoSuchFieldException {
                            Class<?> oVideo = (Class<?>) param.args[0];
                            Field fBlocks = oVideo.getClass().getDeclaredField("blocks");
                            fBlocks.isAccessible();
                            fBlocks.set(oVideo, null);
                            Field fHeaders = oVideo.getClass().getDeclaredField("headers");
                            fHeaders.isAccessible();
                            fHeaders.set(oVideo, null);
                        }
                    });
                }

                findAndHookMethod("com.video.ui.view.DetailFragment", loadPackageParam.classLoader, "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws IllegalAccessException, NoSuchFieldException {
                        Class<?> fragment = (Class<?>) param.thisObject;
                        Field fR1 = fragment.getClass().getDeclaredField("relative_region");
                        Field fR2 = fragment.getClass().getDeclaredField("headers_region");
                        FrameLayout r1 = (FrameLayout) fR1.get(fragment);
                        FrameLayout r2 = (FrameLayout) fR2.get(fragment);
                        r1.setVisibility(View.GONE);
                        r2.setVisibility(View.GONE);
                    }
                });
                findAndHookMethod("com.video.ui.view.DetailFragment", loadPackageParam.classLoader, "checkAdsPresentVisibility", View.class, XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.video.ui.view.ListFragment", loadPackageParam.classLoader, "checkAdsPresentVisibility", View.class, XC_MethodReplacement.returnConstant(null));

                Class<?> clsGenericBlock = XposedHelpers.findClass("com.tv.ui.metro.model.GenericBlock", loadPackageParam.classLoader);
                if (clsGenericBlock != null) {
                    findAndHookMethod("com.video.ui.view.user.MyVideoFragment", loadPackageParam.classLoader, "onLoadFinished", Loader.class, clsGenericBlock, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                            param.args[1] = null;
                        }
                    });
                }

                findAndHookMethod("com.miui.systemAdSolution_packageName.landingPage.LandingPageService", loadPackageParam.classLoader, "init", Context.class, XC_MethodReplacement.returnConstant(null));
            }
            return;
        }


        //文件管理器
        if (loadPackageParam.packageName.equals(fileexplorer_packageName)) {
            if (prefs.getBoolean(enableFileManager, false)) {
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "isAdEnable", Context.class, String.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "supportAd", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "ifAdShowByCloudForNetwork", Context.class, String.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "getHomePageHotVideoTipSwitch", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "getHomePageHotVideoTopicUri", Context.class, XC_MethodReplacement.returnConstant(""));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "getAdStyleName", Context.class, String.class, XC_MethodReplacement.returnConstant(""));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "tryInit", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "isVideoEnable", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "isStickerEnable", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.xunlei.adlibrary.XunleiADSdk", loadPackageParam.classLoader, "setup", Context.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return null;
                    }
                });
                findAndHookMethod("com.xunlei.adlibrary.analytics_packageName.xunlei.AdStatHelper", loadPackageParam.classLoader, "init", Context.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return null;
                    }
                });
                findAndHookMethod("com.android.fileexplorer.video.upload.VideoItemManager", loadPackageParam.classLoader, "initLoad", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return null;
                    }
                });
                findAndHookMethod("com.android.fileexplorer.model.Config", loadPackageParam.classLoader, "isCloudVideoEnabled", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.Config", loadPackageParam.classLoader, "isRecentAdShow", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.Config", loadPackageParam.classLoader, "isStickerEnabled", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.Config", loadPackageParam.classLoader, "isSystemHotAppEnable", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.Config", loadPackageParam.classLoader, "isSystemOnlyWifiEnable", Context.class, XC_MethodReplacement.returnConstant(true));
                findAndHookMethod("com.android.fileexplorer.model.Config", loadPackageParam.classLoader, "isVideoAdShow", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.Config", loadPackageParam.classLoader, "isVideoEnabled", XC_MethodReplacement.returnConstant(false));
            }
        }

        //音乐
        if (loadPackageParam.packageName.equals(music_packageName)) {
            if (prefs.getBoolean(enableMusic, false)) {
                Class<?> clsListener = XposedHelpers.findClass("com.android.volley.Respons$Listener", loadPackageParam.classLoader);
                Class<?> clsErrorListener = XposedHelpers.findClass("com.android.volley.Response$ErrorListener", loadPackageParam.classLoader);
                Class<?> clsAdInfo = XposedHelpers.findClass("com.miui.player.util.AdUtils$AdInfo", loadPackageParam.classLoader);
                XposedHelpers.findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "isAdEnable", XC_MethodReplacement.returnConstant(false));
                if (clsListener != null && clsErrorListener != null) {
                    XposedHelpers.
                            findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "getPlayAd", clsListener, clsErrorListener, XC_MethodReplacement.returnConstant(null));
                }
                findAndHookMethod("com.miui.player.util.ExperimentsHelper", loadPackageParam.classLoader, "isAdEnabled", XC_MethodReplacement.returnConstant(false));
                if (clsAdInfo != null) {
                    findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "handleDeepLinkUrl", Activity.class, clsAdInfo, XC_MethodReplacement.returnConstant(null));
                    findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "showAlertAndDownload", Activity.class, clsAdInfo, XC_MethodReplacement.returnConstant(null));
                    findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "handleAdClick", Activity.class, clsAdInfo, XC_MethodReplacement.returnConstant(null));
                    findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "postPlayAdStat", String.class, clsAdInfo, XC_MethodReplacement.returnConstant(null));
                }
                findAndHookMethod("com.miui.player.phone.view.NowplayingAlbumPage", loadPackageParam.classLoader, "getPlayAd", XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.miui.player.util.Configuration", loadPackageParam.classLoader, "isCmTest", XC_MethodReplacement.returnConstant(true));
                findAndHookMethod("com.miui.player.util.Configuration", loadPackageParam.classLoader, "isCpLogoVisiable", XC_MethodReplacement.returnConstant(false));
                // fuck the ad under account
                Class<?> clsAdInfo2 = XposedHelpers.findClass("com.xiaomi.music.online.model.AdInfo", loadPackageParam.classLoader);
                if (clsAdInfo != null) {
                    findAndHookMethod("com.miui.player.util.AdForbidManager", loadPackageParam.classLoader, "recordAdInfo", clsAdInfo2, Integer.TYPE, XC_MethodReplacement.returnConstant(null));
                    findAndHookMethod("com.miui.player.util.AdForbidManager", loadPackageParam.classLoader, "addForbidInfo", String.class, XC_MethodReplacement.returnConstant(null));
                    findAndHookMethod("com.miui.player.util.AdForbidManager", loadPackageParam.classLoader, "isForbidden", String.class, XC_MethodReplacement.returnConstant(true));
                }
                findAndHookMethod("com.miui.player.hybrid.feature.GetAdInfo", loadPackageParam.classLoader, "addAdQueryParams", Context.class, Uri.class, XC_MethodReplacement.returnConstant(""));
                findAndHookMethod("com.miui.player.display.view.cell.BannerAdItemCell", loadPackageParam.classLoader, "onFinishInflate", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                        View vThis = (View) param.thisObject;
                        try {
                            ((View) XposedHelpers.getObjectField(param.thisObject, "mClose")).setVisibility(View.GONE);
                        } catch (Throwable t) {
                            XposedBridge.log(t.getMessage());
                        }
                        try {
                            ((View) XposedHelpers.getObjectField(param.thisObject, "mImage")).setVisibility(View.GONE);
                        } catch (Throwable t) {
                            XposedBridge.log(t.getMessage());
                        }
                        ViewGroup.LayoutParams lp = vThis.getLayoutParams();
                        lp.height = 0;
                        vThis.setLayoutParams(lp);
                    }
                });
                // 2.7.300
                findAndHookMethod("com.miui.player.content.MusicHybridProvider", loadPackageParam.classLoader, "parseCommand", String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String scheme = (String) param.args[0];
                        if (Objects.equals(scheme, "advertise")) {
                            param.args[0] = "";
                        }
                    }
                });
                findAndHookMethod("com.miui.systemAdSolution_packageName.landingPage.LandingPageService", loadPackageParam.classLoader, "init", Context.class, XC_MethodReplacement.returnConstant(null));
                // 2.7.400
                XposedHelpers.findAndHookMethod("com.miui.player.phone.view.NowplayingContentView", loadPackageParam.classLoader, "setInfoVisibility", java.lang.Boolean.TYPE, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        param.args[0] = true;
                    }
                });
                if (clsAdInfo2 != null) {
                    XposedHelpers.findAndHookConstructor("com.miui.player.phone.view.NowplayingContentView$ShowAdRunnable", loadPackageParam.classLoader, clsAdInfo2, java.lang.Boolean.TYPE, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            param.args[1] = false;
                        }
                    });
                }
                findAndHookMethod("com.miui.player.phone.view.NowplayingContentView$ShowAdRunnable", loadPackageParam.classLoader, "setLoadAd", java.lang.Boolean.TYPE, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        param.args[0] = false;
                    }
                });
                // 2.8
                Class<?> clsDisplayItem = XposedHelpers.findClass("com.miui.player.display.model.DisplayItem", loadPackageParam.classLoader);
                if (clsDisplayItem != null) {
                    findAndHookMethod("com.miui.player.display.view.SearchPopularKeyCard", loadPackageParam.classLoader, "onBindItem", clsDisplayItem, Integer.TYPE, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            View v = (View) param.thisObject;
                            v.getLayoutParams().height = 0;
                            v.setVisibility(View.GONE);
                        }
                    });
                    findAndHookMethod("com.miui.player.display.view.BannerCard", loadPackageParam.classLoader, "onBindItem", clsDisplayItem, Integer.TYPE, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            View v = (View) param.thisObject;
                            v.getLayoutParams().height = 0;
                            v.setVisibility(View.GONE);
                        }
                    });
                }
                return;
            }
        }

        //下载管理
        if (loadPackageParam.packageName.equals(downloads_packageName)) {
            if (prefs.getBoolean(enableDownload, false)) {
                findAndHookConstructor("com.android.providers.downloads.ui.recommend.HomePageRecommendApi", loadPackageParam.classLoader, "getAdNumlnRecommendAppList", List.class, XC_MethodReplacement.returnConstant(0));
                findAndHookConstructor("com.android.providers.downloads.ui.recommend.HomePageRecommendApi", loadPackageParam.classLoader, "getBannerAdList", long.class, String.class, String.class, XC_MethodReplacement.returnConstant(null));
                findAndHookConstructor("com.android.providers.downloads.ui.recommend.HomePageRecommendApi", loadPackageParam.classLoader, "getDatailPageRecommend", String.class, String.class, XC_MethodReplacement.returnConstant(null));
                findAndHookConstructor("com.android.providers.downloads.ui.recommend.HomePageRecommendApi", loadPackageParam.classLoader, "requestBannerRecommend", String.class, String.class, XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "getAdButtonType", XC_MethodReplacement.returnConstant(0));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "getAdSummaryType", XC_MethodReplacement.returnConstant(0));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "getAdType", XC_MethodReplacement.returnConstant(6));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowAd", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowAppSubject", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowExtraAd", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowRecommendInfo", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isStableShowActivateNotify", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "supportRank", XC_MethodReplacement.returnConstant(false));
            }
        }


        //天气
        if (loadPackageParam.packageName.equals(weather2_packageName)) {

            if (prefs.getBoolean(enableWeather, false)) {
                findAndHookMethod("com.miui.weather2.tools.ToolUtils", loadPackageParam.classLoader, "checkCommericalStatue", Context.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult(false);
                    }
                });
                findAndHookMethod("com.miui.weather2.tools.ToolUtils", loadPackageParam.classLoader, "canRequestCommercialInfo", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.weather2.tools.ToolUtils", loadPackageParam.classLoader, "checkCommericalStatue", Context.class, XC_MethodReplacement.returnConstant(false));

                findAndHookMethod("com.miui.weather2.ActivityDailyForecastDetail", loadPackageParam.classLoader, "ep", XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.miui.weather2.ActivityDailyForecastDetail", loadPackageParam.classLoader, "eq", XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.miui.weather2.ActivityDailyForecastDetail", loadPackageParam.classLoader, "er", XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.miui.weather2.ActivityDailyForecastDetail", loadPackageParam.classLoader, "eo", XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.miui.weather2.structures.DailyForecastAdData", loadPackageParam.classLoader, "isAdInfosExistence", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.weather2.structures.DailyForecastAdData", loadPackageParam.classLoader, "isAdTitleExistence", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.weather2.structures.DailyForecastAdData", loadPackageParam.classLoader, "isLandingPageUrlExistence", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.weather2.structures.DailyForecastAdData", loadPackageParam.classLoader, "isUseSystemBrowserExistence", XC_MethodReplacement.returnConstant(false));

                // 8.2.1
                findAndHookMethod("com.miui.weather2.WeatherApplication", loadPackageParam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        Context ctx = (Context) param.args[0];
                        SharedPreferences pref = ctx.getSharedPreferences("com.miui.weather2.information", 0);
                        pref.edit().putBoolean("agree_to_have_information", false).apply();
                    }
                });

                return;
            }
        }

        //个性主题
        if (loadPackageParam.packageName.equals(thememanager_packageName)) {
            if (prefs.getBoolean(enabletheme, false)) {
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

                Class<?> clsHybridView = XposedHelpers.findClass("miui.hybrid.HybridView", loadPackageParam.classLoader);
                if (clsHybridView != null) {
                    findAndHookMethod("com.android.thememanager.h5.ThemeHybridFragment$BaseWebViewClient", loadPackageParam.classLoader, "shouldInterceptRequest", clsHybridView, String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            String url = (String) param.args[1];
                            if (url.contains("AdCenter")) {
                                param.args[1] = "http://127.0.0.1/";
                            }
                        }
                    });
                }
                findAndHookMethod("com.android.thememanager.util.ApplicationHelper", loadPackageParam.classLoader, "isFreshMan", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.thememanager.util.ApplicationHelper", loadPackageParam.classLoader, "hasFreshManMarkRecord", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.systemAdSolution_packageName.landingPage.LandingPageService", loadPackageParam.classLoader, "init", Context.class, XC_MethodReplacement.returnConstant(null));
                return;
            }
        }

        // 短信
        if (loadPackageParam.packageName.equals(mms_packageName)) {
            if (prefs.getBoolean(enableMMS, false)) {
                findAndHookMethod("com.android.mms.ui.MessageUtils", loadPackageParam.classLoader, "isMessagingTemplateAllowed", Context.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam) {
                        Context mc = (Context) paramAnonymousMethodHookParam.args[0];
                        paramAnonymousMethodHookParam.setResult(!mc.getClass().getName().toLowerCase().contains("app"));
                    }
                });
                findAndHookMethod("com.android.mms.ui.SingleRecipientConversationActivity", loadPackageParam.classLoader, "showMenuMode", XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.android.mms.util.MiStatSdkHelper", loadPackageParam.classLoader, "recordBottomMenuShown", String.class, XC_MethodReplacement.returnConstant(null));
            }
        }
    }


    private static void findAndHookMethod(String p1, ClassLoader lpparam, String p2, Object... parameterTypesAndCallback) {
        try {
            XposedHelpers.findAndHookMethod(p1, lpparam, p2, parameterTypesAndCallback);

        } catch (Throwable localString3) {
            XposedBridge.log(localString3);
        }
    }

    private static void findAndHookConstructor(String p1, ClassLoader lpparam, Object... parameterTypesAndCallback) {
        try {
            XposedHelpers.findAndHookConstructor(p1, lpparam, lpparam, parameterTypesAndCallback);

        } catch (Throwable localString3) {
            XposedBridge.log(localString3);
        }
    }

    private void fuckParam(XC_MethodHook.MethodHookParam param, String param1) {

        try {
            Class clsThis = param.thisObject.getClass();
            Field fBlockRootArrayList = clsThis.getDeclaredField("mBlockRootArrayList");
            fBlockRootArrayList.isAccessible();
            ArrayList<?> mBlockRootArrayList = (ArrayList<?>) fBlockRootArrayList.get(param.thisObject);
            for (Object b : mBlockRootArrayList) {
                Field fUI = b.getClass().getSuperclass().getDeclaredField(param1);
                fUI.isAccessible();
                Class<?> oUI = (Class<?>) fUI.get(b);
                Method mId = oUI.getClass().getMethod("id");
                int id = (int) mId.invoke(oUI);
                if (id == 101 || id == 10001 || id == 282 || id == 257 || id == 221 ||
                        id == 501 || id == 502 || id == 503 || (id == 601) || (id == 602) || id == 603 || id == 604) {
                    mBlockRootArrayList.remove(b);
                }
            }
        } catch (Exception e) {
            XposedBridge.log(e.fillInStackTrace());
        }
    }

}


