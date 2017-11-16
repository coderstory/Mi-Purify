package com.coderstory.Purify.fragment;


import android.widget.Switch;

import com.coderstory.Purify.R;
import com.coderstory.Purify.fragment.base.BaseFragment;


public class OthersFragment extends BaseFragment {
    public OthersFragment() {
    }

    @Override
    protected void setUpView() {


        $(R.id.RemoveSearchBar).setOnClickListener(v -> {
            getEditor().putBoolean("RemoveSearchBar", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.fixpcb).setOnClickListener(v -> {
            getEditor().putBoolean("fixpcb", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });


        $(R.id.root25).setOnClickListener(v -> {
            getEditor().putBoolean("root25", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.authcreak).setOnClickListener(v -> {
            getEditor().putBoolean("authcreak", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.zipauthcreak).setOnClickListener(v -> {
            getEditor().putBoolean("zipauthcreak", ((Switch) v).isChecked());
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
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_others;
    }

    @Override
    protected void setUpData() {
        ((Switch) $(R.id.RemoveSearchBar)).setChecked(getPrefs().getBoolean("RemoveSearchBar", false));
        ((Switch) $(R.id.fixpcb)).setChecked(getPrefs().getBoolean("fixpcb", false));
        ((Switch) $(R.id.root25)).setChecked(getPrefs().getBoolean("root25", false));
        ((Switch) $(R.id.hide_icon_label)).setChecked(getPrefs().getBoolean("hide_icon_label", false));
        ((Switch) $(R.id.authcreak)).setChecked(getPrefs().getBoolean("authcreak", false));
        ((Switch) $(R.id.zipauthcreak)).setChecked(getPrefs().getBoolean("zipauthcreak", false));
        ((Switch) $(R.id.downgrade)).setChecked(getPrefs().getBoolean("downgrade", false));
    }
}
