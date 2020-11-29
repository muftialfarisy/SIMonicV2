package com.KP.simonicv2.Reportm;

public class Report_m{
    private String masalah;
    private String tanggal;
    private String jam;

    public Report_m(){

    }
    public Report_m(String masalah, String tanggal, String jam){
        this.masalah = masalah;
        this.tanggal = tanggal;
        this.jam = jam;
    }

    public String getMasalah() {
        return masalah;
    }

    public void setMasalah(String masalah) {
        this.masalah = masalah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tgl) {
        this.tanggal = tgl;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }


}
