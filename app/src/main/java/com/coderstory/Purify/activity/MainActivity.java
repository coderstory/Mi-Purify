package com.coderstory.Purify.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
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

import com.coderstory.Purify.R;
import com.coderstory.Purify.fragment.AboutFragment;
import com.coderstory.Purify.fragment.BlockADSFragment;
import com.coderstory.Purify.fragment.BlogFragment;
import com.coderstory.Purify.fragment.CleanFragment;
import com.coderstory.Purify.fragment.DisbaleAppFragment;
import com.coderstory.Purify.fragment.DonationFragment;
import com.coderstory.Purify.fragment.HostsFragment;
import com.coderstory.Purify.fragment.ManagerAppFragment;
import com.coderstory.Purify.fragment.SettingsFragment;
import com.coderstory.Purify.fragment.crackThemeFragment;
import com.coderstory.Purify.nohttp.HttpListener;
import com.coderstory.Purify.utils.MD5Utils;
import com.coderstory.Purify.utils.MyConfig;
import com.coderstory.Purify.utils.root.SuHelper;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import ren.solid.library.fragment.WebViewFragment;
import ren.solid.library.utils.SnackBarUtils;
import ren.solid.library.utils.ViewUtils;

import static com.coderstory.Purify.R.id.navigation_view;

public class MainActivity extends BaseActivity {

    private static String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;//侧边菜单视图
    private Toolbar mToolbar;
    private NavigationView mNavigationView;//侧边菜单项

    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private MenuItem mPreMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoHttp.initialize(this);
        Logger.setDebug(true);


        Request<JSONObject> request = NoHttp.createJsonObjectRequest("https://blog.coderstory.cn/updater-info111", RequestMethod.GET);
        request(1, request, objectListener, true, true);

    }
    private HttpListener<JSONObject> objectListener = new HttpListener<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            JSONObject jsonObject = response.get();
            if (0 == jsonObject.optInt("error", -1)) {
                String result = "";
                result = String.format(Locale.getDefault(), result, response.request().getRequestMethod().toString(), jsonObject.optString("url"), jsonObject.optString("data"), jsonObject.optString("error"));
            //    mTvResult.setText(result);
            }
            Log.d(TAG, "onSucceed: ");

            try {
                String b=response.get().getString("URL");
                boolean c=response.get().has("Version");
                String ccc=":";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {
            Log.d(TAG, "onSucceed: ");
         //   showMessageDialog(R.string.request_failed, response.getException().getMessage());
        }
    };







    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        mFragmentManager = getSupportFragmentManager();


    }



    private static final int READ_EXTERNAL_STORAGE_CODE = 1;
    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MainActivity.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

        if (MainActivity.this.getSharedPreferences("UserSettings", Context.MODE_WORLD_READABLE).getBoolean("enableCheck", true) && !isEnable()) {
            SnackBarUtils.makeLong(mNavigationView, "插件尚未激活,Xposed功能将不可用,请重启再试！").show();
        }

        mToolbar.setTitle("净化广告");

        //这句一定要在下面几句之前调用，不然就会出现点击无反应
        setSupportActionBar(mToolbar);
        setNavigationViewItemClickListener();
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mToolbar.setNavigationIcon(R.drawable.ic_drawer_home);
        initDefaultFragment();


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
        String keyword= MD5Utils.MD5(getString(R.string.version));
        if (!getString(R.string.pwd).equals(keyword )) {
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

    private void initDefaultFragment() {
        Log.i(TAG, "initDefaultFragment");
        mCurrentFragment = ViewUtils.createFragment(BlockADSFragment.class);
        mFragmentManager.beginTransaction().add(R.id.frame_content, mCurrentFragment).commit();
        mPreMenuItem = mNavigationView.getMenu().getItem(0);
        mPreMenuItem.setChecked(true);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }

    //init the default checked fragment


    public boolean isEnable() {
        return false;
    }

    private void setNavigationViewItemClickListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (null != mPreMenuItem) {
                    mPreMenuItem.setChecked(false);
                }

                if (MyConfig.isProcessing) {
                    SnackBarUtils.makeShort(mDrawerLayout, getString(R.string.isWorkingTips)).danger();
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
                        mToolbar.setTitle("Hosts设置");
                        switchFragment(HostsFragment.class);
                        break;

                    case R.id.navigation_item_cracktheme:
                        mToolbar.setTitle("主题和谐");
                        switchFragment(crackThemeFragment.class);
                        break;

                    case R.id.navigation_item_settings:
                        mToolbar.setTitle("其他设置");
                        switchFragment(SettingsFragment.class);
                        break;

                    case R.id.navigation_item_about:
                        mToolbar.setTitle("关于应用");
                        switchFragment(AboutFragment.class);
                        break;

                    case R.id.navigation_item_Clean:
                            mToolbar.setTitle("应用清理");
                            switchFragment(CleanFragment.class);
                        break;

                    case R.id.navigation_item_disableapps:
                        mToolbar.setTitle("冻结应用");
                        switchFragment(DisbaleAppFragment.class);
                        break;
                    case R.id.navigation_item_donation:
                        mToolbar.setTitle("捐赠");
                        switchFragment(DonationFragment.class);
                        break;
                    case R.id.navigation_item_ManagerApp:
                        mToolbar.setTitle("应用管理");
                        switchFragment(ManagerAppFragment.class);
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

        int id = item.getItemId();

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
            SnackBarUtils.makeShort(mDrawerLayout, "再按一次退出").info();
            lastBackKeyDownTick = currentTick;
        } else {
            finish();
            System.exit(0);
        }
    }
}
