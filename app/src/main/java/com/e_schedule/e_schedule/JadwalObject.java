package com.e_schedule.e_schedule;

/**
 * Created by Kang Juy on 9/16/2016.
 */
public class JadwalObject {

    public String jam_pelaksanaan,  tempat_alamat,sudah, jid;

    public JadwalObject(String jam_pelaksanaan,  String tempat_alamat,String sudah, String jid) {
        this.jam_pelaksanaan = jam_pelaksanaan;
//        this.strgambar = strgambar;
        this.tempat_alamat = tempat_alamat;
        this.sudah = sudah;
        this.jid = jid  ;
    }
}
