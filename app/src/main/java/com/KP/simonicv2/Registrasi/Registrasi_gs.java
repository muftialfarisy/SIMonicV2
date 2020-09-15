package com.KP.simonicv2.Registrasi;

public class Registrasi_gs {
    public String getUuid() {
        return uuid;
    }
    public Registrasi_gs(String alamatt, String lat, String lng, String idd, String s, String text, String namaa, String nikk, String riwayatt, String toString, String string, String provinsi, String kota, String kecamatan, String kelurahan){
        this.uuid =uuid;
        this.nik =nik;
        this.device =device;
        this.nama =nama;
        this.alamat =alamat;
        this.coordinate =coordinate;
        this.riwayat =riwayat;
        this.durasi =durasi;

        this.jenis_kelamin =jenis_kelamin;
        this.selesai = selesai;
        this.provinsi = provinsi;
        this.kota = kota;
        this.kecamatan = kecamatan;
        this.kelurahan = kelurahan;
    }
    public Registrasi_gs(){

    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getRiwayat() {
        return riwayat;
    }

    public void setRiwayat(String riwayat) {
        this.riwayat = riwayat;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    String  uuid;
    String nik;
    String device;
    String nama;
    String alamat;
    String coordinate;
    String riwayat;
    String durasi;
    String provinsi;
    String kota;

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    String kecamatan;
    String kelurahan;

    public String getSelesai() {
        return selesai;
    }

    public void setSelesai(String selesai) {
        this.selesai = selesai;
    }



    String selesai;

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    String jenis_kelamin;


}
