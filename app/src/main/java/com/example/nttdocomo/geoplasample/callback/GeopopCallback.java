package com.geopla.geopop.callback;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.geopla.geopop.sdk.callback.GeopopBaseCallback;


/**
 * 以下のようにAndroidManifest.xmlに指定して使用します。<br>
 * {@literal <meta-data android:name="POPINFO_CALLBACK_CLASS"android:value="クラス名" />}<br>
 */
public class GeopopCallback extends GeopopBaseCallback {


    /**
     * オプトインダイアログ開始時にコールバックされます。
     *
     * @param context コンテキスト
     */
    public void onOptInStarted(Context context) {
        super.onOptInStarted(context);
    }

    /**
     * 全てのオプトインダイアログが完了したタイミングでコールバックされます。
     *
     * @param context         コンテキスト
     * @param pushEnabled     オプトインダイアログで選択されたpush通知の有効(true)または無効(false)
     * @param locationEnabled オプトインダイアログで選択された位置情報使用の有効(true)または無効(false)
     */
    public void onOptInEnded(Context context, boolean pushEnabled,
                             boolean locationEnabled) {
        super.onOptInEnded(context, pushEnabled, locationEnabled);
    }

    /**
     * RegistrationIDとpopinfoIDの紐付け完了(PUSH通知受信可能)時にコールバックされます。
     *
     * @param context   コンテキスト
     * @param popinfoId push受信可能になったpopinfo ID
     */
    public void onTokenRegistered(Context context, String popinfoId) {
        super.onTokenRegistered(context, popinfoId);
    }

    /**
     * popinfoIDが確定された時(まだPUSH通知は使用不可能)にコールバックされます。
     *
     * @param context   コンテキスト
     * @param popinfoId 発行されたpopinfo ID
     */
    public void onGotPopinfoId(Context context, String popinfoId) {
        super.onGotPopinfoId(context, popinfoId);
    }

    /**
     * 通知バー、ウィジェットからお知らせ詳細画面へ遷移する時コールバックされます。<br>
     * 独自で作成したお知らせ詳細画面を表示させる場合は、こちらを使用してください。<br>
     * 戻り値で指定したクラスへ遷移します。※デフォルトはPopinfoMessageViewクラスが設定されています。
     *
     * @param context コンテキスト
     * @return {@literal Class<?>} 遷移先クラス
     */
    public Class<?> updateMessageActivity(Context context) {
        return super.updateMessageActivity(context);
    }

    /**
     * PUSH通知受信時した後にポップアップ画面へ遷移する時にコールバックされます。<br>
     * 独自で作成したポップアップ画面を表示させる場合は、こちらを使用してください。<br>
     * 戻り値で指定したクラスへ遷移します。 ※デフォルトはPopinfoPopupクラスが設定されています。
     *
     * @param context コンテキスト
     * @return {@literal Class<?>} 遷移先クラス
     */
    public Class<?> updatePopupActivity(Context context) {
        return super.updatePopupActivity(context);
    }

    /**
     * PUSH通知受信時にコールバックされます。
     *
     * @param context コンテキスト
     * @param intent  受信情報Intent
     */
    public void onReceivedPushMessage(Context context, Intent intent) {
        super.onReceivedPushMessage(context, intent);
    }

    /**
     * PUSH通知受信時にポップアップ画面へ遷移を行う直前のタイミングでコールバックされます。<br>
     * 戻り値をfalseに設定すると、ポップアップを非表示にできます。<br>
     * ※戻り値のデフォルトはtrueが設定されています。
     *
     * @param context コンテキスト
     * @param msg     PUSH通知メッセージ
     * @return true(ポップアップ表示) or false(ポップアップ非表示)
     */
    public boolean onPopupViewAppeared(Context context, String msg) {
        return super.onPopupViewAppeared(context, msg);
    }

    /**
     * popinfoで取得した位置情報がコールバックされます。
     *
     * @param context  コンテキスト
     * @param location Location
     * @see <a href="http://developer.android.com/reference/android/location/Location.html">
     * Locationオブジェクト</a>
     */
    public void onUpdateLocation(Context context, Location location) {
        super.onUpdateLocation(context, location);
    }

    /**
     * iBeacon機器の検知範囲に入った時に登録されているiBeacon機器情報がコールバックされます。<br>
     * ※別途iBeacon機器をpopinfoサーバーへ登録された場合にのみこちらを利用できます。<br>
     *
     * @param context コンテキスト
     * @param uuid    UUID
     * @param rssi    RSSI値 (電波強度)
     * @param major   major値
     * @param minor   minor値
     */
    public void onFindIBeaconDeviceIn(Context context,
                                      String uuid, int rssi, int major, int minor) {
        super.onFindIBeaconDeviceIn(context, uuid, rssi, major, minor);
    }

}
