/*
 * Copyright © 2016年 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.example.nttdocomo.geoplasample.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.nttdocomo.geoplasample.R;
import com.geopla.geopop.sdk.ui.BaseGeopopSegment;

import jp.iridge.popinfo.sdk.PopinfoUtilsAsync;
import jp.iridge.popinfo.sdk.callback.PopinfoAsyncCallback;

/**
 * popinfo属性設定画面クラスです。
 *
 * @version 1.0
 */
public final class GeopopSegment extends BaseGeopopSegment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // popinfo属性設定画面デフォルトレイアウト設定
        setContentView(R.layout.geopop_segment);

        // 非同期で属性設定フォームHTMLを取得する
        PopinfoUtilsAsync.getSegmentForm(this, new PopinfoAsyncCallback<String>() {

            @Override
            public void onResponse(String result) {

                // HTML取得できなかった場合はエラー処理
                if (result == null) {
                    showSegmentErrorDialog();
                    return;
                }

                // 属性設定画面webview設定
                setupSegmentFormView(result);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 属性設定フォーム画面を設定します。
     *
     * @param html
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void setupSegmentFormView(String html) {
        WebView webView = (WebView) findViewById(R.id.webview_segment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // ローカルファイルのアクセスを禁止
            webView.getSettings().setAllowFileAccessFromFileURLs(false);
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                super.shouldOverrideUrlLoading(view, url);
                // 属性設定画面の終了URLの場合はactivityを終了する
                if (!TextUtils.equals(url, "end.html")) {
                    finish();
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // 読み込み中の画面を切り替える
                findViewById(R.id.loading).setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }

        });

        // 属性設定フォームを表示する
        super.loadData(this, webView, html);

    }

}
