package com.coderstory.Purify.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coderstory.Purify.R;

import java.util.List;

public class AppInfoAdapter extends ArrayAdapter {
    private int resourceId;

    public AppInfoAdapter(Context context, int textViewResourceId, int disable, List<AppInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppInfo appInfo = (AppInfo) getItem(position);


        View view;
        ViewHolder vh;
        if (convertView != null) { //查询布局是否已经缓存
            view = convertView;
            vh = (ViewHolder) view.getTag();//重新获取ViewHolder

        } else {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null); //读取items.xml文件并实例化
            vh = new ViewHolder();
            vh.myimage = (ImageView) view.findViewById(R.id.app_image);//查找items实例中的myimage
            vh.mytext = (TextView) view.findViewById(R.id.app_name);//查找items实例中的mytext
            view.setTag(vh); //保存到view中
        }


        vh.mytext.setTag(appInfo.getPackageName());
        vh.myimage.setImageDrawable(appInfo.getImageId());
        vh.mytext.setText(" 应用名 : " + appInfo.getName() + "\r\n 版本号 : " + appInfo.getVersion());
        if (appInfo.getDisable()) {
            view.setBackgroundColor(Color.parseColor("#d0d7d7d7")); //冻结的颜色
        } else {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary)); //冻结的颜色
        }
        return view;
    }

    private class ViewHolder {
        ImageView myimage;
        TextView mytext;
    }
}
