package com.coderstory.Purify.fragment;


import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Switch;

import com.coderstory.Purify.R;
import com.coderstory.Purify.fragment.base.BaseFragment;


public class SettingsFragment extends BaseFragment {
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    protected void setUpView() {

        $(R.id.enableCheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("enableCheck", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();
            }
        });

        $(R.id.RemoveSearchBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("RemoveSearchBar", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();
                sudoFixPermissions();
            }
        });

        $(R.id.installType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("installType", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();
            }
        });

        $(R.id.fixpcb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("fixpcb", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();
            }
        });


        $(R.id.root25).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("root25", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();
            }
        });

        $(R.id.hideicon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("hideIcon", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();
                ComponentName localComponentName = new ComponentName(getMContext(), "com.coderstory.Purify.activity.SplashActivity");
                PackageManager localPackageManager = getMContext().getPackageManager();
                localPackageManager.getComponentEnabledSetting(localComponentName);
                PackageManager packageManager = getMContext().getPackageManager();
                ComponentName componentName = new ComponentName(getMContext(), "com.coderstory.Purify.activity.SplashActivity");

                if (((Switch) v).isChecked()) {
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                } else {
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                            PackageManager.DONT_KILL_APP);
                }
            }
        });
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void setUpData() {
        ((Switch) $(R.id.enableCheck)).setChecked(getPrefs().getBoolean("enableCheck", true));
        ((Switch) $(R.id.RemoveSearchBar)).setChecked(getPrefs().getBoolean("RemoveSearchBar", false));
        ((Switch) $(R.id.hideicon)).setChecked(getPrefs().getBoolean("hideIcon", false));
        ((Switch) $(R.id.installType)).setChecked(getPrefs().getBoolean("installType", false));
        ((Switch) $(R.id.fixpcb)).setChecked(getPrefs().getBoolean("fixpcb", false));
        ((Switch) $(R.id.root25)).setChecked(getPrefs().getBoolean("root25", false));
    }
}
