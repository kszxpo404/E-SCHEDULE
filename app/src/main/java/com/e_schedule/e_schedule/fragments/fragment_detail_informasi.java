package com.e_schedule.e_schedule.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.e_schedule.e_schedule.DatabaseHelper;
import com.e_schedule.e_schedule.MainActivity;
import com.e_schedule.e_schedule.R;

import java.util.Calendar;

public class fragment_detail_informasi extends Fragment {
    protected Cursor cursor;
    DatabaseHelper dbHelper;
    String nama_hari, bulan;
    TextView informasi_id,informasi_parent_id,dari,st_ralat,st_penting,kepada,materi,fl_tgl_mulai,fl_tgl_selesai;
    public static fragment_detail_informasi newInstance() {
        return new fragment_detail_informasi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_informasi, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Detail Informasi");
        ((MainActivity)getActivity()).resetActionBar(true,
         DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).dimana= "detail informasi";
        informasi_id = (TextView) rootView.findViewById(R.id.informasi_id);
        informasi_parent_id = (TextView) rootView.findViewById(R.id.informasi_parent_id);
        dari = (TextView) rootView.findViewById(R.id.dari);
        st_ralat = (TextView) rootView.findViewById(R.id.st_ralat);
        st_penting = (TextView) rootView.findViewById(R.id.st_penting);
        kepada = (TextView) rootView.findViewById(R.id.kepada);
        materi = (TextView) rootView.findViewById(R.id.materi);
        fl_tgl_mulai = (TextView) rootView.findViewById(R.id.fl_tgl_mulai);
        fl_tgl_selesai = (TextView) rootView.findViewById(R.id.fl_tgl_selesai);
        String where_id = getArguments().getString("informasi_id");
        informasi_id.setText(where_id);
        get_informasi(where_id);
        return rootView;
    }

     public void get_informasi(final String id) {
        local_database();
         SQLiteDatabase db = dbHelper.getReadableDatabase();
         cursor = db.rawQuery("SELECT * FROM informasi where informasi_id = '" + id + "'", null);
         cursor.moveToFirst();
         if (cursor.getCount() == 0) {
         } else {
             kepada.setText(cursor.getString(9).toString());
             if (cursor.getString(1).toString().equals("0")) {
                 informasi_parent_id.setText("-");
             }else {
                 informasi_parent_id.setText(cursor.getString(1).toString());
             }
             materi.setText(cursor.getString(5).toString());
             if (cursor.getString(3).toString().equals("0")) {
                st_ralat.setText("TIDAK");
             }else {
                st_ralat.setText("YA");
             }
             if (cursor.getString(4).toString().equals("0")) {
                 st_penting.setText("TIDAK");
             }else {
                 st_penting.setText("YA");
             }
             String[] tgl_mulai = cursor.getString(6).toString().split("-");
             if ( tgl_mulai[1].equals("01")) {
                 bulan = "January";
             } else if ( tgl_mulai[1].equals("02")) {
                 bulan = "February";
             } else if ( tgl_mulai[1].equals("03")) {
                 bulan = "Maret";
             } else if ( tgl_mulai[1].equals("04")) {
                 bulan = "April";
             } else if ( tgl_mulai[1].equals("05")) {
                 bulan = "Mei";
             } else if ( tgl_mulai[1].equals("06")) {
                 bulan = "Juni";
             } else if ( tgl_mulai[1].equals("07")) {
                 bulan = "Juli";
             }else if ( tgl_mulai[1].equals("08")) {
                 bulan = "Agustus";
             }else if ( tgl_mulai[1].equals("09")) {
                 bulan = "September";
             }else if ( tgl_mulai[1].equals("10")) {
                 bulan = "Oktober";
             }else if ( tgl_mulai[1].equals("11")) {
                 bulan = "November";
             }else if ( tgl_mulai[1].equals("12")) {
                 bulan = "Desember";
             }
             get_day_name(Integer.parseInt(tgl_mulai[2]),Integer.parseInt(tgl_mulai[1]) ,Integer.parseInt(tgl_mulai[0]));
             fl_tgl_mulai.setText(nama_hari + ", " + tgl_mulai[2] + " " + bulan + " " + tgl_mulai[0]);
             String[] tgl_beres = cursor.getString(7).toString().split("-");
             if ( tgl_beres[1].equals("01")) {
                 bulan = "January";
             } else if ( tgl_beres[1].equals("02")) {
                 bulan = "February";
             } else if ( tgl_beres[1].equals("03")) {
                 bulan = "Maret";
             } else if ( tgl_beres[1].equals("04")) {
                 bulan = "April";
             } else if ( tgl_beres[1].equals("05")) {
                 bulan = "Mei";
             } else if ( tgl_beres[1].equals("06")) {
                 bulan = "Juni";
             } else if ( tgl_beres[1].equals("07")) {
                 bulan = "Juli";
             }else if ( tgl_beres[1].equals("08")) {
                 bulan = "Agustus";
             }else if ( tgl_beres[1].equals("09")) {
                 bulan = "September";
             }else if ( tgl_beres[1].equals("10")) {
                 bulan = "Oktober";
             }else if ( tgl_beres[1].equals("11")) {
                 bulan = "November";
             }else if ( tgl_beres[1].equals("12")) {
                 bulan = "Desember";
             }
             get_day_name(Integer.parseInt(tgl_beres[2]),Integer.parseInt(tgl_beres[1]) ,Integer.parseInt(tgl_beres[0]));
             fl_tgl_selesai.setText(nama_hari + ", " + tgl_beres[2] + " " + bulan + " " + tgl_beres[0]);
             dari.setText(cursor.getString(2).toString());

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