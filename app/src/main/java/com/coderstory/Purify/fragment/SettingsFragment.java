package com.coderstory.Purify.fragment;



import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Switch;
import com.coderstory.Purify.R;
import ren.solid.library.fragment.base.BaseFragment;
import static com.coderstory.Purify.utils.root.ShellUtils.execCommand;

public class SettingsFragment extends BaseFragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void setUpView() {


        $(R.id.enableCheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("enableCheck",((Switch)v).isChecked());
                getEditor().apply();
            }
        });

        $(R.id.gantanhao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("gantanhao",((Switch)v).isChecked());
                getEditor().apply();

                if (((Switch)v).isChecked()){
                    execCommand("adb shell settings put global captive_portal_server www.g.cn",true);

                }else{
                    execCommand("settings put global captive_portal_detection_enabled 0",true);
                  //  execCommand("adb shell settings put global captive_portal_detection_enabled 1",true);
                }
            }
        });

        $(R.id.enablemiuiRoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("enablemiuiRoot",((Switch)v).isChecked());
                getEditor().apply();
            }
        });

        $(R.id.RemoveSearchBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("RemoveSearchBar",((Switch)v).isChecked());
                getEditor().apply();
            }
        });

        $(R.id.miuiMusicCustomization).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("miuiMusicCustomization",((Switch)v).isChecked());
                getEditor().apply();
            }
        });


        $(R.id.enableadb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("enableadb",((Switch)v).isChecked());
                getEditor().apply();
            }
        });


        $(R.id.fixpcb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("fixpcb",((Switch)v).isChecked());
                getEditor().apply();
            }
        });

        $(R.id.hideicon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("hideicon",((Switch)v).isChecked());
                getEditor().apply();
                ComponentName localComponentName = new ComponentName( getMContext(), "com.coderstory.Purify.activity.MyAppIntro");
                PackageManager localPackageManager =  getMContext().getPackageManager();
                localPackageManager.getComponentEnabledSetting(localComponentName);
                PackageManager packageManager = getMContext(). getPackageManager();
                ComponentName componentName = new ComponentName( getMContext(), "com.coderstory.Purify.activity.MyAppIntro");

                if(((Switch)v).isChecked()){
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                }else
                {
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                            PackageManager.DONT_KILL_APP);
                }

            }
        });


    }

    @Override
    protected void setUpData() {
        ((Switch) $(R.id.enableCheck)).setChecked(getPrefs().getBoolean("enableCheck",true));
        ((Switch) $(R.id.gantanhao)).setChecked(getPrefs().getBoolean("gantanhao",false));
        ((Switch) $(R.id.enablemiuiRoot)).setChecked(getPrefs().getBoolean("enablemiuiRoot",false));
        ((Switch) $(R.id.RemoveSearchBar)).setChecked(getPrefs().getBoolean("RemoveSearchBar",false));
        ((Switch) $(R.id.miuiMusicCustomization)).setChecked(getPrefs().getBoolean("miuiMusicCustomization",false));
        ((Switch) $(R.id.fixpcb)).setChecked(getPrefs().getBoolean("fixpcb",false));
        ((Switch) $(R.id.enableadb)).setChecked(getPrefs().getBoolean("enableadb",false));
        ((Switch) $(R.id.hideicon)).setChecked(getPrefs().getBoolean("hideicon",false));
    }

}
