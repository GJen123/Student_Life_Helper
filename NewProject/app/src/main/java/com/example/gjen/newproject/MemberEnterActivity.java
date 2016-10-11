package com.example.gjen.newproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MemberEnterActivity extends AppCompatActivity {

    EditText etAcc,etPw;

    String Acc,Pw;

    String user_id = null;

    private String uriAPI = "http://140.134.26.9/Project1/api/AccountApi/Login";

    public static File file;

    public SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_enter);
        findviews();
        file = new File("/data/data/com.example.gjen.newproject/shared_prefs","LoginInfo.xml");

        if(file.exists()){
            ReadValue();
            if(!user_id.equals("")){
                SendIntent();
            }
        }else{
        }
    }
    void findviews(){
        etAcc = (EditText)findViewById(R.id.editText);
        etPw = (EditText)findViewById(R.id.editText2);
    }
    public void onMemOk(View v){
        Acc = etAcc.getText().toString();
        Pw = etPw.getText().toString();
        if(Acc.isEmpty()||Pw.isEmpty()){
            Toast.makeText(this,"null",Toast.LENGTH_SHORT).show();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpClient client = new DefaultHttpClient();
                    try {
                        HttpPost post = new HttpPost(uriAPI);
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("email",Acc));
                        params.add(new BasicNameValuePair("password",Pw));

                        UrlEncodedFormEntity ent = null;
                        ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);

                        HttpResponse responsePOST = client.execute(post);
                        HttpEntity resEntity = responsePOST.getEntity();

                        if(resEntity != null){
                            user_id = EntityUtils.toString(resEntity);
                            if(user_id.equals("\"email is false\"")){
                                //帳號錯誤
                                Log.i("abc",user_id+"1");
                            }else if(user_id.equals("\"password is false\"")){
                                //密碼錯誤
                                Log.i("abc",user_id+"2");
                            }else{
                                //登入成功
                                Log.i("abc",user_id+"3");
                                if (Acc != null || Pw != null || user_id != null){
                                    setting = getSharedPreferences("LoginInfo",0);
                                    setting.edit().putString("user_id",user_id).commit();
                                    SendIntent();
                                }
                            }
                        }else{

                        }
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
    public void onMemCancel(View v){
        MemberEnterActivity.this.finish();
    }
    public void onMemAddMem(View v){
        Intent it = new Intent(MemberEnterActivity.this,MemberAddActivity.class);
        startActivity(it);
    }
    void ReadValue(){
        setting = getSharedPreferences("LoginInfo",0);
        user_id = setting.getString("user_id","");
    }
    void SendIntent(){
        Intent it = new Intent();
        it.setClass(MemberEnterActivity.this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id",user_id);
        it.putExtras(bundle);
        startActivity(it);
        MemberEnterActivity.this.finish();
    }
}
