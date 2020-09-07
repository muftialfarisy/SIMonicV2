package com.KP.simonicv2.Reportm;

public class Report_m{
    private String masalah;
    private String tgl;
    private String jam;


    public Report_m(String masalah, String tgl, String jam){
        this.masalah = masalah;
        this.tgl = tgl;
        this.jam = jam;
    }
    public String getMasalah() {
        return masalah;
    }

    public void setMasalah(String masalah) {
        this.masalah = masalah;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }


}
