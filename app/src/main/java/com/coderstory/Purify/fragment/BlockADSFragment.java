package com.coderstory.Purify.fragment;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.coderstory.Purify.R;

import java.io.File;

import eu.chainfire.libsuperuser.Shell;
import ren.solid.library.fragment.base.BaseFragment;

import static com.coderstory.Purify.config.Misc.ApplicationName;
import static com.coderstory.Purify.config.Misc.SharedPreferencesName;
import static com.coderstory.Purify.utils.roothelper.ShellUtils.execCommand;

public class BlockADSFragment extends BaseFragment {
    public static final String PREFS_FOLDER = " /data/data/" + ApplicationName + "/shared_prefs\n";
    public static final String PREFS_FILE = " /data/data/" + ApplicationName + "/shared_prefs/c" + SharedPreferencesName + ".xml\n";
    private static final String TAG = "AA";

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_block_ad;
    }

    @Override
    protected void setUpView() {
        $(R.id.enableBlockAD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("EnableBlockAD", ((Switch) v).isChecked());
                getEditor().apply();
                setCheck(((Switch) v).isChecked());

                ((Switch) $(R.id.enableBlockADBasic)).setChecked(((Switch) v).isChecked());
                getEditor().putBoolean("EnableBlockADBasic", ((Switch) v).isChecked());
                getEditor().apply();

                if (((Switch) v).isChecked()) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            execCommand("pm disable com.miui.systemAdSolution", true);
                            execCommand("pm disable com.miui.analytics", true);
                            execCommand("pm disable com.qualcomm.qti.seemp", true);
                            execCommand("pm disable com.xiaomi.ab", true);
                            execCommand("pm disable com.miLink", true);
                        }
                    }.start();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            execCommand("pm enable com.miui.systemAdSolution", true);
                            execCommand("pm enable com.miui.analytics", true);
                            execCommand("pm enable com.qualcomm.qti.seemp", true);
                            execCommand("pm enable com.xiaomi.ab", true);
                            execCommand("pm enable com.miLink", true);
                        }
                    }.start();
                }
                sudoFixPermissions();
            }
        });

        $(R.id.enableBlockADBasic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("EnableBlockADBasic", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();

            }
        });

        $(R.id.enableMMS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("EnableMMS", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();

            }
        });
        $(R.id.enableWeather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("EnableWeather", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();

            }
        });
        $(R.id.enableFileManager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("EnableFileManager", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();

            }
        });

        $(R.id.enableDownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("EnableDownload", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();

            }
        });

        $(R.id.enableSafeCenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("EnableSafeCenter", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();

            }
        });

        $(R.id.enableMusic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("EnableMusic", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();

            }
        });

        $(R.id.enabletheme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getEditor().putBoolean("EnableTheme", ((Switch) v).isChecked());
                getEditor().apply();
                sudoFixPermissions();
            }
        });

    }

    @Override
    protected void setUpData() {
        ((Switch) $(R.id.enabletheme)).setChecked(getPrefs().getBoolean("enableTheme", true));
        ((Switch) $(R.id.enableBlockAD)).setChecked(getPrefs().getBoolean("EnableBlockAD", true));
        ((Switch) $(R.id.enableBlockADBasic)).setChecked(getPrefs().getBoolean("EnableBlockADBasic", true));
        ((Switch) $(R.id.enableMMS)).setChecked(getPrefs().getBoolean("EnableMMS", true));
        ((Switch) $(R.id.enableWeather)).setChecked(getPrefs().getBoolean("EnableWeather", true));
        ((Switch) $(R.id.enableFileManager)).setChecked(getPrefs().getBoolean("enableFileManager", true));
        ((Switch) $(R.id.enableDownload)).setChecked(getPrefs().getBoolean("enableDownload", true));
        ((Switch) $(R.id.enableSafeCenter)).setChecked(getPrefs().getBoolean("enableSafeCenter", true));
        ((Switch) $(R.id.enableMusic)).setChecked(getPrefs().getBoolean("enableMusic", true));
        setCheck(getPrefs().getBoolean("EnableBlockAD", true));

    }

    private void setCheck(boolean type) {
        if (type) {
            $(R.id.enableBlockADBasic).setEnabled(true);
            $(R.id.enableMMS).setEnabled(true);
            $(R.id.enableWeather).setEnabled(true);
            $(R.id.enableFileManager).setEnabled(true);
            $(R.id.enableDownload).setEnabled(true);
            $(R.id.enableSafeCenter).setEnabled(true);
            $(R.id.enableMusic).setEnabled(true);

            $(R.id.enabletheme).setEnabled(true);
        } else {
            $(R.id.enableBlockADBasic).setEnabled(false);
            $(R.id.enableMMS).setEnabled(false);
            $(R.id.enableWeather).setEnabled(false);
            $(R.id.enableFileManager).setEnabled(false);
            $(R.id.enableDownload).setEnabled(false);
            $(R.id.enableSafeCenter).setEnabled(false);
            $(R.id.enableMusic).setEnabled(false);
            $(R.id.enabletheme).setEnabled(false);
        }

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

