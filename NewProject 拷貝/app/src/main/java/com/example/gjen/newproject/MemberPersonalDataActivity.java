package com.example.gjen.newproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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

public class MemberPersonalDataActivity extends AppCompatActivity {

    EditText etFname,etLname,etTel,etAddress,etSsn,etBdate;
    RadioButton rbSexBoy,rbSexGirl;
    String Fname,Lname,Sex,Tel,Address,Ssn,Bdate;
    String user_id;
    Bundle bundle;
    String URL = "http://140.134.26.9/Project1/api/P2_User_DetailApi/PostP2_User_Detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_personal_data);
        findviews();
    }
    void findviews(){
        etFname = (EditText)findViewById(R.id.editText5);
        etLname = (EditText)findViewById(R.id.editText6);
        rbSexBoy = (RadioButton)findViewById(R.id.radioButton);
        rbSexGirl = (RadioButton)findViewById(R.id.radioButton2);
        etTel = (EditText)findViewById(R.id.editText7);
        etAddress = (EditText)findViewById(R.id.editText8);
        etSsn = (EditText)findViewById(R.id.editText9);
        etBdate = (EditText)findViewById(R.id.editText10);
    }
    public void onMemPerDataOk(View v){
        Fname = etFname.getText().toString();
        Lname = etLname.getText().toString();
        if(rbSexBoy.isChecked()){
            Sex = "f";
        }else{
            Sex = "m";
        }
        Tel = etTel.getText().toString();
        Address = etAddress.getText().toString();
        Ssn = etSsn.getText().toString();
        Bdate = etBdate.getText().toString();

        bundle = this.getIntent().getExtras();
        user_id = bundle.getString("user_id");

        new Thread(new Runnable() {
            String result = null;
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                try {
                    HttpPost post = new HttpPost(URL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("id",user_id));
                    params.add(new BasicNameValuePair("Fname",Fname));
                    params.add(new BasicNameValuePair("Lname",Lname));
                    params.add(new BasicNameValuePair("sex",Sex));
                    params.add(new BasicNameValuePair("tel",Tel));
                    params.add(new BasicNameValuePair("address",Address));
                    params.add(new BasicNameValuePair("Ssn",Ssn));
                    params.add(new BasicNameValuePair("bdate",Bdate));

                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);

                    HttpResponse responsePOST = client.execute(post);
                    HttpEntity resEntity = responsePOST.getEntity();

                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    client.getConnectionManager().shutdown();
                }
            }
        }).start();

        Intent it = new Intent(MemberPersonalDataActivity.this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id",user_id);
        it.putExtras(bundle);
        startActivity(it);
    }
}
