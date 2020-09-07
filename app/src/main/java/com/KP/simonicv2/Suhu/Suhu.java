package com.KP.simonicv2.Suhu;

public class Suhu {
    private String ht;
    private String keterangan;

    public Suhu(String ht ,String keterangan){
        this.ht = ht;

        this.keterangan = keterangan;

    }
    public String getHt() {
        return ht;
    }

    public void setHt(String ht) {
        this.ht = ht;
    }


    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }


}
