package com.e_schedule.e_schedule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

public class InformasiAdapter extends RecyclerView.Adapter<HolderInformasi> {

    Context context;
    List<InformasiObject> InformasiObjects;

    public InformasiAdapter(Context context, List<InformasiObject> InformasiObjects) {
        this.context = context;
        this.InformasiObjects = InformasiObjects;
    }

    @Override
    public HolderInformasi onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.informasi_list, null);
        HolderInformasi HolderInformasi=new HolderInformasi(view);
        return HolderInformasi;
    }

    @Override
    public void onBindViewHolder(HolderInformasi holder, int position) {
        holder.tanggal.setText(InformasiObjects.get(position).strTanggal);
        holder.materi.setText(InformasiObjects.get(position).strMateri);
        Glide.with(context)
                .load(Base64.decode(InformasiObjects.get(position).strgambar, Base64.DEFAULT))
                .asBitmap()
                .into(holder.img_icon);

        Glide.with(context)
                .load(Base64.decode(InformasiObjects.get(position).strpenting, Base64.DEFAULT))
                .asBitmap()
                .into(holder.penting);
        holder.inf_id.setText(InformasiObjects.get(position).strinf_id);
    }
    @Override
    public int getItemCount() {
        return InformasiObjects.size();
    }


}
