package com.coderstory.Purify.module;

import android.content.Context;
import android.opengl.Visibility;
import android.view.View;

import com.coderstory.Purify.plugins.IModule;
import com.coderstory.Purify.utils.XposedHelper;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
             

                findAndHookMethod("com.android.thememanager.e.a", loadPackageParam.classLoader, "parseAdInfo", String.class, XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.android.thememanager.view.b", loadPackageParam.classLoader, "getAdInfo",XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.android.thememanager.e.a", loadPackageParam.classLoader, "getAdMarker", XC_MethodReplacement.returnConstant(0));
                findAndHookMethod("com.android.thememanager.view.b", loadPackageParam.classLoader, "c", XC_MethodReplacement.returnConstant(null));
                // return new com.android.thememanager.view.a(this.aJ, arg4.getExtraMeta().getSerializable("ad_info")).a();
                Class<?> clsPageItem = XposedHelpers.findClass("com.android.thememanager.e.h", loadPackageParam.classLoader);
                if (clsPageItem != null) {
                    findAndHookMethod("com.android.thememanager.a.b.m", loadPackageParam.classLoader, "k", clsPageItem, XC_MethodReplacement.returnConstant(null));
                }
                
                try {
                    final Class localClass = XposedHelpers.findClass("  miui.hybrid.Request", loadPackageParam.classLoader);
                    Constructor c=localClass.getConstructor(int.class);
                    Object a = c.newInstance(0);
                    findAndHookMethod("com.android.thememanager.h5.feature.AdFeature", loadPackageParam.classLoader, "performClick", localClass,XC_MethodReplacement.returnConstant(a));
                    findAndHookMethod("com.android.thememanager.h5.feature.AdFeature", loadPackageParam.classLoader, "reportView",localClass, XC_MethodReplacement.returnConstant(a));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    XposedBridge.log(e);
                }

                //v2.setShowType(arg7.optInt("showType"));
                //v2.setRecommendMaxCol(arg7.optInt("shopWindowCols", -1));
                //v2.setResourceStamp(arg7.optString("category"));
                findAndHookMethod("com.android.thememanager.a.b.f", loadPackageParam.classLoader, "a", JSONObject.class, XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.android.thememanager.view.z", loadPackageParam.classLoader, "a", int.class, XC_MethodReplacement.returnConstant(View.GONE));
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
                //findAndHookMethod("com.android.mms.util.MiStatSdkHelper", loadPackageParam.classLoader, "recordBottomMenuShown", String.class, XC_MethodReplacement.returnConstant(null));
            }
        }
    }

}


