package com.coderstory.purify.module;

import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;

import com.coderstory.purify.plugins.IModule;
import com.coderstory.purify.utils.XposedHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class CorePatch extends XposedHelper implements IModule {
    static final String f34e = "308203553082023da0030201020204378edaaa300d06092a864886f70d01010b0500305a310d300b0603550406130466616b65310d300b0603550408130466616b65310d300b0603550407130466616b65310d300b060355040a130466616b65310d300b060355040b130466616b65310d300b0603550403130466616b653020170d3138303533303034343434385a180f32313237313230353034343434385a305a310d300b0603550406130466616b65310d300b0603550408130466616b65310d300b0603550407130466616b65310d300b060355040a130466616b65310d300b060355040b130466616b65310d300b0603550403130466616b6530820122300d06092a864886f70d01010105000382010f003082010a0282010100b766ff6afd8a53edd4cee4985bc90e0c515157b5e9f731818961f7250d0d1ac7c7fb80eb5aeb8c28478732e8ff38cff574bfa0eba8039f73af1532f939c4ef9684719efbaba2dd3c583a20907c1c55248a63098c6da23dcfc877763d5fe6061dddd399cf2f49e3250e23f9e687a4d182bcd0662179ba4c9983448e34b4c83e5abbf4f87e87add9157c75fd40de3416744507a3517915f35b6fcad78766e8e1879df8ab823a6ffa335e4790f6e29c87393732025b63ce3a38e42cb0d48cdceb902f191d7d45823db9a0678895e8bfc59b2af7526ca4c2dc3dbe7e70c7c840e666b9629d36e5ddf1d9a80c37f1ab1bc1fb30432914008fbde95d5d3db7853565510203010001a321301f301d0603551d0e04160414d8513e1ae21c64e9ebeee3507e24ea375eef958e300d06092a864886f70d01010b0500038201010088bf20b36428558359536dddcfff16fe233656a92364cb544d8acc43b0859f880a8da339dd430616085edf035e4e6e6dd2281ceb14adde2f05e9ac58d547a09083eece0c6d405289cb7918f85754ee545eefe35e30c103cad617905e94eb4fb68e6920a60d30577855f9feb6e3a664856f74aa9f824aa7d4a3adf85e162c67b9a4261e3185f038ead96112ae3e574d280425e90567352fb82bc9173302122025eaecfabd94d0f9be69a85c415f7cf7759c9651734300952027b316c37aaa1b2418865a3fc7b6bd1072c92ccaacdaa1cf9586d9b8310ceee066ce68859107dfc45ccce729ad9e75b53b584fa37dcd64da8673b1279c6c5861ed3792deac156c8a";
    private HashSet f36g;

    public void initZygote(IXposedHookZygoteInit.StartupParam paramStartupParam) {

        XposedHelpers.findAndHookMethod("java.security.MessageDigest", null, "isEqual", new Object[]{byte[].class, byte[].class, new C00131()});
        try {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.android.org.conscrypt.OpenSSLSignature", null), "engineVerify", new C000510());
        } catch (Throwable th) {
        }
        Class findClass = XposedHelpers.findClass("android.util.apk.ApkSignatureSchemeV2Verifier", null);
        Class findClass2 = XposedHelpers.findClass("android.content.pm.PackageParser", null);
        Class findClass3 = XposedHelpers.findClass("android.util.jar.StrictJarVerifier", null);
        final Class findClass4 = XposedHelpers.findClass("android.content.pm.PackageParser.Package", null);
        XposedBridge.hookAllMethods(findClass2, "getApkSigningVersion", XC_MethodReplacement.returnConstant(Integer.valueOf(1)));
        XposedBridge.hookAllConstructors(findClass3, new C000611());
        XposedBridge.hookAllConstructors(findClass, new XC_MethodHook() {
            /* Access modifiers changed, original: protected */
            public void afterHookedMethod(MethodHookParam methodHookParam) throws NoSuchFieldException, IllegalAccessException {
                Object obj = methodHookParam.thisObject;

               // if (CorePatch.this.f35f.getBoolean("allow_no_sig"), true)) {
                    Field field = findClass4.getField("SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID");
                    field.setAccessible(true);
                    field.set(obj, -1);
               // }
            }
        });
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws IllegalAccessException, InvocationTargetException, InstantiationException {

        if (("android".equals(loadPackageParam.packageName)) && (loadPackageParam.processName.equals("android"))) {

            Class findClass = XposedHelpers.findClass("com.android.server.pm.PackageManagerService", loadPackageParam.classLoader);
            final Class findClass2 = XposedHelpers.findClass("android.content.pm.PackageParser.Package", loadPackageParam.classLoader);
            Class findClass3 = XposedHelpers.findClass("com.android.server.pm.PackageManagerServiceUtils", loadPackageParam.classLoader);
            Class findClass4 = XposedHelpers.findClass("android.content.pm.PackageParser.SigningDetails", loadPackageParam.classLoader);
            Constructor findConstructorExact = XposedHelpers.findConstructorExact(findClass4, Signature[].class, Integer.TYPE);
            findConstructorExact.setAccessible(true);
            Class findClass5 = XposedHelpers.findClass("android.util.apk.ApkSignatureVerifier", loadPackageParam.classLoader);
            final Class findClass6 = XposedHelpers.findClass("android.content.pm.PackageParser.PackageParserException", loadPackageParam.classLoader);
            final Field findField = XposedHelpers.findField(findClass6, "error");
            findField.setAccessible(true);
            try {
                XposedBridge.hookAllMethods(findClass, "checkDowngrade", new XC_MethodHook() {
                    /* Access modifiers changed, original: protected */
                    public void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        super.beforeHookedMethod(methodHookParam);
                        Object obj = methodHookParam.args[0];
                       
                       // if (CorePatch.this.f35f.getBoolean("allow_downgrade", true)) {
                            Field field = findClass2.getField("mVersionCode");
                            field.setAccessible(true);
                            field.set(obj, Integer.valueOf(0));
                            field = findClass2.getField("mVersionCodeMajor");
                            field.setAccessible(true);
                            field.set(obj, Integer.valueOf(0));
                       // }
                    }
                });
                XposedBridge.hookAllMethods(findClass4, "checkCapability", new C00164());
                XposedBridge.hookAllMethods(findClass4, "checkCapabilityRecover", new C00175());
                XposedBridge.hookAllMethods(findClass3, "verifySignatures", new C00186());
                Object[] r1 = new Object[2];
                r1[0] = new Signature[]{new Signature(f34e)};
                r1[1] = Integer.valueOf(1);
                final Object newInstance = findConstructorExact.newInstance(r1);
                XposedBridge.hookAllMethods(findClass5, "verifyV1Signature", new XC_MethodHook() {
                    /* Access modifiers changed, original: protected */
                    public void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        super.afterHookedMethod(methodHookParam);
                       
                        //if (CorePatch.this.f35f.getBoolean("allow_no_sig", true)) {
                            Throwable throwable = methodHookParam.getThrowable();
                            if (throwable != null) {
                                int i;
                                Throwable cause = throwable.getCause();
                                if (throwable.getClass() == findClass6) {
                                    i = findField.getInt(throwable);

                                    if (i == -103) {
                                        methodHookParam.setResult(newInstance);
                                    }
                                }
                                if (cause != null && cause.getClass() == findClass6) {
                                    i = findField.getInt(cause);
                                    if (i == -103) {
                                        methodHookParam.setResult(newInstance);
                                    }
                                }
                            }
                      //  }
                    }
                });
                XposedBridge.hookAllMethods(findClass, "systemReady", new C00208());
                XposedBridge.hookAllMethods(findClass3, "compareSignatures", new C00219());
            } catch (Throwable th) {
                XposedBridge.log("KwAdDnAEBhEHQAoR" + th);
            }

        }
    }

    class C00164 extends XC_MethodHook {
        C00164() {
        }
        public void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
            super.beforeHookedMethod(methodHookParam);

            //if (CorePatch.this.f35f.getBoolean("disable_verify"), true)) {
                methodHookParam.setResult(Boolean.valueOf(true));
           // }
        }
    }

    class C00208 extends XC_MethodHook {
        C00208() {
        }

        /* Access modifiers changed, original: protected */
        public void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
            super.afterHookedMethod(methodHookParam);
            PackageInfo packageInfo = (PackageInfo) XposedHelpers.callMethod(methodHookParam.thisObject, "getPackageInfo", new Object[]{"android", 64, 0});
            if (packageInfo.signatures != null) {
                CorePatch.this.f36g = new HashSet(Arrays.asList(packageInfo.signatures));
            }
        }
    }

    class C00219 extends XC_MethodHook {
        C00219() {
        }

        /* Access modifiers changed, original: protected */
        public void beforeHookedMethod(MethodHookParam methodHookParam) {

          //  if (CorePatch.this.f35f.getBoolean("disable_verify"), false) && CorePatch.this.f36g != null) {
                int length;
                int i;
                Signature[] signatureArr = (Signature[]) methodHookParam.args[0];
                if (signatureArr != null && signatureArr.length > 0) {
                    length = signatureArr.length;
                    i = 0;
                    while (i < length) {
                        if (!CorePatch.this.f36g.contains(signatureArr[i])) {
                            i++;
                        } else {
                            return;
                        }
                    }
                }
                signatureArr = (Signature[]) methodHookParam.args[1];
                if (signatureArr != null && signatureArr.length > 0) {
                    length = signatureArr.length;
                    i = 0;
                    while (i < length) {
                        if (!CorePatch.this.f36g.contains(signatureArr[i])) {
                            i++;
                        } else {
                            return;
                        }
                    }
                }
                methodHookParam.setResult(Integer.valueOf(0));
            }
        //}
    }

    class C00175 extends XC_MethodHook {
        C00175() {
        }

        /* Access modifiers changed, original: protected */
        public void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
            super.beforeHookedMethod(methodHookParam);
          //  if (CorePatch.this.f35f.getBoolean("disable_verify"), true)) {
                methodHookParam.setResult(Boolean.valueOf(true));
          //  }
        }
    }

    class C00186 extends XC_MethodHook {
        C00186() {
        }

        /* Access modifiers changed, original: protected */
        public void beforeHookedMethod(MethodHookParam methodHookParam) {

           // if (CorePatch.this.f35f.getBoolean("allow_no_sig"), true)) {
                methodHookParam.setResult(Boolean.valueOf(false));
            //}
        }
    }

    /* renamed from: me.weishu.corepatch.CorePatch$11 */
    class C000611 extends XC_MethodHook {
        C000611() {
        }

        /* Access modifiers changed, original: protected */
        public void beforeHookedMethod(MethodHookParam methodHookParam) {
           // if (CorePatch.this.f35f.getBoolean(C0027f.m20a("allow_no_sig"), true)) {
                methodHookParam.args[3] = Boolean.FALSE;
          //  }
        }
    }

    class C00131 extends XC_MethodHook {
        C00131() {
        }

        /* Access modifiers changed, original: protected */
        public void beforeHookedMethod(MethodHookParam methodHookParam) {
         //   if (CorePatch.this.f35f.getBoolean(C0027f.m20a("allow_no_sig"), true)) {
                methodHookParam.setResult(Boolean.valueOf(true));
           // }
        }
    }

    class C000510 extends XC_MethodHook {
        C000510() {
        }

        /* Access modifiers changed, original: protected */
        public void beforeHookedMethod(MethodHookParam methodHookParam) {

           // if (CorePatch.this.f35f.getBoolean(C0027f.m20a("allow_no_sig"), true)) {
                methodHookParam.setResult(Boolean.valueOf(true));
           // }
        }
    }
}
