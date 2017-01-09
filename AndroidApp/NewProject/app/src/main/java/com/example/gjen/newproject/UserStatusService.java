package com.example.gjen.newproject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GJen on 2016/11/19.
 */

public class UserStatusService extends Service {

    Date curDate = new Date(System.currentTimeMillis());
    int hour = curDate.getHours();
    int minute = curDate.getMinutes();

    String user_id;

    public String[] SQLG_id = new String[100];
    public String[] SQLU_id = new String[100];
    public String[] SQLUsername = new String[100];
    public String[] SQLEmail = new String[100];

    int JSONArrayLength=0;

    String url = "http://140.134.26.9/Project1/api/P2_GroupApi/PutP1_Group/";

    private Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle bundle = intent.getExtras();
        user_id = bundle.getString("User_id");
        getStatus();
        handler.post(statusOnTime);
    }

    void getStatus(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    // 建立連線
                    URL url = new URL("http://140.134.26.9/Project1/api/P2_GroupApi/GetP1_Group");
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

                    JSONArray array = new JSONArray(jsonString1);
                    JSONArrayLength = array.length();
                    for(int i=0;i<JSONArrayLength;i++){
                        SQLG_id[i] = array.getJSONObject(i).getString("G_id");
                        SQLU_id[i] = array.getJSONObject(i).getString("U_id");
                        SQLUsername[i] = array.getJSONObject(i).getString("Username");
                        SQLEmail[i] = array.getJSONObject(i).getString("Email");
                    }
                }
                catch (Exception e) {
                }
                finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    private Runnable statusOnTime = new Runnable() {
        @Override
        public void run() {
            checkTime();
//            handler.postDelayed(this,1000);
            handler.postDelayed(this,600000);   //   1000 * 60 *10
        }
    };

    void checkTime(){
        Log.i("abc","checkTime");
        if(minute >= 55 || minute <=5){
            Log.i("abc","time correct");
            for(int i=0;i<JSONArrayLength;i++){
                if(user_id.equals(SQLU_id[i])){
                    httpPut(SQLG_id[i],SQLU_id[i],SQLUsername[i],SQLEmail[i],"準時到");
                }
            }
        }else{
            Log.i("abc","time wrong");
        }
    }

    public void httpPut(final String aG_id,final String aU_id, final String aUsername, final String aEmail, final String astatus){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPut put= new HttpPut(url+aU_id);
                    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                    pairs.add(new BasicNameValuePair("G_id", aG_id));
                    pairs.add(new BasicNameValuePair("Gname", "高矮胖瘦專題"));
                    pairs.add(new BasicNameValuePair("U_id", aU_id));
                    pairs.add(new BasicNameValuePair("Username", aUsername));
                    pairs.add(new BasicNameValuePair("Email", aEmail));
                    pairs.add(new BasicNameValuePair("Status", astatus));

                    UrlEncodedFormEntity ent = null;

                    ent = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
                    put.setEntity(ent);
                    HttpResponse response = client.execute(put);
                    HttpEntity resEntity = response.getEntity();


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
