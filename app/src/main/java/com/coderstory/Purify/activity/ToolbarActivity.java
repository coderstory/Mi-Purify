package com.coderstory.Purify.activity;

import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.coderstory.Purify.R;
import com.coderstory.Purify.activity.base.BaseActivity;


public abstract class ToolbarActivity extends BaseActivity {

    protected Toolbar mToolbar;
    protected AppBarLayout mAppBarLayout;
    protected FragmentManager mFragmentManager;

    @Override
    protected void init() {
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void setUpView() {
        mAppBarLayout = $(R.id.appbar_layout);
        mToolbar = $(R.id.toolbar);
        mToolbar.setTitle(getToolbarTitle());

        setUpToolBar();


    }

    private void setUpToolBar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);//决定左上角的图标是否可以点击
        }
    }

    protected abstract String getToolbarTitle();

    @Override
    protected void setUpData() {
        mFragmentManager.beginTransaction().replace(R.id.fl_content, setFragment()).commit();
    }

    protected abstract Fragment setFragment();


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_content;
    }

}
