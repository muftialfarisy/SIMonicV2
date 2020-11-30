package com.KP.simonicv2.Reportm;

public class Report_sos {
    private String sos;
    private String tanggal;
    private String jam;

    public Report_sos(String sos, String tanggal, String jam){
        this.sos = sos;
        this.tanggal = tanggal;
        this.jam = jam;
    }
    public Report_sos(){

    }
    public String getMasalah() {
        return sos;
    }

    public void setMasalah(String sos) {
        this.sos = sos;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

}
