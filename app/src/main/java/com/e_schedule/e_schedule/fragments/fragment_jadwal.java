package com.e_schedule.e_schedule.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e_schedule.e_schedule.DatabaseHelper;
import com.e_schedule.e_schedule.JadwalAdapter;
import com.e_schedule.e_schedule.JadwalObject;
import com.e_schedule.e_schedule.MainActivity;
import com.e_schedule.e_schedule.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class fragment_jadwal extends Fragment {
    protected Cursor cursor;
    DatabaseHelper dbHelper;
    private RecyclerView list_item;
    private android.support.v7.widget.LinearLayoutManager LinearLayoutManager;
    private List<com.e_schedule.e_schedule.JadwalObject> JadwalObject;
    private JadwalAdapter jap;
    String jam = "";
    String menit = "";
    String jam_ayeuna = "";
    String this_device;
    String nama_hari = "";
    public static fragment_jadwal newInstance() {
        return new fragment_jadwal();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment_jadwal, container, false);
        list_item = (RecyclerView) rootView.findViewById(R.id.rcc);
        LinearLayoutManager = new LinearLayoutManager(rootView.getContext());
        list_item.setLayoutManager(LinearLayoutManager);
        ((MainActivity) getActivity()).dimana= "jadwal";
        get_jadwal();
        list_item.addOnItemTouchListener(
                new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d("click item", String.valueOf(position));
                        TextView jadwal_id = (TextView) view.findViewById(R.id.jadwal_id);
                        CardView cardView = (CardView) view.findViewById(R.id.carditem_planet);
                        ImageView imgv = (ImageView)  view.findViewById(R.id.masa);
                        Fragment fragment =  new fragment_detail_jadwal();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.flContent, fragment);
                        ft.addToBackStack("tag");
                        Bundle args = new Bundle();
                        Log.i(TAG,imgv.getTag().toString());
                        args.putString("jadwal_id", jadwal_id.getText().toString());
                        args.putString("status", imgv.getTag().toString());
                        fragment.setArguments(args);
                        ft.addToBackStack(fragment.getClass().getName());
                        ft.commit();
                    }
                }
                ));

        return rootView;
    }


    public void get_jadwal() {
        local_database();
        String light = "";
        JadwalObject = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM jadwal", null);
        if (cursor.moveToFirst()) {
            do {
                String tanggal_selesai= cursor.getString(cursor.getColumnIndex("tgl_selesai"));
                int date_selesai  = Integer.parseInt(tanggal_selesai.replace("-",""));
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String datesss = df.format(c.getTime());
                int date_now = Integer.parseInt(datesss.replace("-",""));
                if (date_now >= date_selesai) {
                String untuk =  cursor.getString(cursor.getColumnIndex("untuk"));
                String jid = cursor.getString(cursor.getColumnIndex("jadwal_id"));
                String jam_mulai = cursor.getString(cursor.getColumnIndex("jam_mulai"));
                String jam_selesai = cursor.getString(cursor.getColumnIndex("jam_selesai"));
                String tanggal_mulai= cursor.getString(cursor.getColumnIndex("tgl_mulai"));
                String tempat = cursor.getString(cursor.getColumnIndex("tempat"));
                String alamat = cursor.getString(cursor.getColumnIndex("alamat"));
                Calendar rightNow = Calendar.getInstance();
                int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                int minute = rightNow.get(Calendar.MINUTE);
                if (hour < 10) {
                    jam = "0"+ String.valueOf(hour);
                }else {
                    jam = String.valueOf(hour);
                }
                if (minute < 10) {
                    menit = "0" + String.valueOf(minute);
                }else {
                    menit = String.valueOf(minute);
                }
                jam_ayeuna = jam + menit;
//                int waktos = Integer.parseInt(jam_mulai.replace(":",""));
//                if ((waktos / 100) <= 599) {
//                    light = "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";
//                } else if ((waktos / 100) >= 600 && ((waktos / 100) < 1800) ) {
//                    light = "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";
//                } else {
//                    light = "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";
//                }
                String sudah = "";
                Date current = new Date();
                SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd");
                String hari_ini = frmt.format(current).replace("-","");
                int hri = Integer.parseInt(hari_ini);
                int hrj = Integer.parseInt(tanggal_mulai.replace("-",""));
                int hrjs = Integer.parseInt(tanggal_selesai.replace("-",""));
                int jj = Integer.parseInt(jam_mulai.replace(":",""));
                int jja = Integer.parseInt(jam_selesai.replace(":",""));
                int ja = Integer.parseInt(jam_ayeuna);
                String[] array_tm = tanggal_mulai.split("-");
                String bulan  = "" ;
                if ( array_tm[1].equals("01")) {
                    bulan = "January";
                } else if ( array_tm[1].equals("02")) {
                    bulan = "February";
                } else if ( array_tm[1].equals("03")) {
                    bulan = "Maret";
                } else if ( array_tm[1].equals("04")) {
                    bulan = "April";
                } else if ( array_tm[1].equals("05")) {
                    bulan = "Mei";
                } else if ( array_tm[1].equals("06")) {
                    bulan = "Juni";
                } else if ( array_tm[1].equals("07")) {
                    bulan = "Juli";
                }else if ( array_tm[1].equals("08")) {
                    bulan = "Agustus";
                }else if ( array_tm[1].equals("09")) {
                    bulan = "September";
                }else if ( array_tm[1].equals("10")) {
                    bulan = "Oktober";
                }else if ( array_tm[1].equals("11")) {
                    bulan = "November";
                }else if ( array_tm[1].equals("12")) {
                    bulan = "Desember";
                }
                get_day_name(Integer.parseInt(array_tm[2]),Integer.parseInt(array_tm[1]) ,Integer.parseInt(array_tm[0]));
                String re_tanggal_mulai = nama_hari + ", "  + array_tm[2] + " " + bulan + " " + array_tm[0] ;
                String[] array_ts = tanggal_selesai.split("-");
                if ( array_ts[1].equals("01")) {
                    bulan = "January";
                } else if ( array_ts[1].equals("02")) {
                    bulan = "February";
                } else if ( array_ts[1].equals("03")) {
                    bulan = "Maret";
                } else if ( array_ts[1].equals("04")) {
                    bulan = "April";
                } else if ( array_ts[1].equals("05")) {
                    bulan = "Mei";
                } else if ( array_ts[1].equals("06")) {
                    bulan = "Juni";
                } else if ( array_ts[1].equals("07")) {
                    bulan = "Juli";
                }else if ( array_ts[1].equals("08")) {
                    bulan = "Agustus";
                }else if ( array_ts[1].equals("09")) {
                    bulan = "September";
                }else if ( array_ts[1].equals("10")) {
                    bulan = "Oktober";
                }else if ( array_ts[1].equals("11")) {
                    bulan = "November";
                }else if ( array_ts[1].equals("12")) {
                    bulan = "Desember";
                }
                get_day_name(Integer.parseInt(array_ts[2]),Integer.parseInt(array_ts[1]) ,Integer.parseInt(array_ts[0]));
                String re_tanggal_selesai = nama_hari + ", " + array_ts[2] + " " + bulan + " " + array_ts[0];
                 if (hri >= hrj && hri <=hrjs) {
                    sudah ="sedang";
                    if (hri == hrj) {
                        if ( (jja / 100) > (ja ) && (jj / 100) < (ja ) ) {
                            sudah = "sedang";
                        } else if ( (jj / 100) < (ja ) ) {
                            sudah = "sedang";
                        } else  {
                            sudah = "";
                        }
                    }
                }
                if ( hrjs == hri) {
                    if ( (jja / 100) < (ja )  ) {
                        sudah = "beres";
                    }
                }
                JadwalObject.add(new JadwalObject(re_tanggal_mulai + ", " + jam_mulai.substring(0,5) + " - " + re_tanggal_selesai + ", " + jam_selesai.substring(0,5), tempat + ", " + alamat ,sudah,jid));
                 jap = new JadwalAdapter(getActivity(),JadwalObject);
                list_item.setAdapter(jap);
                }
            } while (cursor.moveToNext());
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