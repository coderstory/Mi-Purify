package com.coderstory.Purify.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.coderstory.Purify.R;
import com.coderstory.Purify.config.Misc;
import com.coderstory.Purify.fragment.BlockADSFragment;
import com.coderstory.Purify.fragment.CleanFragment;
import com.coderstory.Purify.fragment.DisbaleAppFragment;
import com.coderstory.Purify.fragment.HideAppFragment;
import com.coderstory.Purify.fragment.HostsFragment;
import com.coderstory.Purify.fragment.ManagerAppFragment;
import com.coderstory.Purify.fragment.SettingsFragment;
import com.coderstory.Purify.fragment.WebViewFragment;
import com.coderstory.Purify.utils.SnackBarUtils;
import com.coderstory.Purify.utils.ViewUtils;

import static com.coderstory.Purify.R.id.navigation_view;
import static com.coderstory.Purify.utils.roothelper.SuHelper.canRunRootCommands;

public class MainActivity extends BaseActivity {

    public static final long MAX_DOUBLE_BACK_DURATION = 1500;
    private static final int READ_EXTERNAL_STORAGE_CODE = 1;
    private DrawerLayout mDrawerLayout;//侧边菜单视图
    private Toolbar mToolbar;
    private NavigationView mNavigationView;//侧边菜单项
    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private MenuItem mPreMenuItem;
    private long lastBackKeyDownTick = 0;

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

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MainActivity.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            Log.i("MainActivity", "onRequestPermissionsResult granted=" + granted);
        }
    }

    @Override
    protected void setUpView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(MainActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                requestCameraPermission();
            }
        }
        mToolbar = $(R.id.toolbar);
        mDrawerLayout = $(R.id.drawer_layout);
        mNavigationView = $(navigation_view);

        if (getPrefs().getBoolean("enableCheck", true) && !isEnable()) {
            SnackBarUtils.makeLong(mNavigationView, "插件尚未激活,Xposed功能将不可用,请重启再试！").show();
        }

        if (!canRunRootCommands()) {
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(MainActivity.this);
            normalDialog.setTitle("提示");
            normalDialog.setMessage("请先授权应用ROOT权限");
            normalDialog.setPositiveButton("确定",
                    (dialog, which) -> System.exit(0));

            // 显示
            normalDialog.show();
        }

        mToolbar.setTitle("净化广告");

        //这句一定要在下面几句之前调用，不然就会出现点击无反应
        setSupportActionBar(mToolbar);
        setNavigationViewItemClickListener();
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mToolbar.setNavigationIcon(R.drawable.ic_drawer_home);
        initDefaultFragment();

        //取消滚动条
        NavigationView v = findViewById(R.id.navigation_view);
        v.setEnabled(false);
        v.setClickable(false);
        NavigationMenuView navigationMenuView = (NavigationMenuView) v.getChildAt(0);
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
    }

    //init the default checked fragment
    private void initDefaultFragment() {
        mCurrentFragment = ViewUtils.createFragment(BlockADSFragment.class);
        mFragmentManager.beginTransaction().add(R.id.frame_content, mCurrentFragment).commit();
        mPreMenuItem = mNavigationView.getMenu().getItem(0);
        mPreMenuItem.setChecked(true);
    }

    public boolean isEnable() {
        return false;
    }

    private void setNavigationViewItemClickListener() {
        mNavigationView.setNavigationItemSelectedListener(item -> {
            if (null != mPreMenuItem) {
                mPreMenuItem.setChecked(false);
            }

            if (Misc.isProcessing) {
                SnackBarUtils.makeShort(mDrawerLayout, getString(R.string.isWorkingTips)).danger();
                return false;
            }

            switch (item.getItemId()) {
                case R.id.navigation_item_blockads:
                    mToolbar.setTitle(R.string.MIUI_Purify);
                    switchFragment(BlockADSFragment.class);
                    break;

                case R.id.navigation_item_hosts:
                    mToolbar.setTitle(R.string.hosts);
                    switchFragment(HostsFragment.class);
                    break;

                case R.id.navigation_item_settings:
                    mToolbar.setTitle(R.string.othersettings);
                    switchFragment(SettingsFragment.class);
                    break;

                case R.id.navigation_item_Clean:
                    mToolbar.setTitle(R.string.appclean);
                    switchFragment(CleanFragment.class);
                    break;

                case R.id.navigation_item_disableapps:
                    mToolbar.setTitle(R.string.disableapp);
                    switchFragment(DisbaleAppFragment.class);
                    break;
                case R.id.navigation_item_donation:
                    mToolbar.setTitle(R.string.navigation_item_donation);
                    switchFragment(DonationFragment.class);
                    break;
                case R.id.navigation_item_ManagerApp:
                    mToolbar.setTitle(R.string.navigation_item_ManagerApp);
                    switchFragment(ManagerAppFragment.class);
                    break;
                case R.id.navigation_item_hide_app:
                    mToolbar.setTitle(R.string.hide_app_icon);
                    switchFragment(HideAppFragment.class);
                    break;

                default:
                    break;
            }
            item.setChecked(true);
            mDrawerLayout.closeDrawer(Gravity.START);
            mPreMenuItem = item;
            return false;
                    case R.id.navigation_item_disableapps:
                        mToolbar.setTitle(R.string.disableapp);
                        switchFragment(DisbaleAppFragment.class);
                        break;
                    case R.id.navigation_item_about:
                        startActivityWithoutExtras(AboutActivity.class);
                        break;
                    case R.id.navigation_item_ManagerApp:
                        mToolbar.setTitle(R.string.navigation_item_ManagerApp);
                        switchFragment(ManagerAppFragment.class);
                        break;
                    case R.id.navigation_item_hide_app:
                        mToolbar.setTitle(R.string.hide_app_icon);
                        switchFragment(HideAppFragment.class);
                        break;
                    default:
                        break;
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawer(Gravity.START);
                mPreMenuItem = item;
                return false;
            }
        });
    }

    //切换Fragment
    private void switchFragment(Class<?> clazz) {
        Fragment to = ViewUtils.createFragment(clazz);
        if (to.isAdded()) {
            mFragmentManager.beginTransaction().replace(mCurrentFragment.getId(), to).commit();
        } else {
            mFragmentManager.beginTransaction().replace(mCurrentFragment.getId(), to).commit();
        }
        mCurrentFragment = to;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivityWithoutExtras(AboutActivity.class);
        } else if (id == R.id.action_reboot) {
            SuHelper.showTips("busybox killall system_server", getString(R.string.Tips_HotBoot), this);
        } else if (id == R.id.action_blog) {
            mToolbar.setTitle(R.string.myblog);
            switchFragment(BlogFragment.class);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //当前抽屉是打开的，则关闭
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }
        //如果当前的Fragment是WebViewFragment 则监听返回事件
        if (mCurrentFragment instanceof WebViewFragment) {
            WebViewFragment webViewFragment = (WebViewFragment) mCurrentFragment;
            if (webViewFragment.canGoBack()) {
                webViewFragment.goBack();
                return;
            }
        }

        long currentTick = System.currentTimeMillis();
        if (currentTick - lastBackKeyDownTick > MAX_DOUBLE_BACK_DURATION) {
            SnackBarUtils.makeShort(mDrawerLayout, "再按一次退出").info();
            lastBackKeyDownTick = currentTick;
        } else {
            finish();
            System.exit(0);
        }
    }
}
