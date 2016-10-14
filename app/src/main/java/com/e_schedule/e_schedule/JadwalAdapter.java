
package com.e_schedule.e_schedule;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

public class JadwalAdapter extends RecyclerView.Adapter<HolderJadwal> {

    Context context;
    List<JadwalObject> jadwalObjects;

    public JadwalAdapter(Context context, List<JadwalObject> jadwalObjects) {
        this.context = context;
        this.jadwalObjects = jadwalObjects;
    }

    @Override
    public HolderJadwal onCreateViewHolder(ViewGroup parent, int viewType) {
        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.jadwal_list, null);
        HolderJadwal holderJadwal = new HolderJadwal(view2);
        return holderJadwal;
    }


    @Override
    public void onBindViewHolder(HolderJadwal hj, int position) {
       hj.jam.setText(jadwalObjects.get(position).jam_pelaksanaan);
        hj.tempat_alamat.setText(jadwalObjects.get(position).tempat_alamat);
//        Glide.with(context)
//                .load(Base64.decode(jadwalObjects.get(position).strgambar, Base64.DEFAULT))
//                .asBitmap()
//                .placeholder(R.mipmap.ic_launcher)
//                .into(hj.masa);
       hj.jadwal_id.setText( jadwalObjects.get(position).jid);
        String mamat = jadwalObjects.get(position).sudah;
        if (mamat.equals("beres")) {
            hj.masa.setTag("beres");
            hj.masa.setImageResource(R.drawable.sudah);
//            hj.cardView.setCardBackgroundColor(Color.parseColor("#b70505"));
        }else if (mamat.equals("sedang")){
//            hj.cardView.setCardBackgroundColor(Color.parseColor("#9ACD32"));
            hj.masa.setTag("sedang");
            hj.masa.setImageResource(R.drawable.sedang);
        }else {
            hj.masa.setImageResource(R.drawable.belum);
        }


    }
    @Override
    public int getItemCount() {
        return jadwalObjects.size();
    }


}
