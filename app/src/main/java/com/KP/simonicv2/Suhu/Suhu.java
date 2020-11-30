package com.KP.simonicv2.Suhu;

public class Suhu {
    private Integer suhu;
    private Integer tanggal;

    public Suhu(Integer suhu ,Integer tanggal){
        this.suhu = suhu;

        this.tanggal = tanggal;

    }
    public Suhu(){

    }

    public Integer getTanggal() {
        return tanggal;
    }

    public void setTanggal(Integer tanggal) {
        this.tanggal = tanggal;
    }


    public Integer getSuhu() {
        return suhu;
    }

    public void setSuhu(Integer suhu) {
        this.suhu = suhu;
    }


}
