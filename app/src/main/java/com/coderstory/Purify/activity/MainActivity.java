package com.coderstory.Purify.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.coderstory.Purify.R;
import com.coderstory.Purify.fragment.AboutFragment;
import com.coderstory.Purify.fragment.BisbaleAppFragment;
import com.coderstory.Purify.fragment.BlockADSFragment;
import com.coderstory.Purify.fragment.BlogFragment;
import com.coderstory.Purify.fragment.ChangeSkinFragment;
import com.coderstory.Purify.fragment.CleanFragment;
import com.coderstory.Purify.fragment.HostsFragment;
import com.coderstory.Purify.fragment.SettingsFragment;
import com.coderstory.Purify.fragment.crackThemeFragment;
import com.coderstory.Purify.utils.MyConfig;
import com.coderstory.Purify.utils.root.SuHelper;

import ren.solid.library.activity.base.BaseActivity;
import ren.solid.library.fragment.WebViewFragment;
import ren.solid.library.utils.SnackBarUtils;
import ren.solid.library.utils.ViewUtils;

import static com.coderstory.Purify.R.id.navigation_view;
import static com.coderstory.Purify.utils.root.SuHelper.canRunRootCommands;

public class MainActivity extends BaseActivity {

    private static String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;//侧边菜单视图
    private ActionBarDrawerToggle mDrawerToggle;  //菜单开关
    private Toolbar mToolbar;
    private NavigationView mNavigationView;//侧边菜单项

    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private MenuItem mPreMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        mFragmentManager = getSupportFragmentManager();


    }

    public boolean isEnable() {
        return false;
    }

    @Override
    protected void setUpView() {

        mToolbar = $(R.id.toolbar);
        mDrawerLayout = $(R.id.drawer_layout);
        mNavigationView = $(navigation_view);

        if (MainActivity.this.getSharedPreferences("UserSettings", Context.MODE_WORLD_READABLE).getBoolean("enableCheck", true) && !isEnable()) {
            SnackBarUtils.makeLong(mNavigationView, "插件尚未激活,Xposed功能将不可用,请重启再试！").show();
        }

        mToolbar.setTitle("关于应用");

        //这句一定要在下面几句之前调用，不然就会出现点击无反应
        setSupportActionBar(mToolbar);
        setNavigationViewItemClickListener();
        //ActionBarDrawerToggle配合Toolbar，实现Toolbar上菜单按钮开关效果。
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mToolbar.setNavigationIcon(R.drawable.ic_drawer_home);
        initDefaultFragment();
        dynamicAddSkinEnableView(mToolbar, "background", R.color.colorPrimary);
        dynamicAddSkinEnableView(mNavigationView.getHeaderView(0), "background", R.color.colorPrimary);
        dynamicAddSkinEnableView(mNavigationView, "navigationViewMenu", R.color.colorPrimary);

        //取消滚动条
        NavigationView v = (NavigationView) findViewById(R.id.navigation_view);
        v.setEnabled(false);
        v.setClickable(false);
        if (v != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) v.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
        if (getString(R.string.Limit).equals("True")) {
            Menu m = v.getMenu();
            MenuItem mi = m.getItem(1);
            mi.setVisible(false);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }

    //init the default checked fragment

    private void initDefaultFragment() {
        Log.i(TAG, "initDefaultFragment");
        mCurrentFragment = ViewUtils.createFragment(AboutFragment.class);
        mFragmentManager.beginTransaction().add(R.id.frame_content, mCurrentFragment).commit();
        mPreMenuItem = mNavigationView.getMenu().getItem(0);
        mPreMenuItem.setChecked(true);
    }


    private void setNavigationViewItemClickListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (null != mPreMenuItem) {
                    mPreMenuItem.setChecked(false);
                }

                if (MyConfig.isProcessing) {
                  //  Toast.makeText(MainActivity.this, "请耐心等待应用气力完成再进行其他的操作！", Toast.LENGTH_SHORT).show();
                    SnackBarUtils.makeShort(mDrawerLayout, "请耐心等待应用清理完成再进行其他的操作！").danger();
                    return false;
                }

                switch (item.getItemId()) {

                    case R.id.navigation_item_blockads:
                        mToolbar.setTitle("净化广告");
                        switchFragment(BlockADSFragment.class);
                        break;
                    case R.id.navigation_item_myblog:
                        mToolbar.setTitle("我的博客");
                        switchFragment(BlogFragment.class);
                        break;
                    case R.id.navigation_item_hosts:
                        if (!canRunRootCommands()) {
                            Toast.makeText(MainActivity.this, "尚未获取Root授权,Hosts功能将不可用！", Toast.LENGTH_LONG).show();
                        } else {
                            mToolbar.setTitle("Hosts设置");
                            switchFragment(HostsFragment.class);
                        }
                        break;

                    case R.id.navigation_item_cracktheme:
                        mToolbar.setTitle("主题和谐");
                        switchFragment(crackThemeFragment.class);
                        break;

                    case R.id.navigation_item_settings:
                        mToolbar.setTitle("其他设置");
                        switchFragment(SettingsFragment.class);
                        break;
                    case R.id.navigation_item_switch_theme:
                        mToolbar.setTitle("主题换肤");
                        switchFragment(ChangeSkinFragment.class);
                        break;
                    case R.id.navigation_item_about:
                        mToolbar.setTitle("关于");
                        switchFragment(AboutFragment.class);
                        break;

                    case R.id.navigation_item_Clean:
                        mToolbar.setTitle("应用清理");
                        switchFragment(CleanFragment.class);
                        break;

                    case R.id.navigation_item_disableapps:
                        mToolbar.setTitle("冻结应用");
                        switchFragment(BisbaleAppFragment.class);
                        break;
                    default:
                        break;
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                mPreMenuItem = item;
                return false;
            }
        });
    }

    //切换Fragment
    private void switchFragment(Class<?> clazz) {
        Fragment to = ViewUtils.createFragment(clazz);
        if (to.isAdded()) {
            Log.i(TAG, "Added");
            //mFragmentManager.beginTransaction().hide(mCurrentFragment).show(to).commitAllowingStateLoss();
            mFragmentManager.beginTransaction().replace(mCurrentFragment.getId(), to).commit();
        } else {
            Log.i(TAG, "Not Added");
            //mFragmentManager.beginTransaction().hide(mCurrentFragment).add(R.id.frame_content, to).commitAllowingStateLoss();
            mFragmentManager.beginTransaction().replace(mCurrentFragment.getId(), to).commit();
        }
        mCurrentFragment = to;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivityWithoutExtras(AboutActivity.class);
        } else if (id == R.id.action_reboot) {
            SuHelper.showTips("busybox killall system_server", getString(R.string.Tips_HotBoot), this);
        }


        return super.onOptionsItemSelected(item);
    }


    private long lastBackKeyDownTick = 0;
    public static final long MAX_DOUBLE_BACK_DURATION = 1500;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {//当前抽屉是打开的，则关闭
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }

        if (mCurrentFragment instanceof WebViewFragment) {//如果当前的Fragment是WebViewFragment 则监听返回事件
            WebViewFragment webViewFragment = (WebViewFragment) mCurrentFragment;
            if (webViewFragment.canGoBack()) {
                webViewFragment.goBack();
                return;
            }
        }

        long currentTick = System.currentTimeMillis();
        if (currentTick - lastBackKeyDownTick > MAX_DOUBLE_BACK_DURATION) {
            SnackBarUtils.makeShort(mDrawerLayout, "再按一次退出").success();
            lastBackKeyDownTick = currentTick;
        } else {
            finish();
            System.exit(0);
        }
    }
}
