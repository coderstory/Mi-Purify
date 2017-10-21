package com.coderstory.Purify.fragment;


import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.coderstory.Purify.R;

import java.io.File;

import eu.chainfire.libsuperuser.Shell;
import com.coderstory.Purify.fragment.base.BaseFragment;

import static com.coderstory.Purify.config.Misc.ApplicationName;
import static com.coderstory.Purify.config.Misc.SharedPreferencesName;


public class SettingsFragment extends BaseFragment {
    public static final String PREFS_FOLDER = " /data/data/" + ApplicationName + "/shared_prefs\n";
    public static final String PREFS_FILE = " /data/data/" + ApplicationName + "/shared_prefs/c" + SharedPreferencesName + ".xml\n";
    private static final String TAG = "AA";

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
            }
        });

        $(R.id.RemoveSearchBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("RemoveSearchBar", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();
            }
        });

        $(R.id.installType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("installType", ((Switch) v).isChecked());
                getEditor().apply();
            }
        });

        $(R.id.hideicon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("hideIcon", ((Switch) v).isChecked());
                getEditor().apply();
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
    }

    private void sudoFixPermissions() {
        new Thread() {
            @Override
            public void run() {
                File pkgFolder = new File("/data/data/" + ApplicationName);
                if (pkgFolder.exists()) {
                    pkgFolder.setExecutable(true, false);
                    pkgFolder.setReadable(true, false);
                }
                Shell.SU.run("chmod  755 " + PREFS_FOLDER);
                // Set preferences file permissions to be world readable
                Shell.SU.run("chmod  644 " + PREFS_FILE);
                Log.d(TAG, "Saved Preferences Successfully.");
            }
        }.start();
    }
}
