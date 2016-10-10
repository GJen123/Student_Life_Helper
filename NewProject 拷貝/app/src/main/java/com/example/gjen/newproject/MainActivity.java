package com.example.gjen.newproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    Switch aSwitch,weatherSwitch;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviews();
        Bundle bundle;
        bundle = this.getIntent().getExtras();
        user_id = bundle.getString("user_id");
    }
    void findviews(){
        aSwitch = (Switch)findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(listener);
        weatherSwitch = (Switch)findViewById(R.id.switch2);
        weatherSwitch.setOnCheckedChangeListener(weatherListener);
    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Log.i("abc","listen");
            if(compoundButton.isChecked()){
                Log.i("abc","isChecked");
                Intent it = new Intent(MainActivity.this,LocationService.class);
                startService(it);
            }else{
                Intent it = new Intent(MainActivity.this,LocationService.class);
                stopService(it);
            }
        }
    };

    CompoundButton.OnCheckedChangeListener weatherListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(compoundButton.isChecked()){
                Log.i("abc","isChecked");
                Intent it = new Intent(MainActivity.this,WeatherService.class);
                startService(it);
            }else{
                Intent it = new Intent(MainActivity.this,WeatherService.class);
                stopService(it);
            }
        }
    };

    public void onMainWeather(View v){
        Intent it = new Intent(MainActivity.this,WeatherActivity.class);
        startActivity(it);
    }
    public void onMainLifeStyle(View v){
        Intent it = new Intent(MainActivity.this,LifeStyleActivity.class);
        startActivity(it);
    }
    public void onMainCal(View v){
        Intent it = new Intent(MainActivity.this,CalendarActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id",user_id);
        it.putExtras(bundle);
        startActivity(it);
    }
    public void onMainMoney(View v){
        Intent it = new Intent(MainActivity.this,MoneyActivity.class);
        startActivity(it);
    }
    public void onMainSetting(View v){
        Intent it = new Intent(MainActivity.this,SettingActivity.class);
        startActivity(it);
    }
}
