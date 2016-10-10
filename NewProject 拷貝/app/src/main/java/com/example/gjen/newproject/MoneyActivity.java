package com.example.gjen.newproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MoneyActivity extends AppCompatActivity {

    EditText etEvent,etMoney,etTime;
    TextView tvShow;
    SQLiteDatabase db;
    Cursor cursor;
    Button btnMoneyPrevious,btnMoneyNext;
    RadioButton rbMoneyYes,rbMoneyNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        findviews();
        setupDatabase();
    }
    void findviews(){
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
        values.put("Time",etTime.getText().toString());
        values.put("Event",etEvent.getText().toString());
        values.put("Money",etMoney.getText().toString());
        if(rbMoneyYes.isChecked()){
            values.put("Need","是");
        }else{
            values.put("Need","否");
        }
        db.insert(SQLiteUtil.MONEY_TABLE_NAME, null, values);
        Toast.makeText(this,"資料新增成功",Toast.LENGTH_SHORT).show();
        clear();
        onMoneyQuery(etMoney);

    }
    public void onMoneyDel(View v){
        db.delete(SQLiteUtil.MONEY_TABLE_NAME,"Time=? and Event=? and Money=?",new String[]{etTime.getText().toString(),etEvent.getText().toString(),etMoney.getText().toString()});
        Toast.makeText(this,"資料刪除成功",Toast.LENGTH_SHORT).show();
        clear();
        onMoneyQuery(etMoney);
    }
    public void onMoneyUpdate(View v){
        ContentValues values = new ContentValues();
        values.put("Time",etTime.getText().toString());
        values.put("Event",etEvent.getText().toString());
        values.put("Money",etMoney.getText().toString());
        if(rbMoneyYes.isChecked()){
            values.put("Need","是");
        }else{
            values.put("Need","否");
        }
//        db.update(SQLiteUtil.MONEY_TABLE_NAME,values,"Time=?",new String[]{etTime.getText().toString()});
//        db.update(SQLiteUtil.MONEY_TABLE_NAME,values,"Event=?",new String[]{etEvent.getText().toString()});
//        db.update(SQLiteUtil.MONEY_TABLE_NAME,values,"Money=?",new String[]{etMoney.getText().toString()});
        db.update(SQLiteUtil.MONEY_TABLE_NAME,values,"Time=? and Event=? and Money=?",new String[]{etTime.getText().toString(),etEvent.getText().toString(),etMoney.getText().toString()});
        Toast.makeText(this,"資料修改成功",Toast.LENGTH_SHORT).show();
        clear();
        onMoneyQuery(etMoney);
    }
    public void onMoneyQuery(View v){
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
                if(cursor.getString(3).equals("是")){
                    str2+="時間："+cursor.getString(0)+"\t";
                    str2+="事件："+cursor.getString(1)+"\t";
                    str2+="金錢："+cursor.getString(2)+"\t";
                    str2+="必要："+cursor.getString(3)+"\n";
                    mustCost+=Integer.parseInt(cursor.getString(2));
                }else{
                    str3+="時間："+cursor.getString(0)+"\t";
                    str3+="事件："+cursor.getString(1)+"\t";
                    str3+="金錢："+cursor.getString(2)+"\t";
                    str3+="必要："+cursor.getString(3)+"\n";
                    notmustCost+=Integer.parseInt(cursor.getString(2));
                }

                totalMon+=Integer.parseInt(cursor.getString(2));
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
            etTime.setText(cursor.getString(cursor.getColumnIndex("Time")));
            etEvent.setText(cursor.getString(cursor.getColumnIndex("Event")));
            etMoney.setText(cursor.getString(cursor.getColumnIndex("Money")));
            if(cursor.getString(cursor.getColumnIndex("Need")).equals("是")){
                rbMoneyYes.setChecked(true);
            }else{
                rbMoneyNo.setChecked(false);
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
            etTime.setText(cursor.getString(cursor.getColumnIndex("Time")));
            etEvent.setText(cursor.getString(cursor.getColumnIndex("Event")));
            etMoney.setText(cursor.getString(cursor.getColumnIndex("Money")));
            if(cursor.getString(cursor.getColumnIndex("Need")).equals("是")){
                rbMoneyYes.setChecked(true);
            }else{
                rbMoneyNo.setChecked(false);
            }
        }
    }
    void clear(){
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
}
