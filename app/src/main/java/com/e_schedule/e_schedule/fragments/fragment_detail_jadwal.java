package com.e_schedule.e_schedule.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.e_schedule.e_schedule.DatabaseHelper;
import com.e_schedule.e_schedule.MainActivity;
import com.e_schedule.e_schedule.R;

import java.util.Calendar;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class fragment_detail_jadwal extends Fragment {
    protected Cursor cursor;
    DatabaseHelper dbHelper;
    String bulan, nama_hari;
  TextView oleh, untuk, acara, tempat, alamat,tgl_mulai, jam_mulai, tgl_selesai, jam_selesai, sumber, keterangan, status_jadwal  ;
    public static fragment_detail_jadwal newInstance() {
        return new fragment_detail_jadwal();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_jadwal, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Detail Jadwal");
        ((MainActivity)getActivity()).resetActionBar(true,
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).dimana= "detail jadwal";
        Log.i(TAG,((MainActivity) getActivity()).dimana);
        oleh = (TextView) rootView.findViewById(R.id.oleh);
        untuk= (TextView) rootView.findViewById(R.id.untuk);
        acara = (TextView) rootView.findViewById(R.id.acara);
        tempat = (TextView) rootView.findViewById(R.id.tempat);
        alamat = (TextView) rootView.findViewById(R.id.alamat);
        tgl_mulai = (TextView) rootView.findViewById(R.id.tgl_mulai);
        jam_mulai= (TextView) rootView.findViewById(R.id.jam_mulai);
        tgl_selesai = (TextView) rootView.findViewById(R.id.tgl_selesai);
        jam_selesai= (TextView) rootView.findViewById(R.id.jam_selesai);
        sumber = (TextView) rootView.findViewById(R.id.sumber);
        keterangan = (TextView) rootView.findViewById(R.id.keterangan);
        status_jadwal = (TextView) rootView.findViewById(R.id.status_jadwal) ;

        String where_id = getArguments().getString("jadwal_id");
        load_jadwal(where_id);
        if (getArguments().getString("status").equals("sedang")){
            status_jadwal.setText("Acara Sedang Berlangsung");
        } else  if (getArguments().getString("status").equals("beres")){
            status_jadwal.setText("Acara Sudah Selesai");
        } else {
            status_jadwal.setText("Acara Belum Mulai");
        }
        return rootView;
    }

    public void load_jadwal(final String id) {
        local_database();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM jadwal where jadwal_id = '" + id + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
        } else {
         acara.setText(cursor.getString(5).toString());
         tempat.setText(cursor.getString(6).toString());
         alamat.setText(cursor.getString(7).toString());
         jam_mulai.setText(cursor.getString(2).toString());
         jam_selesai.setText(cursor.getString(4).toString());
         oleh.setText(cursor.getString(18).toString());
         untuk.setText(cursor.getString(19).toString());
         sumber.setText( cursor.getString(9).toString());
         keterangan.setText( cursor.getString(8).toString());

         String[] array_mulai = cursor.getString(1).toString().split("-");
            if ( array_mulai[1].equals("01")) {
                bulan = "January";
            } else if ( array_mulai[1].equals("02")) {
                bulan = "February";
            } else if ( array_mulai[1].equals("03")) {
                bulan = "Maret";
            } else if ( array_mulai[1].equals("04")) {
                bulan = "April";
            } else if ( array_mulai[1].equals("05")) {
                bulan = "Mei";
            } else if ( array_mulai[1].equals("06")) {
                bulan = "Juni";
            } else if ( array_mulai[1].equals("07")) {
                bulan = "Juli";
            }else if ( array_mulai[1].equals("08")) {
                bulan = "Agustus";
            }else if ( array_mulai[1].equals("09")) {
                bulan = "September";
            }else if ( array_mulai[1].equals("10")) {
                bulan = "Oktober";
            }else if ( array_mulai[1].equals("11")) {
                bulan = "November";
            }else if ( array_mulai[1].equals("12")) {
                bulan = "Desember";
            }
          get_day_name(Integer.parseInt(array_mulai[2]),Integer.parseInt(array_mulai[1]) ,Integer.parseInt(array_mulai[0]));
         tgl_mulai.setText(nama_hari + ", " + array_mulai[2] + " " + bulan + " " + array_mulai[0]);

         String[] array_selesai = cursor.getString(3).toString().split("-");
            if ( array_selesai[1].equals("01")) {
                bulan = "January";
            } else if ( array_selesai[1].equals("02")) {
                bulan = "February";
            } else if ( array_selesai[1].equals("03")) {
                bulan = "Maret";
            } else if ( array_selesai[1].equals("04")) {
                bulan = "April";
            } else if ( array_selesai[1].equals("05")) {
                bulan = "Mei";
            } else if ( array_selesai[1].equals("06")) {
                bulan = "Juni";
            } else if ( array_selesai[1].equals("07")) {
                bulan = "Juli";
            }else if ( array_selesai[1].equals("08")) {
                bulan = "Agustus";
            }else if ( array_selesai[1].equals("09")) {
                bulan = "September";
            }else if ( array_selesai[1].equals("10")) {
                bulan = "Oktober";
            }else if ( array_selesai[1].equals("11")) {
                bulan = "November";
            }else if ( array_selesai[1].equals("12")) {
                bulan = "Desember";
            }
            get_day_name(Integer.parseInt(array_selesai[2]),Integer.parseInt(array_selesai[1]) ,Integer.parseInt(array_selesai[0]));
         tgl_selesai.setText(nama_hari + ", " + array_selesai[2] + " " + bulan + " " + array_selesai[0]);
        }
    }


    public void local_database() {

        dbHelper = new DatabaseHelper(getActivity());
        try {
            dbHelper.createDataBase();
        } catch (Exception ioe) {
            Toast.makeText(getContext(), "Gagal", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }
    private void get_day_name(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR,year);
        int dt_day = cal.get(Calendar.DAY_OF_WEEK);
        if (dt_day == 1) {
            nama_hari = "Minggu";
        } else  if (dt_day == 2) {
            nama_hari = "Senin";
        } else  if (dt_day == 3) {
            nama_hari = "Selasa";
        }else  if (dt_day == 4) {
            nama_hari = "Rabu";
        }else  if (dt_day == 5) {
            nama_hari = "Kamis";
        }else  if (dt_day == 6) {
            nama_hari = "Jumat";
        }else  if (dt_day == 7) {
            nama_hari = "Sabtu";
        }
    }
}