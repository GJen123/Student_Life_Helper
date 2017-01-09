package com.example.gjen.newproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;

public class UserStatusChangeActivity extends AppCompatActivity {

    String url = "http://140.134.26.9/Project1/api/P2_GroupApi/PutP1_Group/";
    String user_id,Username,Email;
    Handler handler = new Handler();

    String UserStatus="";

    final String FILE_NAME="settings";

    // 抓時間
    Date curDate = new Date(System.currentTimeMillis());
    int hour = curDate.getHours();
    int minute = curDate.getMinutes();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_status_change);
        Bundle bundle = this.getIntent().getExtras();
        user_id = bundle.getString("user_id");

//        handler.postDelayed(runShowStatus,1000); //1秒
    }

    public void onUserStatusArrive(View v){
        UserStatus = "準時到";
        Bundle bundle = this.getIntent().getExtras();
        user_id = bundle.getString("user_id");
        Log.i("abcUser",user_id);
        if(Integer.parseInt(user_id) == 30){
            httpPut("1","30","louis","abc@123.com",UserStatus);
        }else if(Integer.parseInt(user_id) == 66){
            httpPut("2","66","hao","hao@gmail.com",UserStatus);
        }else{
            httpPut("4","67","chichen","chichen@gmail.com",UserStatus);
        }
        UserStatusChangeActivity.this.finish();
    }
    public void onUserStatusUnArrive(View v){
        UserStatus = "無法參加";
        Bundle bundle = this.getIntent().getExtras();
        user_id = bundle.getString("user_id");
        Log.i("abcUser",user_id);
        if(Integer.parseInt(user_id) == 30){
            httpPut("1","30","louis","abc@123.com",UserStatus);
        }else if(Integer.parseInt(user_id) == 66){
            httpPut("2","66","hao","hao@gmail.com",UserStatus);
        }else{
            httpPut("4","67","chichen","chichen@gmail.com",UserStatus);
        }
        UserStatusChangeActivity.this.finish();
    }
    public void onUserStatusLate(View v){
        UserStatus = "延後到";
        Bundle bundle = this.getIntent().getExtras();
        user_id = bundle.getString("user_id");
        Log.i("abcUser",user_id);
        if(Integer.parseInt(user_id) == 30){
            httpPut("1","30","louis","abc@123.com",UserStatus);
        }else if(Integer.parseInt(user_id) == 66){
            httpPut("2","66","hao","hao@gmail.com",UserStatus);
        }else{
            httpPut("4","67","chichen","chichen@gmail.com",UserStatus);
        }
        UserStatusChangeActivity.this.finish();
    }


    public void httpPut(final String aG_id,final String aU_id, final String aUsername, final String aEmail, final String astatus){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("abc","1111abbbbbbbb");
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

    private Runnable runShowStatus = new Runnable() {
        @Override
        public void run() {
            if(minute >= 01 && minute <= 54){
                if(Integer.parseInt(user_id) == 30){
                    httpPut("1","30","louis","abc@123.com","準時到");
                }else if(Integer.parseInt(user_id) == 66){
                    httpPut("2","66","hao","hao@gmail.com","準時到");
                }else{
                    httpPut("4","67","chichen","chichen@gmail.com","準時到");
                }
                UserStatus="準時到";
            }
            handler.postDelayed(this,60000);
        }
    };

    public void onStatusFriend(View v){
        Uri uri =Uri.parse("http://140.134.26.9/Project1/manage/Teacher_Yang");
        Intent it = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(it);
    }
}
