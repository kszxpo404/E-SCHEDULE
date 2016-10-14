package com.e_schedule.e_schedule.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.e_schedule.e_schedule.DatabaseHelper;
import com.e_schedule.e_schedule.InformasiAdapter;
import com.e_schedule.e_schedule.InformasiObject;
import com.e_schedule.e_schedule.MainActivity;
import com.e_schedule.e_schedule.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class fragment_informasi extends Fragment {
    protected Cursor cursor;
    DatabaseHelper dbHelper;
    private RecyclerView list_item;
    String nama_hari, bulan;
    private android.support.v7.widget.LinearLayoutManager LinearLayoutManager;
    private List<com.e_schedule.e_schedule.InformasiObject> InformasiObject;
    private InformasiAdapter iap;

    public static fragment_informasi newInstance() {
        return new fragment_informasi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_informasi, container, false);
        list_item = (RecyclerView) rootView.findViewById(R.id.rcc);
        LinearLayoutManager = new LinearLayoutManager(rootView.getContext());
        list_item.setLayoutManager(LinearLayoutManager);
        get_informasi();
        ((MainActivity) getActivity()).dimana= "jadwal";
        list_item.addOnItemTouchListener( // and the click is handled
                new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d("click item", String.valueOf(position));
                        TextView id = (TextView) view.findViewById(R.id.inf_id);
                        Fragment fragment =  new fragment_detail_informasi();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.addToBackStack("tag");
                        ft.replace(R.id.flContent, fragment);
                        Bundle args = new Bundle();
                        args.putString("informasi_id", id.getText().toString());
                        fragment.setArguments(args);
                        ft.commit();
                    }
                }
                ));
        return rootView;
    }

    public void get_informasi() {
        local_database();
        String revisi_materi;

        InformasiObject = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM informasi where clicked = 'tidak'", null);
        if (cursor.moveToFirst()) {
            do {
                String fl_tgl_akhir = cursor.getString(cursor.getColumnIndex("fl_tgl_akhir"));
                int date_selesai = Integer.parseInt(fl_tgl_akhir.replace("-",""));
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String datesss = df.format(c.getTime());
                int date_now = Integer.parseInt(datesss.replace("-",""));
                if (date_now >= date_selesai) {
                String informasi_id = cursor.getString(cursor.getColumnIndex("informasi_id"));
                String parent_id = cursor.getString(cursor.getColumnIndex("informasi_parent_id"));
                String kepada = cursor.getString(cursor.getColumnIndex("kepada"));
                String dari = cursor.getString(cursor.getColumnIndex("dari"));
                String st_ralat = cursor.getString(cursor.getColumnIndex("st_ralat"));
                String st_penting = cursor.getString(cursor.getColumnIndex("st_penting"));
                String materi = cursor.getString(cursor.getColumnIndex("materi"));
                if (materi.length() > 30) {
                    revisi_materi = materi.substring(0,30) + ".....";
                }else {
                    revisi_materi = materi;
                }
                String fl_tgl_mulai = cursor.getString(cursor.getColumnIndex("fl_tgl_mulai"));
                String base_ralat = "";
                String base_penting = "";
                if (st_ralat.equals("1")) {
                    base_ralat = "iVBORw0KGgoAAAANSUhEUgAAAD4AAAAaCAIAAABgnngzAAAACXBIWXMAABJ0AAASdAHeZh94AAACuElEQVRYhe2XQUgUcRTGf6MDMiYJ7SUhJgqKkCQQx0OxRiiBgW1Et2Chgwqe8mDUBhlBCB2K8uAhuuTBU9BEEIt2kEIzZSWWtQUxcLEtw3BDdBXF12F2d3aXXZ0VYzH8eKf3//7vffPmvTeMMvz7BnsU/Z989Y3BisplkD1hFZXL9Y3BF8PdinZgNb5cVuwCFoxSdVMBKbaMHaKk2AJ2jn3pxcD/Jd1jIpJha0tEJum+kM10DyQIfm/uCKYnb2LNx4ogQqh3q9TpFujJDpK9OD2mWBi8KYYhze0y/ktERJbkgZ7BHJhNMBeHckcwPXnXs280wZGvoied2hExjIQNzouISND2nDiUFSS/dDuxR6IiIjLTn8b0ykIyu6yITytEui6h9dRlGXDn4JhWykDeh3fW6yY/rbd80PZ5r+MCpglvgEZLp6NIFvQuTqoQI/wD4GxHAXdTcCbdw2EAFiO2r80N8G2M10GAumtojrM+vIQK8RC3xwH08+QfirzYSnr5MQyD5nbGn1MFG9+5fytxpPmo1QBGntI3BqDW8Eh3ltNNw3GAgIn5lhhQRYd3m0s5kbfXM7Aqg502pycgIiJR8SDUyYyIiIR6HfV64nRdejQB+bgiIrLg39Vef3MFRUFx8WwSymh6nFx2OpdrABbCRA0Mhak5gOomnNS94xwA07w7jWEwEQZw1dO6i1W3a5as69wrAdF7JW09ZMDaFVtVPX0vZWLU9y82zAR/AChRITlkRGhQUCw7ShhwsCu62nABce6WJ+8qvI8BnGkpYNABtRAyYA9ZZIQPKWeEoSlOVWfvCmvQU1iLcbUWIP6FJ3Hb//IzjRfR6rincSfNvy0cNAzit17zrPiSp32ZX9ZUF/m9eQZdZD6a4Ay15u6i1KA7aZj9X41iYF96MbCXpZeqm8XWsBOUqpt/AaE6+ZsbiuTgAAAAAElFTkSuQmCC";
                } else {
                    base_ralat = "";
                }
                if (st_penting.equals("1")) {
                    base_penting = "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";
                } else {
                    base_penting = "";
                }
                Log.i(TAG,"penting : "  + st_penting);
                String[] array_tgl = fl_tgl_mulai.split("-");
                if ( array_tgl[1].equals("01")) {
                    bulan = "January";
                } else if ( array_tgl[1].equals("02")) {
                    bulan = "February";
                } else if ( array_tgl[1].equals("03")) {
                    bulan = "Maret";
                } else if ( array_tgl[1].equals("04")) {
                    bulan = "April";
                } else if ( array_tgl[1].equals("05")) {
                    bulan = "Mei";
                } else if ( array_tgl[1].equals("06")) {
                    bulan = "Juni";
                } else if ( array_tgl[1].equals("07")) {
                    bulan = "Juli";
                }else if ( array_tgl[1].equals("08")) {
                    bulan = "Agustus";
                }else if ( array_tgl[1].equals("09")) {
                    bulan = "September";
                }else if ( array_tgl[1].equals("10")) {
                    bulan = "Oktober";
                }else if ( array_tgl[1].equals("11")) {
                    bulan = "November";
                }else if ( array_tgl[1].equals("12")) {
                    bulan = "Desember";
                }
                get_day_name(Integer.parseInt(array_tgl[2]),Integer.parseInt(array_tgl[1]),Integer.parseInt(array_tgl[0]));
                InformasiObject.add(new InformasiObject( nama_hari + ", " + array_tgl[2] + " "  + bulan + " " + array_tgl[0] , revisi_materi  , base_ralat, base_penting, informasi_id));
            }
            }
            while (cursor.moveToNext());
        }

        iap = new InformasiAdapter(getActivity(), InformasiObject);
        list_item.setAdapter(iap);

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
