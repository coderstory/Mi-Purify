package com.coderstory.purify.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coderstory.purify.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class AppInfoAdapter extends ArrayAdapter {
    private int resourceId;


    public AppInfoAdapter(Context context, int textViewResourceId, List<AppInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        AppInfo appInfo = (AppInfo) getItem(position);
        View view;
        ViewHolder vh;
        if (convertView != null) { //查询布局是否已经缓存
            view = convertView;
            vh = (ViewHolder) view.getTag();//重新获取ViewHolder
        } else {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null); //读取items.xml文件并实例化
            vh = new ViewHolder();
            vh.myImage = view.findViewById(R.id.app_image);//查找items实例中的myimage
            vh.myText = view.findViewById(R.id.app_name);//查找items实例中的mytext
            view.setTag(vh); //保存到view中
        }

        vh.myText.setTag(appInfo.getPackageName());
        vh.myImage.setImageDrawable(appInfo.getImageId());
        vh.myText.setText(String.format(getContext().getString(R.string.appname), appInfo.getName(), appInfo.getVersion()));
        if (appInfo.getDisable()) {
            view.setBackgroundColor(Color.parseColor("#d0d7d7d7")); //冻结的颜色
        } else {
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary)); //正常的的颜色
        }
        return view;
    }

    private class ViewHolder {
        ImageView myImage;
        TextView myText;
    }
}
