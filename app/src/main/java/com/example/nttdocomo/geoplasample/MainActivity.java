package com.example.nttdocomo.geoplasample;

import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.geopla.api.GeofencingSdk;
import com.geopla.api.GeofencingSdkSettings;
import com.geopla.api.request.GenreListRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import jp.iridge.popinfo.sdk.Popinfo;

public class MainActivity extends AppCompatActivity {

    String apikey = "apikey_859e018796416cfc34a61114272cee24";
    String accountId = "";
    String clientId = "";

    boolean application_status;

    Button button;
    Button button_f1;
    Button button_f2;
    Button button_f3;
    Button button_f4;

    ListView logListView;
    ArrayAdapter<String> adapter;
    private UpdateReceiver upReceiver;
    private IntentFilter intentFilter;
    final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // アプリの初期化
        initialize();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(application_status == false) {
                    button.setText(R.string.button_stop);
                    //Toast.makeText(MainActivity.this, "ジオフェンスの検知を開始しました。", Toast.LENGTH_SHORT).show();
                    application_status = true;

                    Popinfo.start(MainActivity.this);
                    GeofencingService.start(MainActivity.this);

                    Log.d("LOG","start");
                }else if(application_status == true) {
                    button.setText(R.string.button_start);
                    //Toast.makeText(MainActivity.this, "ジオフェンスの検知を停止しました。", Toast.LENGTH_SHORT).show();
                    application_status = false;

                    Log.d("LOG","stop");
                }
            }
        });

        button_f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLog("テスト");
            }
        });
    }

    private void initialize() {

        GeofencingSdkSettings settings = new GeofencingSdkSettings.Builder()
                .setApiKey(apikey).setMaxLogCount(GeofencingSdkSettings.MAX_LOG_COUNT_UNLIMITED).build();

        Log.d("GEOFENCING", "initialize");
        GeofencingSdk.initialize(getApplicationContext(), settings);

        GeofencingSdk.getInstance().setExternalLogData(accountId, "");
        clientId = GeofencingSdk.getInstance().getClientId();

        application_status = false;

        button = (Button)findViewById(R.id.button);
        button_f1 = (Button)findViewById(R.id.button_f1);
        button_f2 = (Button)findViewById(R.id.button_f2);
        button_f3 = (Button)findViewById(R.id.button_f3);
        button_f4 = (Button)findViewById(R.id.button_f4);

        logListView = (ListView)findViewById(R.id.log_list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        upReceiver = new UpdateReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION");
        registerReceiver(upReceiver, intentFilter);
        upReceiver.registerHandler(updateHandler);

        addLog(clientId);
    }

    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("HANDLER", "received broadcast");

            Bundle bundle = msg.getData();
            String message = bundle.getString("message");

            addLog(message);
        }
    };

    public void addLog(String message) {
        final Date date = new Date(System.currentTimeMillis());

        adapter.add(
                df.format(date) + "\n" +
                        message + "\n");
        logListView.setAdapter(adapter);
    }

    public void addLog(String message, long fenceId, String fenceName, double latitude, double longitude) {
        final Date date = new Date(System.currentTimeMillis());

        adapter.add(
                df.format(date) + "\n" +
                message + "\n" +
                "フェンスID：" + fenceId + "\n" +
                "フェンス名：" + fenceName + "\n" +
                "緯度：" + latitude + " 経度：" + longitude);
        logListView.setAdapter(adapter);
    }
}
