package com.e_schedule.e_schedule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kang Juy on 10/5/2016.
 */

public class HolderInformasi extends RecyclerView.ViewHolder{
public TextView tanggal, materi, inf_id;
public ImageView img_icon, penting;
        public RecyclerView rcc;


public HolderInformasi(View itemView) {
        super(itemView);

        tanggal = (TextView) itemView.findViewById(R.id.tanggal);
        materi = (TextView) itemView.findViewById(R.id.materi);
        img_icon = (ImageView) itemView.findViewById(R.id.img_icon);
        penting = (ImageView) itemView.findViewById(R.id.penting);
        inf_id = (TextView) itemView.findViewById(R.id.inf_id);
        rcc = (RecyclerView) itemView.findViewById(R.id.rcc);
        }

}