package com.e_schedule.e_schedule;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.params.BasicHttpParams;

import static android.content.ContentValues.TAG;

public class Notifikasi_Service extends Service {
    protected Cursor cursor;
    DatabaseHelper dbHelper;
    Context context;
    String this_device;
    String information_id;
    String jadwal_id;
    int aya = 0;
    int aya_jadwal = 0;
    int notif_1_jadwal = 0;
    int notif_2_jadwal = 0;
    String pengirim_informasi, to_info, pengirim_jadwal, to_jadwal;
    String kepada, information_parent_id, dari, st_ralat, st_penting, materi, fl_tanggal_mulai, fl_tanggal_akhir;
    String tgl_mulai, jam_mulai, tgl_selesai, jam_selesai, acara, tempat, alamat, keterangan, sumber, sumber_no, sumber_tgl, sumber_diterima, alert1, stalert_1, alert2, stalert_2, tgl_entry;
    Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


        context = getApplicationContext();
        super.onCreate();
        timer.schedule(new DelayedTask(), 300, 3000);// 300000 is 5min

    }

    private class DelayedTask extends TimerTask {

        @Override
        public void run() {
            local_database();
            get_device_id();

            Log.i(TAG, this_device);
            getData();
            getJadwal();
            pengingat_jadwal();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Log.i(TAG, "STOPED");
    }

    public void local_database() {
        dbHelper = new DatabaseHelper(context);
        try {
            dbHelper.createDataBase();
        } catch (Exception ioe) {
            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_LONG).show();
        }
    }

    public void get_device_id() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM login", null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
        } else {
            this_device = cursor.getString(0).toString();
        }
    }

    public void insert_notifikasi() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into informasi values ('" + information_id + "','" + information_parent_id + "','" + pengirim_informasi + "','" + st_ralat + "','" + st_penting + "','" + materi + "','" + fl_tanggal_mulai + "','" + fl_tanggal_akhir + "','tidak','" + to_info + "' )");
    }

    public void insert_jadwal() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into jadwal values ('" + jadwal_id + "','" + tgl_mulai + "','" + jam_mulai + "','" + tgl_selesai + "','" + jam_selesai + "','" + acara + "','" + tempat + "','" + alamat + "','" + keterangan + "','" + sumber + "','" + sumber_no + "','" + sumber_tgl + "','" + sumber_diterima + "','" + alert1 + "','" + stalert_1 + "','" + alert2 + "','" + stalert_2 + "','" + tgl_entry + "','" + pengirim_jadwal + "','" + to_jadwal + "')");
    }

    public void get_inf() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM informasi where informasi_id = '" + information_id + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            aya = 1;
        } else {

        }
    }

    public void get_jadwal_id() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM jadwal where jadwal_id = '" + jadwal_id + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            aya_jadwal = 1;
            String nt1 = cursor.getString(14).toString();
            String nt2 = cursor.getString(16).toString();
            if (nt1.equals("1")) {
                notif_1_jadwal = 1;
            } else {
                notif_1_jadwal = 0;
            }
            if (nt2.equals("1")) {
                notif_2_jadwal = 1;
            } else {
                notif_2_jadwal = 0;
            }

        } else {
            aya_jadwal = 0;
        }
    }

    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                cz.msebera.android.httpclient.impl.client.DefaultHttpClient httpclient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient(new BasicHttpParams());
                cz.msebera.android.httpclient.client.methods.HttpPost httppost = new cz.msebera.android.httpclient.client.methods.HttpPost("http://vulnwalker.com/android/local/informasi.php");
                httppost.setHeader("Content-type", "application/json");
                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject jsonRootObject = new JSONObject(result);
                    JSONArray jsonArray = jsonRootObject.optJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        kepada = jsonObject.getString("kepada").toString();


                        String[] array_kepada = kepada.split(";");
                        for (int a = 0; a < array_kepada.length; a++) {

                            if (array_kepada[a].equals(this_device)) {
                                dari = jsonObject.getString("dari").toString();
                                information_id = jsonObject.getString("informasi_id").toString();
                                information_parent_id = jsonObject.getString("informasi_parent_id").toString();
                                st_ralat = jsonObject.getString("st_ralat").toString();
                                st_penting = jsonObject.getString("st_penting").toString();
                                materi = jsonObject.getString("materi").toString();
                                to_info = jsonObject.getString("to_info").toString();
                                pengirim_informasi = jsonObject.getString("from").toString();
                                fl_tanggal_mulai = jsonObject.getString("fl_tgl_mulai").toString();
                                fl_tanggal_akhir = jsonObject.getString("fl_tgl_akhir").toString();
                                get_inf();
                                if (aya == 1) {

                                } else {
                                    Notify("INFORMASI HARI INI", materi);
                                    insert_notifikasi();
                                    update_clicked(information_parent_id);
                                 }
                                aya = 0;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    private void Notify(String notificationTitle, String notificationMessage) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationMessage);
        builder.setTicker("INFORMASI HARI INI");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setNumber(1);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("arah", "informasi");
        notificationIntent.putExtra("id", information_id);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.sound);
        builder.setSound(soundUri);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    private void Notify_Jadwal(String notificationTitle, String jam, String acara) {
        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        builder.setTicker("JADWAL");

        android.support.v4.app.NotificationCompat.InboxStyle inboxStyle = new android.support.v4.app.NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = new String(jam);
        events[1] = new String(acara);
        inboxStyle.setBigContentTitle(notificationTitle);

        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }
        builder.setStyle(inboxStyle);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("arah", "jadwal");
        notificationIntent.putExtra("id", jadwal_id);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.sound);
        builder.setSound(soundUri);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void getJadwal() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                cz.msebera.android.httpclient.impl.client.DefaultHttpClient httpclient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient(new BasicHttpParams());
                cz.msebera.android.httpclient.client.methods.HttpPost httppost = new cz.msebera.android.httpclient.client.methods.HttpPost("http://vulnwalker.com/android/local/list_jadwal.php");
                httppost.setHeader("Content-type", "application/json");
                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject jsonRootObject = new JSONObject(result);
                    JSONArray jsonArray = jsonRootObject.optJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String untuk = jsonObject.getString("untuk").toString();
                        String[] array_untuk = untuk.split(";");
                        for (int a = 0; a < array_untuk.length; a++) {
                                if (array_untuk[a].equals(this_device)) {
                                jadwal_id = jsonObject.getString("jadwal_id").toString();
                                String dari = jsonObject.getString("user_id").toString();
                                tgl_mulai = jsonObject.getString("tgl_mulai").toString();
                                jam_mulai = jsonObject.getString("jam_mulai").toString();
                                tgl_selesai = jsonObject.getString("tgl_selesai").toString();
                                jam_selesai = jsonObject.getString("jam_selesai").toString();
                                acara = jsonObject.getString("acara").toString();
                                tempat = jsonObject.getString("tempat").toString();
                                alamat = jsonObject.getString("alamat").toString();
                                keterangan = jsonObject.getString("keterangan").toString();
                                sumber = jsonObject.getString("sumber").toString();
                                sumber_no = jsonObject.getString("sumber_no").toString();
                                sumber_tgl = jsonObject.getString("sumber_tgl").toString();
                                sumber_diterima = jsonObject.getString("sumber_diterima").toString();
                                alert1 = jsonObject.getString("alert1").toString();
                                stalert_1 = jsonObject.getString("stalert_1").toString();
                                alert2 = jsonObject.getString("alert2").toString();
                                stalert_2 = jsonObject.getString("stalert_2").toString();
                                tgl_entry = jsonObject.getString("alert1").toString();
                                pengirim_jadwal = jsonObject.getString("pengirim_jadwal").toString();
                                to_jadwal = jsonObject.getString("to_jadwal").toString();

                                    get_jadwal_id();
                                    if (aya_jadwal == 1) {

                                } else {
                                    insert_jadwal();
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    public void update_clicked(String kolot ) {
        if (kolot.equals("0")) {

        } else {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("update informasi set clicked = 'ya' where informasi_id = '" + kolot + "'");
        }


    }

 public void pengingat_jadwal () {
     SQLiteDatabase db = dbHelper.getReadableDatabase();
     cursor = db.rawQuery("SELECT * FROM jadwal", null);
     if (cursor.moveToFirst()) {
         do {
             Calendar rightNow = Calendar.getInstance();
             int hour = rightNow.get(Calendar.HOUR_OF_DAY);
             int minute = rightNow.get(Calendar.MINUTE);
             String n_j , n_m;
             if (hour < 10) {
                 n_j = "0"+ String.valueOf(hour);
             }else {
                 n_j = String.valueOf(hour);
             }
             if (minute < 10) {
                 n_m = "0" + String.valueOf(minute);
             }else {
                 n_m = String.valueOf(minute);
             }
             String cr = n_j + n_m;
             String jam_start = cursor.getString(cursor.getColumnIndex("jam_mulai"));
             String jam_finish = cursor.getString(cursor.getColumnIndex("jam_selesai"));
             String abok = cursor.getString(cursor.getColumnIndex("acara"));
             String notif_kahiji_jadwal = cursor.getString(cursor.getColumnIndex("stalert_1"));
             String notif_kadua_jadwal = cursor.getString(cursor.getColumnIndex("stalert_2"));
             String lama_alert_1 = cursor.getString(cursor.getColumnIndex("alert1"));
             String lama_alert_2 = cursor.getString(cursor.getColumnIndex("alert2"));
             String xx = jam_start.replace(":", "");
             int now = Integer.parseInt(cr);
             int jm = Integer.parseInt(xx);
             int beda = (jm / 100) - (now );
             if (notif_kahiji_jadwal.equals("1")) {
                 if (lama_alert_1.equals("1")) {
                     if (beda == 100) {
                         Notify_Jadwal("ACARA AKAN DI MULAI DALAM 1 JAM", jam_start + " - " + jam_finish, abok);
                     }
                 }
                 if (lama_alert_1.equals("2")) {
                     if (beda == 200) {
                         Notify_Jadwal("ACARA AKAN DI MULAI DALAM 2 JAM", jam_start + " - " + jam_finish, abok);
                        }
                 }
             }
             if (notif_kadua_jadwal.equals("1")) {
                 if (lama_alert_2.equals("1")) {
                     if (beda == 50) {
                         Notify_Jadwal("ACARA AKAN DI MULAI DALAM 10 MENIT", jam_start + " - " + jam_finish, abok);
                     }
                 } else if (lama_alert_2.equals("2")) {
                     if (beda == 60) {
                         Notify_Jadwal("ACARA AKAN DI MULAI DALAM 20 MENIT", jam_start + " - " + jam_finish, abok);

                     }
                 } else if (lama_alert_2.equals("3")) {
                     if (beda == 70) {
                         Notify_Jadwal("ACARA AKAN DI MULAI DALAM 30 MENIT", jam_start + " - " + jam_finish, abok);

                     }
                 } else if (lama_alert_2.equals("4")) {
                     if (beda == 80) {
                         Notify_Jadwal("ACARA AKAN DI MULAI DALAM 40 MENIT", jam_start + " - " + jam_finish, abok);
                       }
                 } else if (lama_alert_2.equals("5")) {
                     if (beda == 90) {
                         Notify_Jadwal("ACARA AKAN DI MULAI DALAM 50 MENIT", jam_start + " - " + jam_finish, abok);

                     }
                 }
             }

         } while (cursor.moveToNext());
     }



 }



    }