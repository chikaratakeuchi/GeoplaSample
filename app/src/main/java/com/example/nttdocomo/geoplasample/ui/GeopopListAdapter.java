/*
 * Copyright © 2016年 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.example.nttdocomo.geoplasample.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.example.nttdocomo.geoplasample.R;
import com.geopla.geopop.sdk.model.UserNotification;
import com.geopla.geopop.sdk.ui.BaseGeopopListAdapter;


/**
 * お知らせ一覧ListView用Adapterクラスです。
 *
 * @version 1.0
 */
final class GeopopListAdapter extends BaseGeopopListAdapter {

    public GeopopListAdapter(Context context, List<UserNotification> objects) {
        super(context, R.layout.geopop_list_row, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holder holder;

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.geopop_list_row, null);
            holder = new Holder();
            holder.title = (TextView) convertView.findViewById(R.id.popinfo_list_title);
            holder.time = (TextView) convertView.findViewById(R.id.popinfo_list_time);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();

        }

        final UserNotification userNotification = getItem(position);

        // 配信アイコンをリスト一覧に表示したい時は以下コードのコメントを解除してImageViewをレイアウトへ追加してください
//        ImageView imageView = (ImageView) view.findViewById(R.id.popinfo_list_icon);
//        if (imageView != null) {
//            String icon = cursor.getString(cursor.getColumnIndex(ICON));
//            PopinfoUtils.setIcon(context, imageView, icon);
//        }

        holder.title.setText(userNotification.getTitle());
        holder.time.setText(getFriendlyTime(getContext(), userNotification.getSendTime()));

        // 未読・既読で背景色を変える
        setPopinfoListStyle(getContext(), convertView, holder.title, userNotification.isRead());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getActionId();
    }

    /**
     * ホルダー
     */
    private class Holder {
        TextView title;
        TextView time;
    }
}
