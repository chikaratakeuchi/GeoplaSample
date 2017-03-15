/*
 * Copyright © 2016年 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.example.nttdocomo.geoplasample.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.example.nttdocomo.geoplasample.R;
import com.geopla.geopop.sdk.GeopopUtil;
import com.geopla.geopop.sdk.model.UserNotification;

import jp.iridge.popinfo.sdk.PopinfoUiUtils;
import jp.iridge.popinfo.sdk.PopinfoUtils;
import jp.iridge.popinfo.sdk.baseui.PopinfoBaseList;
import jp.iridge.popinfo.sdk.data.PopinfoMessage;

/**
 * お知らせ一覧画面クラスです。
 *
 * @version 1.0
 */
public final class GeopopListActivity extends PopinfoBaseList {

    private ListView mListView;

    /**
     * お知らせ一覧を閲覧中にpush通知を受信時のコールバック ※こちらでお知らせ一覧更新処理を行ってください。
     */
    @Override
    protected void onMessageUpdated() {
        // ListView更新
        if (mListView != null) {
            setupDefaultListAdapter();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // ListView更新
        if (mListView != null) {
            setupDefaultListAdapter();
        }
    }

    /**
     * お知らせ一覧初期設定完了コールバック ※こちらでViewの値や動作設定等を行ってください。
     */
    @Override
    protected void onInitialized(boolean result) {
        // popinfoお知らせ一覧デフォルトレイアウト設定
        setContentView(R.layout.geopop_list);

        // popinfoお知らせ一覧デフォルト各パーツ設定
        setupDefaultWidgets();

        // popinfoお知らせ一覧デフォルトAdapter設定
        setupDefaultListAdapter();

        // 通知バーからこのアプリの通知を消去
        PopinfoUiUtils.clearNotification(this);
    }

    /**
     * popinfoお知らせ一覧デフォルトUIコンポーネント設定する。
     */
    private void setupDefaultWidgets() {

        // ヘッダー部分のバック画像ボタンの動作設定
        ImageButton imgBtnBack = (ImageButton) findViewById(R.id.popinfo_imgbtn_back);
        imgBtnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PopinfoUiUtils.goToMainActivity(GeopopListActivity.this);
            }
        });

        // ヘッダー部分の設定画像ボタンの動作設定
        ImageButton imgBtnSettings = (ImageButton) findViewById(R.id.popinfo_imgbtn_settings);
        imgBtnSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PopinfoUiUtils.showPopinfoSettings(GeopopListActivity.this);
            }
        });

        // ListView設定
        mListView = getPopinfoListView();

    }

    /**
     * popinfoお知らせ一覧デフォルトAdapterをセットする。
     */
    private void setupDefaultListAdapter() {
        // お知らせ一覧データカーソルを取得
        ArrayList<UserNotification> userNotifications = GeopopUtil.getUserNotifications(this);
        List<PopinfoMessage> popinfos = PopinfoUtils.getPopinfoMessages(this);
        // Adapter設定
        ListAdapter adapter = new GeopopListAdapter(this, userNotifications);
        mListView.setAdapter(adapter);
    }

    /**
     * popinfoお知らせ一覧デフォルトListViewを取得する。
     *
     * @return ListView
     */
    private ListView getPopinfoListView() {
        if (mListView != null) {
            return mListView;
        }

        // ListActivityとの互換性のため、androidパッケージのIDを使用
        ListView listView = (ListView) findViewById(
                getResources().getIdentifier("list", "id", "android"));
        listView.setEmptyView(findViewById(
                getResources().getIdentifier("empty", "id", "android")));

        // ListView選択時の動作設定
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserNotification userNotification = (UserNotification) mListView.getAdapter().getItem(position);
                if (GeopopUtil.isLocalPushMessage(userNotification)) {
                    GeopopMessageActivity.startActivity(GeopopListActivity.this, GeopopMessageActivity.class, userNotification);

                } else {
                    // お知らせ詳細画面を表示する
                    PopinfoUiUtils.showMessageView(GeopopListActivity.this, userNotification.getId());
                }
            }
        });
        return listView;
    }

}
