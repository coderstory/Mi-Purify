package ren.solid.library.fragment.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;


/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:11:30
 */
public abstract class BaseFragment extends Fragment {

    private View mContentView;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayoutResourceID(), container, false);//setContentView(inflater, container);
        mContext = getContext();
        mProgressDialog = new ProgressDialog(getMContext());
        mProgressDialog.setCanceledOnTouchOutside(false);
        setHasOptionsMenu(true);
        init();
        setUpView();
        setUpData();
        getPrefs();
        return mContentView;
    }

    protected abstract int setLayoutResourceID();

    protected void setUpData() {

    }

    protected SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = prefs.edit();
        }
        return editor;

    }

    protected SharedPreferences getPrefs() {

//        String  a="3149a6fe037f6ccd515fd229df6bebc4";

//        if (prefs == null) {
//         String ab=   getSign(getMContext());
//            if (a.equals(ab)) {
                prefs = getContext().getSharedPreferences("UserSettings", Context.MODE_WORLD_READABLE);
//            }else {
//                prefs = getContext().getSharedPreferences("UserSettings1", Context.MODE_WORLD_READABLE);
//            }
//        }
        return prefs;
    }

    private String getSign(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        while (iter.hasNext()) {
            PackageInfo packageinfo = iter.next();
            String packageName = packageinfo.packageName;
            if (packageName.equals("com.coderstory.Purify")) {
                Log.d("", "getSign: " + packageinfo.signatures[0].toCharsString());
                String a= packageinfo.signatures[0].toCharsString();
                return getMd5(a);
            }
        }
        return null;
    }



        //静态方法，便于作为工具类
        public static String getMd5(String plainText) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(plainText.getBytes());
                byte b[] = md.digest();

                int i;

                StringBuffer buf = new StringBuffer("");
                for (int offset = 0; offset < b.length; offset++) {
                    i = b[offset];
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        buf.append("0");
                    buf.append(Integer.toHexString(i));
                }
                //32位加密
                return buf.toString();
                // 16位的加密
                //return buf.toString().substring(8, 24);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }

        }

        protected void init() {

    }

    protected void setUpView() {
    }

    protected <T extends View> T $(int id) {
        return (T) mContentView.findViewById(id);
    }

    // protected abstract View setContentView(LayoutInflater inflater, ViewGroup container);

    protected View getContentView() {
        return mContentView;
    }

    public Context getMContext() {
        return mContext;
    }

    protected ProgressDialog getProgressDialog() {
        return mProgressDialog;
    }
}
