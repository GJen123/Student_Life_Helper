package com.example.gjen.newproject;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.util.Date;

public class LifeStyleActivity extends AppCompatActivity {

    TextView tvShow,tvShowPre;
    Handler handler = new Handler();

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

    //抓預判資料
    public String[] Prehour = new String[100];
    public String[] Prelocation = new String[100];
    public String[] Preevent = new String[100];
    public String[] PreLong = new String[100];
    public String[] PreLat = new String[100];
    public String[] Preweekly = new String[100];
    public String[] Preschedule = new String[100];
    public String[] PreUser_id = new String[100];
    String preURL = "http://140.134.26.9/Project1/api/PredictApi/GetMax/";
    StringBuilder stringBuilder2 = new StringBuilder();
    int JSONArraylength2;

    // 抓位置
    static final int MIN_TIME = 5000;// 位置更新條件：5000 毫秒
    static final float MIN_DIST = 0; // 位置更新條件：5 公尺
    LocationManager mgr;        // 定位總管
    LocationListener myLocListener;
    Location currentLocation;
    Double currentLat = 0.0, currentLong = 0.0;

    // 抓時間
    Date curDate = new Date(System.currentTimeMillis());
    //int hour = curDate.getHours();
    int hour = 13;

    // 系統通知管理器
    NotificationManager ntfMgr;
    final int NTF_ID = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_style);
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        Log.i("abc","onCreate");
        setMyLocListener();
        //onConnect();
        ntfMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        findviews();
        getData();
        getPredictData();
    }

    void onConnect(){
        Log.i("abc","connect");
        String provider = LocationManager.NETWORK_PROVIDER;
        String provider1 = LocationManager.GPS_PROVIDER;
        String provider2 = LocationManager.PASSIVE_PROVIDER;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("abc","if connect");
            return ;
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
//                    PackageManager.PERMISSION_GRANTED &&
//                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
//                            PackageManager.PERMISSION_GRANTED){
//            }
//        }
        Log.i("abc","onResume");
//        mgr.requestLocationUpdates(provider, MIN_TIME, MIN_DIST, myLocListener);
//        mgr.requestLocationUpdates(provider1, MIN_TIME, MIN_DIST, myLocListener);
//        mgr.requestLocationUpdates(provider2, MIN_TIME, MIN_DIST, myLocListener);
        mgr.requestLocationUpdates
                (LocationManager.NETWORK_PROVIDER, 1000, 5, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            //Log.d(this.getClass().getName(), location.toString());
                            currentLocation = location;
                            currentLat = currentLocation.getLatitude();
                            currentLong = currentLocation.getLongitude();
                            Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
                            handler.post(showData);
                        }
                    }
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                    @Override
                    public void onProviderEnabled(String provider) {
                    }
                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
    }

    void findviews() {
        tvShow = (TextView) findViewById(R.id.textView21);
        tvShowPre = (TextView) findViewById(R.id.textView33);
    }

    void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                String URL1 = URL+curDate.getDay();
                HttpClient httpClient = new DefaultHttpClient();
                try {
                    HttpGet get = new HttpGet(URL1);
                    HttpResponse response = httpClient.execute(get);
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {

                        result = EntityUtils.toString(resEntity);
                        JSONArraylength = new JSONArray(result).length();

                        for (int i = 0; i < JSONArraylength; i++) {
                            //SQLR_ID[i] = new JSONArray(result).getJSONObject(i).getString("r_id");
                            SQLhour[i] = new JSONArray(result).getJSONObject(i).getString("hour");
                            SQLlocation[i] = new JSONArray(result).getJSONObject(i).getString("location");
                            SQLevent[i] = new JSONArray(result).getJSONObject(i).getString("event");
                            SQLLong[i] = new JSONArray(result).getJSONObject(i).getString("longitude");
                            SQLLat[i] = new JSONArray(result).getJSONObject(i).getString("latitude");
                            SQLweekly[i] = new JSONArray(result).getJSONObject(i).getString("weekly");
                            SQLschedule[i] = new JSONArray(result).getJSONObject(i).getString("schedule");
                            SQLUser_id[i] = new JSONArray(result).getJSONObject(i).getString("user_id");
                            stringBuilder.append("\n時間 : " + SQLhour[i] + "\t\t\t地點 : " + SQLlocation[i] + "\t\t\t事情 : " + SQLevent[i] + "Lat : " + SQLLat[i] + "Long : " + SQLLong[i] + "\n\n\n");
                        }
                        handler.post(showData);
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

    void getPredictData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                String URL1 = preURL+curDate.getDay();
                HttpClient httpClient = new DefaultHttpClient();
                try {
                    HttpGet get = new HttpGet(URL1);
                    HttpResponse response = httpClient.execute(get);
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {

                        result = EntityUtils.toString(resEntity);
                        JSONArraylength2 = new JSONArray(result).length();

                        for (int i = 0; i < JSONArraylength2; i++) {
                            //SQLR_ID[i] = new JSONArray(result).getJSONObject(i).getString("r_id");
                            Prehour[i] = new JSONArray(result).getJSONObject(i).getString("hour");
                            Prelocation[i] = new JSONArray(result).getJSONObject(i).getString("location");
                            Preevent[i] = new JSONArray(result).getJSONObject(i).getString("event");
                            PreLong[i] = new JSONArray(result).getJSONObject(i).getString("longitude");
                            PreLat[i] = new JSONArray(result).getJSONObject(i).getString("latitude");
                            Preweekly[i] = new JSONArray(result).getJSONObject(i).getString("weekly");
                            Preschedule[i] = new JSONArray(result).getJSONObject(i).getString("schedule");
                            PreUser_id[i] = new JSONArray(result).getJSONObject(i).getString("user_id");
                            stringBuilder2.append("\nPre時間 : " + Prehour[i] + "\t\t\t地點 : " + Prelocation[i] + "\t\t\t事情 : " + Preevent[i] + "Lat : " + PreLat[i] + "Long : " + PreLong[i] + "\n\n\n");
                        }
                        handler.post(showPreData);
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

    private  Runnable showPreData = new Runnable() {
        @Override
        public void run() {
            tvShowPre.setText(stringBuilder2.toString());
        }
    };


    private Runnable showData = new Runnable() {
        @Override
        public void run() {
            //tvShow.setText(stringBuilder.toString() + "hour : " + hour);
            tvShow.setText(stringBuilder.toString() + "hour : " + hour + "\nLat : " + String.valueOf(currentLat) + "\nLong : " + String.valueOf(currentLong));
        }
    };

    void setMyLocListener() {
        myLocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("abc", "getLoc");
                currentLocation = location;
                currentLat = currentLocation.getLatitude();
                currentLong = currentLocation.getLongitude();
                if (currentLocation != null) {
                    handler.post(showData);
                    //tvShow.setText(stringBuilder.toString() + "hour : " + hour + "\nLat : " + String.valueOf(currentLat) + "\nLong : " + String.valueOf(currentLong));
                } else {
                    handler.post(showData);
                    //tvShow.setText(stringBuilder.toString() + "hour : " + hour);
                }

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

    @Override
    protected void onResume() {
        super.onResume();
        String provider = LocationManager.NETWORK_PROVIDER;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("abc","onResume if");
            return;
        }
        Log.i("abc","onResume");
        mgr.requestLocationUpdates(provider, MIN_TIME, MIN_DIST, myLocListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("abc","onPause");
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mgr.removeUpdates(myLocListener);    // 停止監聽位置事件
    }

    @Override
    protected void onDestroy() {
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
        Log.i("abc","onDestroy");
        //mgr.removeUpdates(myLocListener);
    }

    public void onWrongevent(View v) {
        Intent it = new Intent(LifeStyleActivity.this,LifeStyleWrongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id",SQLUser_id[0]);
        bundle.putString("weekly",SQLweekly[0]);
        Log.i("abc","Life + "+SQLUser_id[0] + "\t" + SQLweekly[0]);
        it.putExtras(bundle);
        startActivity(it);
        LifeStyleActivity.this.finish();
    }
}
