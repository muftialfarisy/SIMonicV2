package com.KP.simonicv2.Reportc;

public class Report_c {
    String rs;
    String tanggal;
    String haemoglobin;
    String leukosit;
    String trombosit;
    String elektrolit;
    String kadar_puasa;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key;

    public Report_c(){

    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private boolean expanded;
    public Report_c(String rs,String tanggal,String haemoglobin, String leukosit,
                      String trombosit, String elektrolit, String kadar_puasa, String kadar_setelah_puasa, String kolesterol,
                      String asam_urat, String fungsi_hati, String fungsi_ginjal){
        this.rs = rs;
        this.tanggal = tanggal;
        this.haemoglobin = haemoglobin;
        this.leukosit = leukosit;
        this.trombosit = trombosit;
        this.elektrolit = elektrolit;
        this.kadar_puasa = kadar_puasa;
        this.kadar_setelah_puasa = kadar_setelah_puasa;
        this.kolesterol = kolesterol;
        this.asam_urat = asam_urat;
        this.fungsi_hati = fungsi_hati;
        this.fungsi_ginjal = fungsi_ginjal;
        this.expanded = false;
    }
    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getHaemoglobin() {
        return haemoglobin;
    }

    public void setHaemoglobin(String haemoglobin) {
        this.haemoglobin = haemoglobin;
    }

    public String getLeukosit() {
        return leukosit;
    }

    public void setLeukosit(String leukosit) {
        this.leukosit = leukosit;
    }

    public String getTrombosit() {
        return trombosit;
    }

    public void setTrombosit(String trombosit) {
        this.trombosit = trombosit;
    }

    public String getElektrolit() {
        return elektrolit;
    }

    public void setElektrolit(String elektrolit) {
        this.elektrolit = elektrolit;
    }

    public String getKadar_puasa() {
        return kadar_puasa;
    }

    public void setKadar_puasa(String kadar_puasa) {
        this.kadar_puasa = kadar_puasa;
    }

    public String getKadar_setelah_puasa() {
        return kadar_setelah_puasa;
    }

    public void setKadar_setelah_puasa(String kadar_setelah_puasa) {
        this.kadar_setelah_puasa = kadar_setelah_puasa;
    }

    public String getKolesterol() {
        return kolesterol;
    }

    public void setKolesterol(String kolesterol) {
        this.kolesterol = kolesterol;
    }

    public String getAsam_urat() {
        return asam_urat;
    }

    public void setAsam_urat(String asam_urat) {
        this.asam_urat = asam_urat;
    }

    public String getFungsi_hati() {
        return fungsi_hati;
    }

    public void setFungsi_hati(String fungsi_hati) {
        this.fungsi_hati = fungsi_hati;
    }

    public String getFungsi_ginjal() {
        return fungsi_ginjal;
    }

    public void setFungsi_ginjal(String fungsi_ginjal) {
        this.fungsi_ginjal = fungsi_ginjal;
    }

    String kadar_setelah_puasa;
    String kolesterol;
    String asam_urat;
    String fungsi_hati;
    String fungsi_ginjal;
}

