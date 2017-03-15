/*
 * Copyright © 2016年 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.example.nttdocomo.geoplasample.ui;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nttdocomo.geoplasample.R;
import com.geopla.geopop.sdk.model.UserNotification;
import com.geopla.geopop.sdk.ui.BaseGeopopMessageActivity;

import jp.iridge.popinfo.sdk.PopinfoUiUtils;

/**
 * お知らせ詳細画面クラスです。
 *
 * @version 1.0
 */
public final class GeopopMessageActivity extends BaseGeopopMessageActivity {

    @Override
    protected void initialize(UserNotification userNotification) {
        setContentView(R.layout.geopop_message_view);

        // 各UIコンポーネント設定
        TextView title = (TextView) findViewById(R.id.popinfo_message_title);
        title.setText(userNotification.getTitle());

        // 表示メッセージ設定 (通常メッセージ)
        TextView message = (TextView) findViewById(R.id.popinfo_message);
        message.setVisibility(View.GONE);
        if (TextUtils.equals(userNotification.getContentType(), "text/plain")
                || TextUtils.isEmpty(userNotification.getContentType())) {
            message.setText(userNotification.getContent());
            message.setVisibility(View.VISIBLE);
        }

        // 表示メッセージ設定 (HTMLメッセージ)
        WebView webViewMessage = (WebView) findViewById(R.id.popinfo_html_message);
        webViewMessage.setVisibility(View.GONE);
        // HTML配信の場合
        if (TextUtils.equals(userNotification.getContentType(), "text/html")) {
            setupDefaultWebView(webViewMessage, userNotification);
        }

        // お知らせ一覧へボタン設定
        ImageButton imgBtnShowList = (ImageButton) findViewById(R.id.popinfo_imgbtn_back);
        imgBtnShowList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PopinfoUiUtils.showPopinfoList(GeopopMessageActivity.this);
            }
        });

        // URLを開くボタン設定
        final Button btnOpenUrl = (Button) findViewById(R.id.popinfo_open_url);
        final long messageId = userNotification.getActionId();
        final String url = userNotification.getUrl();

        // URLが設定されている場合は表示する
        if (messageId >= 0 && !TextUtils.isEmpty(url)) {
            btnOpenUrl.setVisibility(View.VISIBLE);
        }
        btnOpenUrl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl();
            }
        });
    }


    /**
     * HTML配信メッセージ表示用のWebviewの設定をする。
     *
     * @param webView
     * @param userNotification
     */
    private void setupDefaultWebView(WebView webView, UserNotification userNotification) {

        // HTML配信メッセージに対応するwebviewのデフォルト設定 (表示最適化、javascript有効、GPS表示有効)
        webView = setPopinfoMessageWebViewSettings(webView, userNotification.getContent());

        // WebViewClientはPopinfoMessageWebViewClientを使用して設定
        webView.setWebViewClient(new PopinfoMessageWebViewClient() {

            @Override
            public void onScaleChanged(final WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                super.shouldOverrideUrlLoading(view, url);

                // WebView内のリンクは外部URLへ遷移
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

                return true;
            }

        });
    }
}
