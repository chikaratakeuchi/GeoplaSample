package com.example.nttdocomo.geoplasample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.geopla.api.group.Genre;
import com.geopla.api.monitoring.gps.GpsEventInfo;
import com.geopla.api.request.Callback;
import com.geopla.api.request.GenreListRequest;
import com.geopla.api.request.RequestError;
import com.geopla.api.util.geofencing.Error;
import com.geopla.api.util.geofencing.GeofencingListener;
import com.geopla.api.util.geofencing.genre.GpsGenreBulkFetchGeofencing;
import com.geopla.api.util.geofencing.genre.GpsGenreBulkFetchGeofencingSettings;
import com.geopla.api.util.geofencing.genre.PrepareCallback;
import com.geopla.geopop.sdk.service.GeopopService;

import java.util.List;

public class GeofencingService extends GeopopService {

    protected static final String ACTION_START = "ACTION_START";

    private GpsGenreBulkFetchGeofencing mGpsGenreGeofencing;
    private List<Genre> mGenres;
    private NotificationManager mNotificationManager;
    private Handler handler;

    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d("SERVICE", "create geofencing service");

        GenreListRequest req = new GenreListRequest.Builder()
                .setTypes(GenreListRequest.TARGET_GPS).build();
        req.execute(new Callback<List<Genre>>() {
            @Override
            public void onComplete(List<Genre> response) {
                mGenres = response;
                if(!mGenres.isEmpty()) {
                    sendBroadCast("ジャンルリストを取得しました。");
                    sendBroadCast("取得したジャンル数：" + mGenres.size());
                    sendBroadCast(mGenres.get(0).getName() + ":" + mGenres.get(1).getName() + ":" + mGenres.get(2).getName());
                }else{
                    sendBroadCast("ジャンルリストを取得できませんでした。");
                }
            }

            @Override
            public void onError(RequestError requestError) {
                Log.e("SERVICE", requestError.toString());
            }
        });

    }

    public static void start(Context context) {
        Log.d("SERVICE", "call start method");
        Intent intent = new Intent();
        intent.setAction(ACTION_START);
        intent.setClass(context, GeofencingService.class);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SERVICE", "call onStartCommand method");
        if(intent != null && intent.getAction() != null) {
            Log.d("SERVICE", "check intent : OK");
            switch(intent.getAction()) {
                case ACTION_START:
                    startGpsGenreGeofencing();

                    //startGeopop();
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopGpsGenreGeofencing();
    }

    private void startGpsGenreGeofencing() {
        mGpsGenreGeofencing = new GpsGenreBulkFetchGeofencing(getApplicationContext(), "gpsGenre.db");

        mGpsGenreGeofencing.prepare(mGenres, new PrepareCallback() {
            @Override
            public void onPrepared() {
                showNotification("検知開始");
                sendBroadCast("検知開始");

                GpsGenreBulkFetchGeofencingSettings gpsSetting = new GpsGenreBulkFetchGeofencingSettings.Builder().setLocationRequestInterval(
                        GpsGenreBulkFetchGeofencingSettings.LOCATION_REQUEST_INTERVAL_HIGH_ACCURACY_IN_MILLIS).build();
                mGpsGenreGeofencing.startGeofencing(gpsSetting, new GeofencingListener<GpsEventInfo>() {
                    @Override
                    public void onEnter(GpsEventInfo gpsEventInfo) {
                        checkEnterGeoPointId(gpsEventInfo.getPoint().getId());
                        //showNotification("侵入を検知");
                        sendBroadCast(gpsEventInfo.getPoint().getName() + "への侵入を検知");
                    }

                    @Override
                    public void onExit(GpsEventInfo gpsEventInfo) {
                        checkExitGeoPointId(gpsEventInfo.getPoint().getId());
                        //showNotification("離脱を検知");
                        sendBroadCast(gpsEventInfo.getPoint().getName() + "からの離脱を検知");
                    }

                    @Override
                    public void onError(Error error) {
                        showNotification("エラーを検知しました。");
                    }
                });
            }

            @Override
            public void onError(Error error) {
                showNotification("サービスでエラーが起きました。");
                Log.e("SERVICE", error.toString());

            }
        });
    }

    private void stopGpsGenreGeofencing() {
        mGpsGenreGeofencing.stopGeofencing();
        sendBroadCast("検知停止");
    }

    protected void sendBroadCast(String message) {
        Log.i("BROADCAST", message);
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra("message", message);
        broadcastIntent.setAction("ACTION");
        getBaseContext().sendBroadcast(broadcastIntent);
    }

    /*
    protected void sendBroadCast(String message, long fenceId, String fenceName, double latitude, double longitude) {

        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra("message", message);
        broadcastIntent.putExtra("id", fenceId);
        broadcastIntent.putExtra("name", fenceName);
        broadcastIntent.putExtra("latitude", latitude);
        broadcastIntent.putExtra("longitude", longitude);
        broadcastIntent.setAction("DETECT");
        getBaseContext().sendBroadcast(broadcastIntent);
    }
    */

    private void showNotification(String message) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        Notification notif = new Notification.Builder(this)
                .setContentTitle("Geopla")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .build();
        notif.flags = Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(1, notif);
    }
}
