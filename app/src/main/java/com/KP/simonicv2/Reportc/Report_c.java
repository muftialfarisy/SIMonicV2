package com.KP.simonicv2.Reportc;

public class Report_c {
    private String nama_rs;
    private boolean expanded;
    public String getNama_rs() {
        return nama_rs;
    }

    public void setNama_rs(String nama_rs) {
        this.nama_rs = nama_rs;
    }

    public String getTgl_reportc() {
        return tgl_reportc;
    }

    public void setTgl_reportc(String tgl_reportc) {
        this.tgl_reportc = tgl_reportc;
    }
    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    private String tgl_reportc;

    public Report_c(String nama_rs, String tgl_reportc){
        this.nama_rs = nama_rs;
        this.tgl_reportc = tgl_reportc;
        this.expanded = false;

    }

}
