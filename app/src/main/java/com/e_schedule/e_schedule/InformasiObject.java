package com.e_schedule.e_schedule;

public class InformasiObject {

    public String strTanggal, strMateri, strgambar, strpenting, strinf_id;

    public InformasiObject(String strJudul, String strWaktu, String strgambar, String strpenting, String strinf_id) {
        this.strTanggal = strJudul;
        this.strMateri = strWaktu;
        this.strgambar = strgambar;
        this.strpenting = strpenting;
        this.strinf_id = strinf_id;
    }
}
