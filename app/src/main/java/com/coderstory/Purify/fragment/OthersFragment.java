package com.coderstory.Purify.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Switch;

import com.coderstory.Purify.R;

import ren.solid.library.fragment.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class OthersFragment extends BaseFragment {



    public OthersFragment() {
        // Required empty public constructor
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_others;
    }

    @Override
    protected void setUpView() {


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
    }

    @Override
    protected void setUpData() {

        ((Switch) $(R.id.enablemiuiRoot)).setChecked(getPrefs().getBoolean("enablemiuiRoot",false));
        ((Switch) $(R.id.RemoveSearchBar)).setChecked(getPrefs().getBoolean("RemoveSearchBar",false));
    }

}
