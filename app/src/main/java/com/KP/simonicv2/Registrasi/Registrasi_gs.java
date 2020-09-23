package com.KP.simonicv2.Registrasi;

public class Registrasi_gs {
    public String getUuid() {
        return uuid;
    }
    public Registrasi_gs(String alamat, String lat, String lng, String device, String durasi,String jenis_kelamin,
                         String nama, String nik, String riwayat,String selesai,String uuid,String provinsi, String kota, String kecamatan, String kelurahan,String device_id){

        this.uuid =uuid;
        this.nik =nik;
        this.device =device;
        this.nama =nama;
        this.alamat =alamat;
        this.lat = lat;
        this.lng = lng;
        this.riwayat =riwayat;
        this.durasi =durasi;

        this.jenis_kelamin =jenis_kelamin;
        this.selesai = selesai;
        this.provinsi = provinsi;
        this.kota = kota;
        this.kecamatan = kecamatan;
        this.kelurahan = kelurahan;
        this.device_id = device_id;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    String  uuid;
    String nik;
    String device;
    String nama;
    String alamat;
    String lat;
    String lng;
    String riwayat;
    String durasi;
    String provinsi;
    String kota;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    String device_id;
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
