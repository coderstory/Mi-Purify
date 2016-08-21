package com.coderstory.Purify.fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.coderstory.Purify.R;

import java.io.File;

import ren.solid.library.fragment.WebViewFragment;
import ren.solid.library.utils.SnackBarUtils;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:17:46
 */
public class BlogFragment extends WebViewFragment  {


    @Override
    public String getLoadUrl() {
        return "http://blog.coderstory.cn";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_webview_toolbar, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId()==R.id.action_copy){
          ClipboardManager myClipboard;
          myClipboard = (ClipboardManager)getActivity().getSystemService(CLIPBOARD_SERVICE);
          ClipData myClip;
          String text =mWebView.getUrl();
          myClip = ClipData.newPlainText("text", text);
          myClipboard.setPrimaryClip(myClip);
          SnackBarUtils.makeLong(getView(), "已复制网址到粘贴板！").show();
      }else if (item.getItemId()==R.id.action_share){
          shareMsg("网址分享",mWebView.getTitle(),mWebView.getUrl(),null);
      }
        return false;
    }

    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);

            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));

    }
}
