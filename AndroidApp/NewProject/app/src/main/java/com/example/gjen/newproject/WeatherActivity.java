package com.example.gjen.newproject;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {

    TextView tvShow;
    StringBuilder stringBuilder;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        findviews();
        downloadWeather();
    }
    void findviews(){
        tvShow = (TextView)findViewById(R.id.textView20);
    }
    void downloadWeather(){
        stringBuilder=new StringBuilder();
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
                    String text = jsonObj6.getString("text");
                    String code = jsonObj6.getString("code");
                    String temp = jsonObj6.getString("temp");

                    //int tempC = ((Integer.parseInt(temp)-32)/9)*5;
                    double tempC = (Double.parseDouble(temp)-32)/9*5;

                    stringBuilder.append("今天日期 : "+date + "\n天氣 : " + text + "\n溫度(攝氏) : "+(int)tempC);
                    handler.post(showWeather);
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
    private Runnable showWeather = new Runnable() {
        @Override
        public void run() {
            tvShow.setText(stringBuilder);
        }
    };
}
