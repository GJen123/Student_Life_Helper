package com.example.gjen.newproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MemberAddActivity extends AppCompatActivity {

    EditText etAcc,etPw;

    String Acc,Pw;

    String user_id;

    private String uriAPI = "http://140.134.26.9/Project1/api/AccountApi/Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_add);
        findviews();
    }
    void findviews(){
        etAcc = (EditText)findViewById(R.id.editText3);
        etPw = (EditText)findViewById(R.id.editText4);
    }
    public void onMemAddOk(View v){
        Acc = etAcc.getText().toString();
        Pw = etPw.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(uriAPI);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("email",Acc));
                    params.add(new BasicNameValuePair("password",Pw));
                    UrlEncodedFormEntity ent = null;

                    ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);

                    HttpResponse responsePOST = client.execute(post);
                    HttpEntity resEntity = responsePOST.getEntity();

                    if(resEntity!=null){
                        user_id = EntityUtils.toString(resEntity);
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
        if(user_id != null){
            Intent it = new Intent(MemberAddActivity.this,MemberPersonalDataActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("user_id",user_id);
            it.putExtras(bundle);
            startActivity(it);
        }
    }
    public void onMemAddCancel(View v){
        MemberAddActivity.this.finish();
    }
}
