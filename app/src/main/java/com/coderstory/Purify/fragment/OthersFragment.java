package com.coderstory.purify.fragment;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.widget.Switch;

import com.coderstory.purify.R;
import com.coderstory.purify.fragment.base.BaseFragment;
import com.coderstory.purify.utils.RuntimeUtil;



public class OthersFragment extends BaseFragment {
    public OthersFragment() {
    }

    @Override
    protected void setUpView() {

        $(R.id.enableBlockAD).setOnClickListener(v -> {
            getEditor().putBoolean("EnableBlockAD", ((Switch) v).isChecked());
            getEditor().apply();

            if (((Switch) v).isChecked()) {
                new Thread(() -> {
                    String[] list = new String[]{"pm disable com.miui.systemAdSolution",
                            "pm disable com.miui.analytics",
                            "pm disable com.qualcomm.qti.seemp",
                            "pm disable com.xiaomi.ab",
                            "pm disable com.miLink"};
                    RuntimeUtil.execSilent(list);
                }).start();
            } else {
                new Thread(() -> {
                    String[] list = new String[]{"pm enable com.miui.systemAdSolution",
                            "pm enable com.miui.analytics",
                            "pm enable com.qualcomm.qti.seemp",
                            "pm enable com.xiaomi.ab",
                            "pm enable com.miLink"};
                    RuntimeUtil.execSilent(list);
                }).start();
            }
            sudoFixPermissions();
        });


        $(R.id.enableMMS).setOnClickListener(v -> {
            getEditor().putBoolean("enableMMS", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();

        });

        $(R.id.enableDownload).setOnClickListener(v -> {
            getEditor().putBoolean("enableDownload", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();

        });

        $(R.id.signatureCheck).setOnClickListener(v -> {
            getEditor().putBoolean("signatureCheck", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.comparisonSignature).setOnClickListener(v -> {
            getEditor().putBoolean("comparisonSignature", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });


        $(R.id.downgrade).setOnClickListener(v -> {
            getEditor().putBoolean("downgrade", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.hide_icon_label).setOnClickListener(v -> {
            getEditor().putBoolean("hide_icon_label", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });
        $(R.id.prevent_freeze_reverse).setOnClickListener(v -> {
            getEditor().putBoolean("prevent_freeze_reverse", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.default_store).setOnClickListener(v -> {
            getEditor().putBoolean("default_store", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.downloadVip).setOnClickListener(v -> {
            getEditor().putBoolean("downloadVip", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.lowBatteryWarning).setOnClickListener(v -> {
            getEditor().putBoolean("lowBatteryWarning", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.alarmClockIcon).setOnClickListener(v -> {
            getEditor().putBoolean("alarmClockIcon", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.noBatteryWarning).setOnClickListener(v -> {
            getEditor().putBoolean("noBatteryWarning", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.hideNetworkSpeed).setOnClickListener(v -> {
            getEditor().putBoolean("hideNetworkSpeed", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        if (getPrefs().getString("platform", "").equals("")) {
            // 读取平台签名并保存
            new Thread(() -> {
                try {
                    PackageInfo packageInfo = getMContext().getPackageManager().getPackageInfo("android", PackageManager.GET_SIGNATURES);
                    if (packageInfo.signatures[0] != null) {
                        String platform = new String(Base64.encode(packageInfo.signatures[0].toByteArray(), Base64.DEFAULT)).replaceAll("\n", "");
                        getEditor().putString("platform", platform);
                        getEditor().apply();
                        sudoFixPermissions();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_others;
    }

    @Override
    protected void setUpData() {
        ((Switch) $(R.id.hide_icon_label)).setChecked(getPrefs().getBoolean("hide_icon_label", false));
        ((Switch) $(R.id.signatureCheck)).setChecked(getPrefs().getBoolean("signatureCheck", false));
        ((Switch) $(R.id.comparisonSignature)).setChecked(getPrefs().getBoolean("comparisonSignature", false));
        ((Switch) $(R.id.downgrade)).setChecked(getPrefs().getBoolean("downgrade", false));
        ((Switch) $(R.id.prevent_freeze_reverse)).setChecked(getPrefs().getBoolean("prevent_freeze_reverse", false));
        ((Switch) $(R.id.enableBlockAD)).setChecked(getPrefs().getBoolean("EnableBlockAD", false));
        ((Switch) $(R.id.enableMMS)).setChecked(getPrefs().getBoolean("EnableMMS", false));
        ((Switch) $(R.id.enableDownload)).setChecked(getPrefs().getBoolean("EnableDownload", false));
        ((Switch) $(R.id.default_store)).setChecked(getPrefs().getBoolean("default_store", false));
        ((Switch) $(R.id.noBatteryWarning)).setChecked(getPrefs().getBoolean("noBatteryWarning", false));
        ((Switch) $(R.id.alarmClockIcon)).setChecked(getPrefs().getBoolean("alarmClockIcon", false));
        ((Switch) $(R.id.lowBatteryWarning)).setChecked(getPrefs().getBoolean("lowBatteryWarning", false));
        ((Switch) $(R.id.downloadVip)).setChecked(getPrefs().getBoolean("downloadVip", false));
        ((Switch) $(R.id.enableMMS)).setChecked(getPrefs().getBoolean("enableMMS", false));
        ((Switch) $(R.id.enableDownload)).setChecked(getPrefs().getBoolean("enableDownload", false));
        ((Switch) $(R.id.hideNetworkSpeed)).setChecked(getPrefs().getBoolean("hideNetworkSpeed", false));
    }
}
