package com.e_schedule.e_schedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LOGIN extends AppCompatActivity implements AsyncResponse, View.OnClickListener {
    protected Cursor cursor;
    DatabaseHelper dbHelper;
    String nama, tempat_lahir, tanggal_lahir, jabatan, user_level, grup_level, has_admin, user_status, user_mobile, foto, last_edit;
    String token;
    ImageButton login;
    EditText username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        local_database();
        check_login();
        setContentView(R.layout.activity_login);
        deklarasi_item();
    }


    public void local_database() {
        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDataBase();
        } catch (Exception ioe) {
            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_LONG).show();
        }
    }

    public void get_token() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        token = sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
    }
    public void check_login() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM login", null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
        } else {
            String nama =  cursor.getString(2).toString();
            String foto = cursor.getString(11).toString();
            startService(new Intent(LOGIN.this, Notifikasi_Service.class));
            Intent already_login = new Intent(LOGIN.this, MainActivity.class);
            already_login.putExtra("nama",nama);
            already_login.putExtra("foto",foto);
            startActivity(already_login);
            finish();
        }
    }

    public void deklarasi_item() {
        login = (ImageButton) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        HashMap postData = new HashMap();
        postData.put("username", username.getText().toString().replace("'","''").replace(" ",""));
        postData.put("password", password.getText().toString().replace("'","''").replace(" ",""));
        PostResponseAsyncTask curl = new PostResponseAsyncTask(this, postData);
        curl.execute("http://vulnwalker.com/android/local/login.php");
    }
    @Override
    public void processFinish(String pregmatch) {
        if (!pregmatch.equals("") && !pregmatch.equals("failed")){
            try {
                JSONObject jsonRootObject = new JSONObject(pregmatch);
                JSONArray jsonArray = jsonRootObject.optJSONArray("result");
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                nama = jsonObject.getString("user_name").toString();
                foto = jsonObject.getString("foto").toString();
                tempat_lahir = jsonObject.getString("tempat_lahir").toString();
                tanggal_lahir = jsonObject.getString("tanggal_lahir").toString();
                jabatan = jsonObject.getString("jabatan").toString();
                user_level = jsonObject.getString("level").toString();
                grup_level = jsonObject.getString("grup_level").toString();
                has_admin = jsonObject.getString("has_admin").toString();
                user_status = jsonObject.getString("user_status").toString();
                user_mobile = jsonObject.getString("user_mobile").toString();
                last_edit = jsonObject.getString("last_edit").toString();
                add_device();
                startService(new Intent(LOGIN.this, Notifikasi_Service.class));
                Intent already_login = new Intent(LOGIN.this, MainActivity.class);
                already_login.putExtra("nama",nama);
                already_login.putExtra("foto",foto);
                startActivity(already_login);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }  else {
            Toast.makeText(this,"Login Failed",Toast.LENGTH_LONG).show();
            username.setText("");
            password.setText("");
            username.setFocusable(true);
        }
    }

    public void add_device() {
        get_token();
        HashMap postData = new HashMap();
        postData.put("username", username.getText().toString().replace("'","''").replace(" ",""));
        postData.put("token", token);
        PostResponseAsyncTask curl = new PostResponseAsyncTask(this, postData);
        curl.execute("http://vulnwalker.com/android/local/devices.php");
        insert_session();
    }

    public void insert_session() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into login values('" + username.getText().toString().replace("'","''").replace(" ","") + "' , '" + password.getText().toString().replace("'","''").replace(" ","") + "','" + nama + "','" + tempat_lahir + "','" + tanggal_lahir + "','" + jabatan + "','" + user_level + "','" + grup_level + "','" + has_admin + "','" + user_status + "','" + user_mobile + "','" + foto + "','" + last_edit + "')");
    }
    @Override
    public void onBackPressed() {

    }

}
