package com.example.gjen.newproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class SettingActivity extends AppCompatActivity {

    TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tvShow = (TextView)findViewById(R.id.textView31);
        tvShow.setText("聯絡我們\n" +
                "\n" +
                "D0240056@gmail.com\n" +
                "\n" +
                "407台中市西屯區文華路100號\n" +
                "\n" +
                "04-24517250");
    }
    public void onSettingLogout(View v){
        File file = new File("/data/data/com.example.gjen.newproject/shared_prefs","LoginInfo.xml");
        file.delete();
        Intent it = new Intent(SettingActivity.this,MemberEnterActivity.class);
        startActivity(it);
        SettingActivity.this.finish();
    }
}
