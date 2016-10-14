package com.e_schedule.e_schedule;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class HolderJadwal extends RecyclerView.ViewHolder{
    public TextView jam, tempat_alamat, jadwal_id;
    public ImageView masa;
    public CardView cardView;


    public HolderJadwal(View itemView) {
        super(itemView);

        jam = (TextView) itemView.findViewById(R.id.jam);
        tempat_alamat = (TextView) itemView.findViewById(R.id.tempat_alamat);
        masa = (ImageView) itemView.findViewById(R.id.masa);
        jadwal_id = (TextView) itemView.findViewById(R.id.jadwal_id);
        cardView = (CardView) itemView.findViewById(R.id.carditem_planet);
    }

}