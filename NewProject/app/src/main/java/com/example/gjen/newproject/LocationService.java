package com.example.gjen.newproject;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by GJen on 2016/9/26.
 */

public class LocationService extends Service {

    //抓資料庫資料的變數
    //public String[] SQLR_ID = new String[100];
    public String[] SQLhour = new String[100];
    public String[] SQLlocation = new String[100];
    public String[] SQLevent = new String[100];
    public String[] SQLLong = new String[100];
    public String[] SQLLat = new String[100];
    public String[] SQLweekly = new String[100];
    public String[] SQLschedule = new String[100];
    public String[] SQLUser_id = new String[100];
    String URL = "http://140.134.26.9/Project1/api/P2_RecordApi/GetP2_RecordByWeekly/";
    StringBuilder stringBuilder = new StringBuilder();
    int JSONArraylength;

    static final int MIN_TIME = 1000;// 位置更新條件：5000 毫秒
    static final float MIN_DIST = 0; // 位置更新條件：5 公尺
    LocationManager mgr;        // 定位總管
    LocationListener myLocListener;
    Location currentLocation;
    Double currentLat = 0.0, currentLong = 0.0;

    // 抓時間
    Date curDate = new Date(System.currentTimeMillis());
    //int hour = curDate.getHours();
    int hour = 13;
    int minute = curDate.getMinutes();
    //int minute = 55;
    int day = curDate.getDay();


    // 系統通知管理器
    NotificationManager ntfMgr;
    final int NTF_ID = 1000;

    private Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        setListener();
        ntfMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        getData();
        Log.i("abc","onCreate");
        String provider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mgr.requestLocationUpdates(provider, MIN_TIME, MIN_DIST, myLocListener);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i("abc", "-----onStart-----");
        handler.postDelayed(showData, 1000);
        handler.post(showGetData);
        handler.postDelayed(makeMesNot, 10000); //10秒
        handler.postDelayed(makeMesNot, 60000); //60秒
//        handler.postDelayed(makeMesNot, 300000); //60秒*5 = 5分鐘
        Log.i("abc",stringBuilder.toString());
        super.onStart(intent, startId);

    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(showData);
        handler.removeCallbacks(makeMesNot);
        Log.i("abc", "-----onStop-----");
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mgr.removeUpdates(myLocListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Runnable showData = new Runnable() {
        @Override
        public void run() {
            Log.i("abc", "Lat : " + currentLat + "  Long : " + currentLong);
            Log.i("abc", "     time : " + new Date().toString());
            handler.postDelayed(this, 1000);   //10秒
        }
    };
    private Runnable showGetData = new Runnable() {
        @Override
        public void run() {
            Log.i("abc", stringBuilder.toString());
        }
    };
    private Runnable makeMesNot = new Runnable() {
        @Override
        public void run() {
            makeMessage();
            handler.postDelayed(this, 10000);   //10秒
//            handler.postDelayed(this, 60000);   //60秒
//            handler.postDelayed(this, 300000); //60秒*5
        }
    };
    void setListener(){
        myLocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits( 6 );
                currentLat = currentLocation.getLatitude();
                currentLong = currentLocation.getLongitude();
                currentLat = Double.parseDouble(nf.format(currentLat));
                currentLong = Double.parseDouble(nf.format(currentLong));
                Log.i("abc","====================");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }
    void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                Log.i("abc","getData");
                String url1 = URL+day;
                HttpClient httpClient = new DefaultHttpClient();
                try {
                    HttpGet get = new HttpGet(url1);
                    HttpResponse response = httpClient.execute(get);
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {

                        result = EntityUtils.toString(resEntity);
                        Log.i("abc",result);
                        JSONArraylength = new JSONArray(result).length();

                        for (int i = 0; i < JSONArraylength; i++) {
                            Log.i("abc","getDataFor");
                            //SQLR_ID[i] = new JSONArray(result).getJSONObject(i).getString("r_id");
                            SQLhour[i] = new JSONArray(result).getJSONObject(i).getString("hour");
                            SQLlocation[i] = new JSONArray(result).getJSONObject(i).getString("location");
                            SQLevent[i] = new JSONArray(result).getJSONObject(i).getString("event");
                            SQLLong[i] = new JSONArray(result).getJSONObject(i).getString("longitude");
                            SQLLat[i] = new JSONArray(result).getJSONObject(i).getString("latitude");
                            SQLweekly[i] = new JSONArray(result).getJSONObject(i).getString("weekly");
                            SQLschedule[i] = new JSONArray(result).getJSONObject(i).getString("schedule");
                            SQLUser_id[i] = new JSONArray(result).getJSONObject(i).getString("user_id");
                            stringBuilder.append("時間 : " + SQLhour[i] + "\t\t\t地點 : " + SQLlocation[i] + "\t\t\t事情 : " + SQLevent[i] + "Lat : " + SQLLat[i] + "Long : " + SQLLong[i] + "\n\n\n");
                        }
                        handler.post(showGetData);


                    }else{
                        Log.i("abc","result = null");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }).start();
    }

    void makeMessage(){
        double dis = 0.0;

        for(int i=0;i<JSONArraylength;i++){
            Log.i("abc","===========");
            Log.i("abc","hour : " + hour);
            Log.i("abc","SQLhour : " + SQLhour[i]);
            if(hour == (Integer.parseInt(SQLhour[i])-1) && minute == 55){
                Log.i("abc","hour is same");
                Log.i("abc","minutes : " +minute);
                if(currentLong == Double.parseDouble(SQLLong[i]) && currentLat == Double.parseDouble(SQLLat[i])){
                    // 不做事
                    Log.i("abc","makeMessage location same");
                }else{
                    // 做提醒
                    // 算距離

                    dis = calDistance(currentLat,currentLong,SQLLat[i],SQLLong[i]);
                    if(dis>300){
                        Notification notification = new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.gps)
                                .setContentTitle("生活小幫手 : 您目前的位置距離有錯")
                                .setContentText("距離目的地有點遠，建議跑步")
                                .build();
                        ntfMgr.notify(NTF_ID,notification);
                    } else {
                        Notification notification = new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.gps)
                                .setContentTitle("生活小幫手 : 您目前的位置有錯")
                                .setContentText("若要更改，請打開App")
                                .build();
                        ntfMgr.notify(NTF_ID,notification);
                    }
                    Log.i("abc","distance : "+String.valueOf(dis));
                    Log.i("abc","makeMessage location diff");
                    Log.i("abc","===========");
                }
            }else{
                Log.i("abc","hour is diff");
                Log.i("abc","minutes : " +minute);
                Log.i("abc","===========");
            }
        }
    }

    double calDistance(double curLat, double curLong, String sqlLat, String sqlLong){
        final double EARTH_RADIUS = 6378137.0;

        double ALat = (curLat * Math.PI / 180.0);
        double BLat = (Double.parseDouble(sqlLat) * Math.PI / 180.0);
        double a = ALat - BLat;
        double b = (curLong - Double.parseDouble(sqlLong)) * Math.PI / 180.0;
        double s = 2*Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(ALat) * Math.cos(BLat) * Math.pow(Math.sin(b / 2) , 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}

