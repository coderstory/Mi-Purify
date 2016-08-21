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

    public AppInfoAdapter(Context context, int textViewResourceId, int disable, List<AppInfo> objects)
    {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView (int position , View convertView, ViewGroup parent)
    {
        AppInfo appInfo = (AppInfo) getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.app_image);
        TextView textView= (TextView) view.findViewById(R.id.app_name);

        textView.setTag(appInfo.getPackageName());
        imageView.setImageDrawable(appInfo.getImageId());
        textView.setText(" 应用名 : "+appInfo.getName()+"\r\n 版本号 : "+appInfo.getVersion());
        if(appInfo.getDisable()){
            view.setBackgroundColor(Color.parseColor("#d0d7d7d7")); //冻结的颜色
        }
        return  view;
    }
}
