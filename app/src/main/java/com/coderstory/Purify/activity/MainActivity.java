package com.coderstory.Purify.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;

import com.coderstory.Purify.R;
import com.coderstory.Purify.activity.base.BaseActivity;
import com.coderstory.Purify.config.Misc;
import com.coderstory.Purify.fragment.BlogFragment;
import com.coderstory.Purify.fragment.CleanFragment;
import com.coderstory.Purify.fragment.DisbaleAppFragment;
import com.coderstory.Purify.fragment.HideAppFragment;
import com.coderstory.Purify.fragment.HostsFragment;
import com.coderstory.Purify.fragment.ManagerAppFragment;
import com.coderstory.Purify.fragment.OthersFragment;
import com.coderstory.Purify.fragment.SettingsFragment;
import com.coderstory.Purify.fragment.WebViewFragment;
import com.coderstory.Purify.utils.SnackBarUtils;
import com.coderstory.Purify.utils.ViewUtils;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import eu.chainfire.libsuperuser.Shell;

import static com.coderstory.purify.R.id.navigation_view;

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
    private ProgressDialog dialog;
    @SuppressLint("HandlerLeak")
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
                    normalDialog.setTitle("提示");
                    normalDialog.setMessage("请先授权应用ROOT权限");
                    normalDialog.setPositiveButton("确定",
                            (dialog, which) -> System.exit(0));
                    normalDialog.show();
                    super.handleMessage(msg);
                    break;
                case 1:
                    dialog = ProgressDialog.show(MainActivity.this, "检测ROOT权限", "请在ROOT授权弹窗中给与ROOT权限,\n如果长时间无反应则请检查ROOT程序是否被\"省电程序\"干掉");
                    dialog.show();
                    break;
                case 2:
                    if (dialog != null && dialog.isShowing()) {
                        dialog.cancel();
                        getEditor().putBoolean("isRooted", true).apply();
                    }
                    break;
                case 3:
                    android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("本应用尚未再Xposed中启用,请启用后再试...");
                    dialog.setPositiveButton("退出", (dialog12, which) -> {
                        System.exit(0);
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                    break;
            }

        }
    };

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            Log.i("MainActivity", "onRequestPermissionsResult granted=" + granted);
        }
    }

    @Override
    protected void setUpView() {
        mToolbar = $(R.id.toolbar);
        mDrawerLayout = $(R.id.drawer_layout);
        mNavigationView = $(navigation_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(MainActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                requestCameraPermission();
            }
        }

        mToolbar.setTitle(getString(R.string.othersettings));

        //这句一定要在下面几句之前调用，不然就会出现点击无反应
        setSupportActionBar(mToolbar);
        setNavigationViewItemClickListener();
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mToolbar.setNavigationIcon(R.drawable.ic_drawer_home);
        initDefaultFragment();

        if (!getPrefs().getBoolean("isRooted", false)) {
            // 检测弹窗
            new Thread(() -> {
                Message msg = new Message();
                msg.arg1 = 1;
                myHandler.sendMessage(msg);
                if (!Shell.SU.available()) {
                    msg = new Message();
                    msg.arg1 = 0;
                    myHandler.sendMessage(msg);
                } else {
                    msg = new Message();
                    msg.arg1 = 2;
                    myHandler.sendMessage(msg);
                }
                checkEnable();
            }).start();
        } else {
            checkEnable();
        }
    }

    private void checkEnable() {
        if (MainActivity.this.getSharedPreferences(Misc.SharedPreferencesName, Context.MODE_PRIVATE).getBoolean("enableCheck", true) && !isEnable()) {
            SnackBarUtils.makeLong(mNavigationView, "插件尚未激活,Xposed功能将不可用,请重启再试！").show();
        }
    }

    //init the default checked fragment
    private void initDefaultFragment() {
        mCurrentFragment = ViewUtils.createFragment(OthersFragment.class);
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

                case R.id.navigation_item_hosts:
                    mToolbar.setTitle(R.string.hosts);
                    switchFragment(HostsFragment.class);
                    break;

                case R.id.navigation_item_settings:
                    mToolbar.setTitle(R.string.others_appsettings);
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

                case R.id.navigation_item_otherssettings:
                    mToolbar.setTitle(R.string.othersettings);
                    switchFragment(OthersFragment.class);
                    break;
                case R.id.navigation_item_Blog:
                    mToolbar.setTitle(R.string.blog);
                    switchFragment(BlogFragment.class);
                    break;
                default:
                    break;
            }
            item.setChecked(true);
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mPreMenuItem = item;
            return false;
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
    public void onBackPressed() {
        //当前抽屉是打开的，则关闭
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
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
