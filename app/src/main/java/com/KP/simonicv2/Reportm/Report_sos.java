package com.KP.simonicv2.Reportm;

public class Report_sos {
    private String sos;
    private String tgl;
    private String jam;

    public Report_sos(String sos, String tgl, String jam){
        this.sos = sos;
        this.tgl = tgl;
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
        return tgl;
    }

    public void setTanggal(String tgl) {
        this.tgl = tgl;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

}
