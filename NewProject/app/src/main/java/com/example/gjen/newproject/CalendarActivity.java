package com.example.gjen.newproject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    EditText etHour,etLocation,etEvent,etLat,etLong;
    RadioButton rbScheduleYes,rbScheduleNo;
    TextView tvShow;

    String postHour,postEvent,postLocation,postLong,postLat,postSchedule,postUser_id;
    private String URL = "http://140.134.26.9/Project1/api/P2_RecordApi/PostP2_Record";

    Date curDate = new Date(System.currentTimeMillis());
    int postWeekly=0;

    static final int MIN_TIME = 5000;// 位置更新條件：5000 毫秒
    static final float MIN_DIST = 0; // 位置更新條件：5 公尺
    LocationManager mgr;        // 定位總管
    LocationListener myLocListener;
    Double currentLat=0.0,currentLong=0.0;
    Location currentLocation;

    Double chooseLat,chooseLong;

    //SQLite
    SQLiteDatabase db;
    Cursor cursor;
    Button btnCalPrevious,btnCalNext;
    ImageButton imbtnCalLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        findviews();
        setupDatabase();
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        setMyLocListener();
        getBundleBack();
    }
    void findviews(){
        etHour = (EditText)findViewById(R.id.editText14);
        etLocation = (EditText)findViewById(R.id.editText15);
        etEvent = (EditText)findViewById(R.id.editText16);
        etLat = (EditText)findViewById(R.id.editText22);
        etLong = (EditText)findViewById(R.id.editText23);
        tvShow = (TextView)findViewById(R.id.textView30);
        rbScheduleYes = (RadioButton)findViewById(R.id.radioButton3);
        rbScheduleNo = (RadioButton)findViewById(R.id.radioButton4);
        btnCalPrevious = (Button)findViewById(R.id.button23);
        btnCalPrevious.setEnabled(false);
        btnCalNext = (Button)findViewById(R.id.button24);
        btnCalNext.setEnabled(false);
        imbtnCalLoc = (ImageButton)findViewById(R.id.imageButton4);
        imbtnCalLoc.setEnabled(false);
    }

    void setupDatabase(){
        DBHelper dbHelper=new DBHelper(this,SQLiteUtil.DATABASE_NAME,null,SQLiteUtil.VERSION);
        db=dbHelper.getWritableDatabase();
//        db=openOrCreateDatabase(SQLiteUtil.DATABASE_NAME,MODE_PRIVATE,null);
//        db.execSQL(SQLiteUtil.SQL_CREATE_TABLE_CALENDAR);
    }

    public void onCalNew(View v){
        postToMsSQL();
        newToSQLite();
    }

    void newToSQLite(){
        ContentValues values = new ContentValues();
        values.put("Hour",etHour.getText().toString());
        values.put("Lat",etLat.getText().toString());
        values.put("Location",etLocation.getText().toString());
        values.put("Long",etLong.getText().toString());
        values.put("Event",etEvent.getText().toString());
        if(rbScheduleYes.isChecked()){
            values.put("Schedule","true");
        }else{
            values.put("Schedule","false");
        }
        Log.i("abc",values.toString());
        db.insert(SQLiteUtil.CALENDAR_TABLE_NAME, null, values);
        Toast.makeText(this,"資料新增成功",Toast.LENGTH_SHORT).show();
        clear();
        onCalQuery(etEvent);
//        cursor=db.query(SQLiteUtil.CALENDAR_TABLE_NAME,null,null,null,null,null,null);
//        Log.i("abc", String.valueOf(cursor.getCount()));
    }

    public void onCalDel(View v){
        db.delete(SQLiteUtil.CALENDAR_TABLE_NAME,"Lat=?",new String[]{etLat.getText().toString()});
        db.delete(SQLiteUtil.CALENDAR_TABLE_NAME,"Long=?",new String[]{etLong.getText().toString()});
        db.delete(SQLiteUtil.CALENDAR_TABLE_NAME,"Location=?",new String[]{etLocation.getText().toString()});
        db.delete(SQLiteUtil.CALENDAR_TABLE_NAME,"Event=?",new String[]{etEvent.getText().toString()});
        Toast.makeText(this,"資料刪除成功",Toast.LENGTH_SHORT).show();
        clear();
        onCalQuery(etEvent);
    }

    public void onCalUpdate(View v){
        ContentValues values = new ContentValues();
        values.put("Lat",etLat.getText().toString());
        values.put("Long",etLong.getText().toString());
        values.put("Hour",etHour.getText().toString());
        values.put("Location",etLocation.getText().toString());
        values.put("Event",etEvent.getText().toString());
        db.update(SQLiteUtil.CALENDAR_TABLE_NAME,values,"Hour=?",new String[]{etHour.getText().toString()});
        db.update(SQLiteUtil.CALENDAR_TABLE_NAME,values,"Lat=?",new String[]{etLat.getText().toString()});
        db.update(SQLiteUtil.CALENDAR_TABLE_NAME,values,"Long=?",new String[]{etLong.getText().toString()});
        db.update(SQLiteUtil.CALENDAR_TABLE_NAME,values,"Location=?",new String[]{etLocation.getText().toString()});
        db.update(SQLiteUtil.CALENDAR_TABLE_NAME,values,"Event=?",new String[]{etEvent.getText().toString()});
        Toast.makeText(this,"資料修改成功",Toast.LENGTH_SHORT).show();
        clear();
        onCalQuery(etEvent);
    }

    public void onCalQuery(View v){
        Log.i("abc","Query");
        cursor=db.query(SQLiteUtil.CALENDAR_TABLE_NAME,null,null,null,null,null,"Hour asc");
        btnCalNext.setEnabled(true);
        btnCalPrevious.setEnabled(true);
//        if(cursor.getCount()>0){
            Log.i("abc","if");
            String str="總共有 "+cursor.getCount()+"筆資料\n";
            Log.i("abc", String.valueOf(cursor.getCount()));
            str+="-----\n";
            cursor.moveToFirst();
            do{
                Log.i("abc","do while");
                str+="時間："+cursor.getString(0)+"\t";
                str+="經度："+cursor.getString(1)+"\t";
                str+="緯度："+cursor.getString(2)+"\t";
                str+="地點："+cursor.getString(3)+"\t";
                str+="事情："+cursor.getString(4)+"\n";
            }while(cursor.moveToNext());
            Log.i("abc","show");
            tvShow.setText(str);
//        }
    }

    public void onCalPrevious(View v){
        if(cursor==null){
            return;
        }
        if(cursor.isFirst()){
            Toast.makeText(this,"己經在第一筆資料了",Toast.LENGTH_SHORT).show();
            return;
        }
        if(cursor.moveToPrevious()){
            etHour.setText(cursor.getString(cursor.getColumnIndex("Hour")));
            etLat.setText(cursor.getString(cursor.getColumnIndex("Lat")));
            etLong.setText(cursor.getString(cursor.getColumnIndex("Long")));
            etLocation.setText(cursor.getString(cursor.getColumnIndex("Location")));
            etEvent.setText(cursor.getString(cursor.getColumnIndex("Event")));
        }
    }

    public void onCalNext(View v){
        if(cursor==null){
            return;
        }
        if(cursor.isLast()){
            Toast.makeText(this,"己經在第一筆資料了",Toast.LENGTH_SHORT).show();
            return;
        }
        if(cursor.moveToNext()){
            etHour.setText(cursor.getString(cursor.getColumnIndex("Hour")));
            etLat.setText(cursor.getString(cursor.getColumnIndex("Lat")));
            etLong.setText(cursor.getString(cursor.getColumnIndex("Long")));
            etLocation.setText(cursor.getString(cursor.getColumnIndex("Location")));
            etEvent.setText(cursor.getString(cursor.getColumnIndex("Event")));
        }
    }

    void postToMsSQL(){
        postHour = etHour.getText().toString();
        postLocation = etLocation.getText().toString();
        postEvent = etEvent.getText().toString();

        //---------------要去抓今天禮拜幾 和 user_id----------------------------------
        postWeekly = curDate.getDay();
        Bundle bundle = this.getIntent().getExtras();
        postUser_id = bundle.getString("user_id");
        //--------------------------------------------------------------------------------------

        postLat = String.valueOf(chooseLat);
        postLong = String.valueOf(chooseLong);

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
                    params.add(new BasicNameValuePair("location",postLocation));
                    params.add(new BasicNameValuePair("event",postEvent));
                    params.add(new BasicNameValuePair("Longitude",postLong));
                    params.add(new BasicNameValuePair("Latitude",postLat));
                    params.add(new BasicNameValuePair("weekly",String.valueOf(postWeekly)));
                    params.add(new BasicNameValuePair("schedule",postSchedule));
                    params.add(new BasicNameValuePair("user_id",postUser_id));
                    UrlEncodedFormEntity ent = null;

                    ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse responsePOST = client.execute(post);
                    HttpEntity resEntity = responsePOST.getEntity();

                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity);
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

    void setMyLocListener() {
        myLocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("getLoc","getLoc");
                imbtnCalLoc.setEnabled(true);
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
        mgr.removeUpdates(myLocListener);    // 停止監聽位置事件
    }

    public void onCalSearchLatLng(View v){
        Intent it = new Intent(CalendarActivity.this,MapsCalActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id",postUser_id);
        bundle.putDouble("Lat",currentLat);
        bundle.putDouble("Long",currentLong);
        Log.i("abc",currentLat + "\t" + currentLong);
        it.putExtras(bundle);
        startActivity(it);
        CalendarActivity.this.finish();
    }

    void getBundleBack(){
        Bundle bundle = this.getIntent().getExtras();
        postUser_id = bundle.getString("user_id");
        chooseLat = bundle.getDouble("lastLat");
        chooseLong = bundle.getDouble("lastLong");
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits( 6 );
        chooseLat = Double.parseDouble(nf.format(chooseLat));
        chooseLong = Double.parseDouble(nf.format(chooseLong));
        if(chooseLat!=0.0 && chooseLong !=0.0){
            etLat.setText(String.valueOf(chooseLat));
            etLong.setText(String.valueOf(chooseLong));
        }
    }

    void clear(){
        etLat.setText("");
        etLong.setText("");
        etHour.setText("");
        etLocation.setText("");
        etEvent.setText("");
        rbScheduleYes.setChecked(false);
        rbScheduleNo.setChecked(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
