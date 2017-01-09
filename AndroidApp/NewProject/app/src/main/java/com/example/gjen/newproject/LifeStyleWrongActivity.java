package com.example.gjen.newproject;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LifeStyleWrongActivity extends AppCompatActivity {

    TextView tvShowTime;
    EditText etEvent,etLocation,etLat,etLong;
    RadioButton rbScheduleYes,rbScheduleNo;
    ImageButton imgLifeWrongLoc;
    private int selhour, selminute;

    String postMinute,postHour,postEvent,postLocation,postLong,postLat,postWeekly,postSchedule,postUser_id;
    private String URL = "http://140.134.26.9/Project1/api/P2_RecordApi/PostP2_Record";

    static final int MIN_TIME = 5000;// 位置更新條件：5000 毫秒
    static final float MIN_DIST = 0; // 位置更新條件：5 公尺
    LocationManager mgr;        // 定位總管
    LocationListener myLocListener;
    Double currentLat=0.0,currentLong=0.0;
    Location currentLocation;

    //被選擇的經緯度
    Double chooseLat,chooseLong;

    String time;
    Date curDate = new Date(System.currentTimeMillis());
    int month = curDate.getMonth()+1;
    int date = curDate.getDate();
    int hour = curDate.getHours();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_style_wrong);
        findviews();
        getBundleBack();
        setTime();
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        setMyLocListener();
    }
    void findviews(){
        tvShowTime = (TextView)findViewById(R.id.textView22) ;
        etLocation = (EditText)findViewById(R.id.editText18);
        etEvent = (EditText)findViewById(R.id.editText19);
        rbScheduleYes = (RadioButton)findViewById(R.id.radioButton5);
        rbScheduleNo = (RadioButton)findViewById(R.id.radioButton6);
        etLat = (EditText)findViewById(R.id.editText20);
        etLong = (EditText)findViewById(R.id.editText21);
        imgLifeWrongLoc = (ImageButton)findViewById(R.id.imageButton3);
        imgLifeWrongLoc.setEnabled(false);
    }

    private void setTime(){
        Calendar c = Calendar.getInstance();
        int setHour = c.get(Calendar.HOUR_OF_DAY);
        int setMinute = c.get(Calendar.MINUTE);
        String time = String.format("%2d:%2d", setHour, setMinute);
        tvShowTime.setText(time);
        postHour = String.valueOf(setHour);
        postMinute = String.valueOf(setMinute);
    }

    public void onSelectTime(View v){
        time();
        timePickerDialog();
    }

    private void timePickerDialog() {
        new TimePickerDialog(LifeStyleWrongActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int mounte) {
                String time = String.format("%2d:%2d", hour, mounte);
                tvShowTime.setText(time);
                postHour = String.valueOf(hour);
                postMinute = String.valueOf(mounte);
            }
        }, selhour, selminute, true).show();
    }

    private void time() {
        Calendar c = Calendar.getInstance();
        //時
        selhour = c.get(Calendar.HOUR_OF_DAY);
        //分
        selminute = c.get(Calendar.MINUTE);
    }

    public void onLifeStyleWrongOK(View v){

        time = "2016-"+month+"-"+date+"T"+hour+":00:00";
        postLocation = etLocation.getText().toString();
        postEvent = etEvent.getText().toString();

        if(chooseLat!=null && chooseLat !=null){
            postLat = String.valueOf(chooseLat);
            postLong = String.valueOf(chooseLong);
        }

        Bundle bundle = this.getIntent().getExtras();
        postUser_id = bundle.getString("user_id");
        postWeekly = bundle.getString("weekly");
        Log.i("abc",postWeekly + "\t" + postUser_id);
        // 例行公事
        if(rbScheduleYes.isChecked()){
            postSchedule = "true";
        }else{
            postSchedule = "false";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;

                HttpClient client = new DefaultHttpClient();
                try {
                    HttpPost post = new HttpPost(URL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("hour",postHour));
                    params.add(new BasicNameValuePair("minute",postMinute));
                    params.add(new BasicNameValuePair("time",time));
                    params.add(new BasicNameValuePair("event",postEvent));
                    params.add(new BasicNameValuePair("location",postLocation));
                    params.add(new BasicNameValuePair("Longitude",postLong));
                    params.add(new BasicNameValuePair("Latitude",postLat));
                    params.add(new BasicNameValuePair("weekly",postWeekly));
                    params.add(new BasicNameValuePair("schedule",postSchedule));
                    params.add(new BasicNameValuePair("user_id",postUser_id));
                    Log.i("abc",postHour+"\t"+time+"\t"+postLocation+"\t"+postEvent+"\t"+postLong+"\t"+postLat+"\t"+postWeekly+"\t"+postSchedule+"\t"+postUser_id);
                    UrlEncodedFormEntity ent = null;

                    Log.i("abc",params.toString());

                    ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse responsePOST = client.execute(post);
                    HttpEntity resEntity = responsePOST.getEntity();

                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity);
                        Log.i("abc",result);
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

        LifeStyleWrongActivity.this.finish();
    }

    public void onSearchLatlng(View v){
        Intent it = new Intent(LifeStyleWrongActivity.this,MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble("Lat", currentLat);
        bundle.putDouble("Long",currentLong);
        bundle.putString("user_id",postUser_id);
        bundle.putString("weekly",postWeekly);
        Log.i("abc",currentLat.toString());
        it.putExtras(bundle);
        startActivity(it);
        LifeStyleWrongActivity.this.finish();
    }

    public void onLifeStyleWrongCancel(View v){
        LifeStyleWrongActivity.this.finish();
    }

    void setMyLocListener() {
        myLocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("getLoc","getLoc");
                imgLifeWrongLoc.setEnabled(true);
                currentLocation = location;
                currentLat = currentLocation.getLatitude();
                currentLong = currentLocation.getLongitude();
                Log.i("abc",currentLat.toString()+"   Loc");
                if(currentLocation!=null) {
                   // tvShow.setText( "Lat : " + String.valueOf(currentLat) + "\nLong : " + String.valueOf(currentLong));
                }
                else {
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mgr.removeUpdates(myLocListener);    // 停止監聽位置事件
    }

    void getBundleBack(){
        Bundle bundle = this.getIntent().getExtras();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits( 6 );
        chooseLat = bundle.getDouble("lastLat");
        chooseLat = Double.parseDouble(nf.format(chooseLat));
        chooseLong = bundle.getDouble("lastLong");
        chooseLong = Double.parseDouble(nf.format(chooseLong));
        postUser_id = bundle.getString("user_id");
        postWeekly = bundle.getString("weekly");
        Log.i("abc",chooseLat.toString()+"123");
        if(chooseLat!=0.0 && chooseLong !=0.0){
            etLat.setText(String.valueOf(chooseLat));
            etLong.setText(String.valueOf(chooseLong));
        }else{
            etLat.setText(String.valueOf(currentLat));
            etLong.setText(String.valueOf(currentLong));
        }
    }
}
