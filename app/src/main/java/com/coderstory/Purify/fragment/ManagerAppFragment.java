package com.coderstory.Purify.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.coderstory.Purify.R;

import java.util.ArrayList;
import java.util.List;

import com.coderstory.Purify.fragment.base.BaseFragment;


public class ManagerAppFragment extends BaseFragment implements ViewPager.OnPageChangeListener,
        TabHost.OnTabChangeListener {

    public static final int TAB_LOGIN = 0;
    public static final int TAB_REG = 1;
    private TabHost tabHost;
    private int currentTab = TAB_LOGIN;
    private ViewPager viewPager;
    private List<Fragment> fragments;

    @Override
    protected int setLayoutResourceID() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);
        tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(this);
        fragments = new ArrayList<>();
        fragments.add(new BackupAppFragment());
        fragments.add(new RestoreAppFragment());
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        TabFragmentAdapter pageAdapter = new TabFragmentAdapter(getChildFragmentManager(),
                fragments, getArguments());
        pageAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pageAdapter);
        setupTabs();
    }

    private void setupTabs() {
        tabHost.setup();
        tabHost.addTab(newTab(R.string.tab_1));
        tabHost.addTab(newTab(R.string.tab_2));

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            final View view = tabHost.getTabWidget().getChildTabViewAt(i);
            final View textView = view.findViewById(android.R.id.title);
            ((TextView) textView).setTextColor(getResources().getColor(R.color.white));
            ((TextView) textView).setHeight(100);
            ((TextView) textView).setTextSize(20);
            ((TextView) textView).setTextSize(20);
            ((TextView) textView).setSingleLine(true);
        }
        tabHost.setOnTabChangedListener(ManagerAppFragment.this);
        tabHost.setCurrentTab(currentTab);
    }

    private TabHost.TabSpec newTab(int titleId) {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(getString(titleId));
        tabSpec.setIndicator(getString(titleId));
        tabSpec.setContent(new TabFactory(getActivity()));

        return tabSpec;
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    @Override
    public void onPageScrolled(int position, float arg1, int arg2) {

    }

    //选择了某个标签
    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onTabChanged(String tabId) {
        currentTab = tabHost.getCurrentTab();
        viewPager.setCurrentItem(currentTab);
        updateTab();
    }

    @SuppressWarnings("unused")
    private void updateTab() {
        switch (currentTab) {
            case TAB_REG:
                RestoreAppFragment login = (RestoreAppFragment) fragments.get(currentTab);
                break;
            case TAB_LOGIN:
                BackupAppFragment register = (BackupAppFragment) fragments
                        .get(currentTab);
                break;
        }
    }

    @Override
    protected void setUpData() {
        super.setUpData();

    }

    class TabFactory implements TabHost.TabContentFactory {

        private final Context context;

        public TabFactory(Context context) {
            this.context = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(context);
            v.setMinimumHeight(0);
            v.setMinimumWidth(0);
            return v;
        }

    }
}
