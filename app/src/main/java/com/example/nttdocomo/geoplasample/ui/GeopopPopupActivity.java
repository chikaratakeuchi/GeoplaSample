/*
 * Copyright © 2016年 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.example.nttdocomo.geoplasample.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nttdocomo.geoplasample.R;
import com.geopla.geopop.sdk.model.UserNotification;
import com.geopla.geopop.sdk.ui.BaseGeopopPopupActivity;

import jp.iridge.popinfo.sdk.PopinfoUtils;

/**
 * PUSH通知画面クラスです。<br>
 * PopinfoPopup継承クラスを使用する場合は、PopinfoCallbackクラスにある<br>
 * public Class<?> updatePopupActivity(Context context) ※PUSH受信時に開くクラスを制御するコールバック<br>
 * を使用してPUSH通知画面の遷移を制御してください。
 *
 * @version 1.0
 */
public final class GeopopPopupActivity extends BaseGeopopPopupActivity {
    private static final String INTENT_USER_NOTIFICATION = "INTENT_USER_NOTIFICATION";

    /**
     * アクティビティの起動
     *
     * @param userNotification
     */
    public static void startActivity(Context context, UserNotification userNotification) {
        Intent intent = new Intent(context, GeopopPopupActivity.class);
        intent.putExtra(INTENT_USER_NOTIFICATION, userNotification);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserNotification userNotification = (UserNotification) getIntent().getSerializableExtra(INTENT_USER_NOTIFICATION);
        initialize(userNotification);

    }

    protected void initialize(final UserNotification userNotification) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.geopop_popup);

        String label = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            label = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        TextView title = (TextView) findViewById(R.id.app_name);
        title.setText(label);

        TextView message = (TextView) findViewById(R.id.message);
        message.setText(userNotification.getMessage());

        ImageView imageView = (ImageView) findViewById(R.id.icon);
        PopinfoUtils.setIcon(this, imageView, userNotification.getIcon());


        // 閉じるボタン
        findViewById(R.id.finish).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 表示ボタン
        findViewById(R.id.show).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // お知らせ詳細を表示する
                GeopopMessageActivity.startActivity(GeopopPopupActivity.this, GeopopMessageActivity.class, userNotification);
                finish();
            }
        });
    }

}
