package com.example.gjen.newproject;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoneyActivity extends AppCompatActivity {

    String user_id;

    EditText etId,etEvent,etMoney,etTime;
    TextView tvShow;
    SQLiteDatabase db;
    Cursor cursor;
    Button btnMoneyPrevious,btnMoneyNext;
    RadioButton rbMoneyYes,rbMoneyNo;
    int maxId;

    Date curDate = new Date(System.currentTimeMillis());
    int month = curDate.getMonth()+1;

    String url = "http://140.134.26.9/Project1/api/P2_OverspendApi/PostP1_Overspend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        getBundle();
        findviews();
        setupDatabase();
        checkMonthly();
        onMoneyQuery(etId);
    }
    void getBundle(){
        Bundle bundle = this.getIntent().getExtras();
        user_id = bundle.getString("user_id");
    }
    void findviews(){
        etId = (EditText)findViewById(R.id.editText17);
        etEvent = (EditText)findViewById(R.id.editText11);
        etMoney = (EditText)findViewById(R.id.editText12);
        etTime = (EditText)findViewById(R.id.editText13);
        btnMoneyPrevious = (Button)findViewById(R.id.button17);
        btnMoneyPrevious.setEnabled(false);
        btnMoneyNext = (Button)findViewById(R.id.button18);
        btnMoneyNext.setEnabled(false);
        tvShow = (TextView)findViewById(R.id.textView14);
        rbMoneyYes = (RadioButton)findViewById(R.id.radioButton7);
        rbMoneyNo = (RadioButton)findViewById(R.id.radioButton8);
    }

    void setupDatabase(){
        DBHelper dbHelper =new DBHelper(this,SQLiteUtil.DATABASE_NAME,null,SQLiteUtil.VERSION);
        db= dbHelper.getWritableDatabase();
    }
    public void onMoneyNew(View v){
        ContentValues values = new ContentValues();
        values.put("Id",etId.getText().toString());
        values.put("Time",etTime.getText().toString());
        values.put("Event",etEvent.getText().toString());
        values.put("Money",etMoney.getText().toString());
        values.put("Month",String.valueOf(month));
        if(rbMoneyYes.isChecked()){
            values.put("Need","是");
        }else{
            values.put("Need","否");
        }
        db.insert(SQLiteUtil.MONEY_TABLE_NAME, null, values);
        clear();
        onMoneyQuery(etId);
    }
    public void onMoneyDel(View v){
        db.delete(SQLiteUtil.MONEY_TABLE_NAME,"Id=?",new String[]{etId.getText().toString()});
//        db.delete(SQLiteUtil.MONEY_TABLE_NAME,"Time=? and Event=? and Money=?",new String[]{etTime.getText().toString(),etEvent.getText().toString(),etMoney.getText().toString()});
        clear();
        onMoneyQuery(etId);
    }
    public void onMoneyUpdate(View v){
        ContentValues values = new ContentValues();
        values.put("Id",etId.getText().toString());
        values.put("Time",etTime.getText().toString());
        values.put("Event",etEvent.getText().toString());
        values.put("Money",etMoney.getText().toString());
        values.put("Month",String.valueOf(month));
        if(rbMoneyYes.isChecked()){
            values.put("Need","是");
        }else{
            values.put("Need","否");
        }
        db.update(SQLiteUtil.MONEY_TABLE_NAME,values,"Id=?",new String[]{etId.getText().toString()});
//        db.update(SQLiteUtil.MONEY_TABLE_NAME,values,"Time=?",new String[]{etTime.getText().toString()});
//        db.update(SQLiteUtil.MONEY_TABLE_NAME,values,"Event=?",new String[]{etEvent.getText().toString()});
//        db.update(SQLiteUtil.MONEY_TABLE_NAME,values,"Money=?",new String[]{etMoney.getText().toString()});
//        db.update(SQLiteUtil.MONEY_TABLE_NAME,values,"Time=? and Event=? and Money=?",new String[]{etTime.getText().toString(),etEvent.getText().toString(),etMoney.getText().toString()});
        clear();
        onMoneyQuery(etId);
    }
    public void onMoneyQuery(View v){
        maxId=0;
        int totalMon=0;
        int mustCost=0,notmustCost=0;
        cursor=db.query(SQLiteUtil.MONEY_TABLE_NAME,null,null,null,null,null,"Time asc");
        btnMoneyPrevious.setEnabled(true);
        btnMoneyNext.setEnabled(true);
        if(cursor.getCount()>0){
            String str1="總共有 "+cursor.getCount()+"筆資料\n";
            String str2=""; //必要
            String str3=""; //非必要
            str1+="------------------------\n\n";
            cursor.moveToFirst();
            do{
                if(cursor.getString(4).equals("是")){
                    str2+="Id : "+cursor.getString(0)+"\t";
                    str2+="Month : "+cursor.getString(5)+"\t";
                    str2+="時間："+cursor.getString(1)+"\t";
                    str2+="事件："+cursor.getString(2)+"\t";
                    str2+="金錢："+cursor.getString(3)+"\t";
                    str2+="必要："+cursor.getString(4)+"\n";
                    mustCost+=Integer.parseInt(cursor.getString(3));
                }else{
                    str3+="Id : "+cursor.getString(0)+"\t";
                    str3+="Month : "+cursor.getString(5)+"\t";
                    str3+="時間："+cursor.getString(1)+"\t";
                    str3+="事件："+cursor.getString(2)+"\t";
                    str3+="金錢："+cursor.getString(3)+"\t";
                    str3+="必要："+cursor.getString(4)+"\n";
                    notmustCost+=Integer.parseInt(cursor.getString(3));
                }

                totalMon+=Integer.parseInt(cursor.getString(3));

                if(Integer.parseInt(cursor.getString(0))>maxId) {    //  找出ID最大值
                    maxId = Integer.parseInt(cursor.getString(0));
                }
            }while(cursor.moveToNext());

            tvShow.setText(str1
                    + "-----必要花費-----\n"
                    + str2 + "\n必要花費共 : "+ mustCost + " 元"
                    + "\n------------------------"
                    + "\n\n-----非必要花費-----\n"
                    + str3 + "\n非必要花費共 : " + notmustCost+ " 元"
                    + "\n\n------------------------\n"
                    + "總共花費 : " + totalMon + " 元"
                    + "\n這個月餘額還有 : " + (10000-totalMon));
            etId.setText(String.valueOf(maxId+1));

        }else{
            etId.setText(String.valueOf(1));
            tvShow.setText("沒有資料");
        }
    }
    public void onMoneyPrevious(View v){
        if(cursor==null){
            return;
        }
        if(cursor.isFirst()){
            Toast.makeText(this,"己經在第一筆資料了",Toast.LENGTH_SHORT).show();
            return;
        }
        if(cursor.moveToPrevious()){
            etId.setText(cursor.getString(cursor.getColumnIndex("Id")));
            etTime.setText(cursor.getString(cursor.getColumnIndex("Time")));
            etEvent.setText(cursor.getString(cursor.getColumnIndex("Event")));
            etMoney.setText(cursor.getString(cursor.getColumnIndex("Money")));
            if(cursor.getString(cursor.getColumnIndex("Need")).equals("是")){
                rbMoneyYes.setChecked(true);
            }else{
                rbMoneyNo.setChecked(true);
            }
        }
    }
    public void onMoneyNext(View v){
        if(cursor==null){
            return;
        }
        if(cursor.isLast()){
            Toast.makeText(this,"己經在最後一筆資料了",Toast.LENGTH_SHORT).show();
            return;
        }
        if(cursor.moveToNext()){
            etId.setText(cursor.getString(cursor.getColumnIndex("Id")));
            etTime.setText(cursor.getString(cursor.getColumnIndex("Time")));
            etEvent.setText(cursor.getString(cursor.getColumnIndex("Event")));
            etMoney.setText(cursor.getString(cursor.getColumnIndex("Money")));
            if(cursor.getString(cursor.getColumnIndex("Need")).equals("是")){
                rbMoneyYes.setChecked(true);
            }else{
                rbMoneyNo.setChecked(true);
            }
        }
    }

    public void onMoneyClear(View v){
        clear();
        etId.setText(String.valueOf(maxId+1));
    }

    void clear(){
        etId.setText("");
        etTime.setText("");
        etEvent.setText("");
        etMoney.setText("");
        rbMoneyYes.setChecked(false);
        rbMoneyNo.setChecked(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    void checkMonthly(){
        int totalMon=0;
        String spend;
        cursor=db.query(SQLiteUtil.MONEY_TABLE_NAME,null,null,null,null,null,"Time asc");
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            String monthInDB = cursor.getString(5);
            do{
                totalMon+=Integer.parseInt(cursor.getString(3));
            }while(cursor.moveToNext());
            if((10000-totalMon)>0){
                spend = "false";
            }else{
                spend = "true";
            }
            if(!monthInDB.equals(String.valueOf(month))){
                postOverSpend(monthInDB,spend);
//                db.execSQL("DROP TABLE IF EXISTS "+SQLiteUtil.DATABASE_NAME); //刪除history table
                this.deleteDatabase(SQLiteUtil.DATABASE_NAME);
                setupDatabase();
            }else {

            }
        }else{
            etId.setText(String.valueOf(1));
            tvShow.setText("沒有資料");
        }

    }
    void postOverSpend(final String monthInDB, final String spend){
        Log.i("abc","postOverSpend");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                Log.i("abc","U_id : "+user_id);
                Log.i("abc","Month : "+monthInDB);
                Log.i("abc","Spend : "+String.valueOf(spend));
                HttpClient client = new DefaultHttpClient();
                try {
                    HttpPost post = new HttpPost(url);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("O_id","4"));
                    params.add(new BasicNameValuePair("User_id",user_id));
                    params.add(new BasicNameValuePair("Month",monthInDB));
                    params.add(new BasicNameValuePair("Status",spend));

                    UrlEncodedFormEntity ent = null;

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
    }
}
