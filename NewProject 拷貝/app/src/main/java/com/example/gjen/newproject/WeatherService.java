package com.example.gjen.newproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.logging.Handler;

/**
 * Created by GJen on 2016/10/4.
 */

public class WeatherService extends Service {

    Date curDate = new Date(System.currentTimeMillis());
    int hour = curDate.getHours();
    private android.os.Handler handler = new android.os.Handler();
    NotificationManager ntfMgr;
    final int NTF_ID = 1000;

    String code,text;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ntfMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        downloadWeather();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        handler.postDelayed(runnable,1000); //1秒
        handler.postDelayed(rainRun,1000); //1秒
//        handler.postDelayed(runnable,3600000); //1小時
//        handler.postDelayed(rainRun, 300000); //5分鐘
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(rainRun);
    }

    void downloadWeather(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    // 建立連線
                    URL url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20woeid%20%3D%202306181&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.connect();
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    // 讀取資料
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            conn.getInputStream(), "UTF-8"));
                    String jsonString1 = reader.readLine();
                    reader.close();
                    // 解析 json
                    JSONObject jsonObj = new JSONObject(jsonString1);
                    JSONObject jsonObj2 = new JSONObject(jsonObj.getString("query"));
                    JSONObject jsonObj3 = new JSONObject(jsonObj2.getString("results"));
                    JSONObject jsonObj4 = new JSONObject(jsonObj3.getString("channel"));
                    JSONObject jsonObj5 = new JSONObject(jsonObj4.getString("item"));
                    JSONObject jsonObj6 = new JSONObject(jsonObj5.getString("condition"));
                    String date = jsonObj6.getString("date");
                    text = jsonObj6.getString("text");
                    code = jsonObj6.getString("code");

                }
                catch (Exception e) {
                    Log.i("123","Exception : "+e.toString());
                }
                finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkTime();
            handler.postDelayed(this, 1000); //1秒
//            handler.postDelayed(this, 3600000); //1小時
        }
    };

    private Runnable rainRun = new Runnable() {
        @Override
        public void run() {
            checkText();
            handler.postDelayed(this, 1000); //1秒
//            handler.postDelayed(this, 300000); //5分鐘
        }
    };

    void checkTime(){
        if(hour == 8){
//            //做提醒
//            Notification notification = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("今日天氣")
//                    .setContentText("天氣:"+text+"溫度:"+code)
//                    .build();
//            ntfMgr.notify(NTF_ID,notification);
            Log.i("abc","checkTime True");
            Toast.makeText(this,"checkTime True",Toast.LENGTH_SHORT).show();
        }else{
            Log.i("abc","checkTime False");
            Toast.makeText(this,"checkTime False",Toast.LENGTH_SHORT).show();
        }
    }

    void checkText(){
        if(text.equals("thundershowers")||text.equals("snow showers")||text.equals("isolated thundershowers")||
                text.equals("freezing drizzle")||text.equals("drizzle")||text.equals("freezing rain")||
                text.equals("showers")||text.equals("Thunderstorms")){
//            //做提醒
//            Notification notification = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("即將降雨")
//                    .setContentText("天氣:"+text)
//                    .build();
//            ntfMgr.notify(NTF_ID,notification);
            Log.i("abc","checkText True");
            Toast.makeText(this,"checkText False",Toast.LENGTH_SHORT).show();
        }else{
            Log.i("abc","checkText False");
            Toast.makeText(this,"checkText False",Toast.LENGTH_SHORT).show();
        }
    }
}
