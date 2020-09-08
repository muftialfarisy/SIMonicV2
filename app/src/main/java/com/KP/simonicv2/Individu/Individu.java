package com.KP.simonicv2.Individu;

import android.os.Parcel;
import android.os.Parcelable;

public class Individu implements Parcelable {
    String nama;
    String wilayah;
    String alamat;
    String provinsi;
    String kota;
    String kecamatan;
    String kelurahan;
    String uuid;
    String nik;
    String device;

    public String getUuid() {
        return uuid;
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

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getSelesai() {
        return selesai;
    }

    public void setSelesai(String selesai) {
        this.selesai = selesai;
    }

    String durasi;
    String selesai;
    public String getKey() {
        return key;
    }
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
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
    public Individu(){
    }
    public void setKey(String key) {
        this.key = key;
    }

    String key;
    public Individu(String nama, String nik,String alamat, String provinsi, String kota, String kecamatan, String kelurahan,String durasi, String selesai ){
        this.nama = nama;
        this.nik = nik;
        this.alamat = alamat;
        this.provinsi = provinsi;
        this.kota = kota;
        this.kecamatan = kecamatan;
        this.kelurahan = kelurahan;
        this.durasi = durasi;
        this.selesai = selesai;
    }
    protected Individu(Parcel in) {
        nama = in.readString();
        wilayah = in.readString();
    }

    public static final Creator<Individu> CREATOR = new Creator<Individu>() {
        @Override
        public Individu createFromParcel(Parcel in) {
            return new Individu(in);
        }

        @Override
        public Individu[] newArray(int size) {
            return new Individu[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nama);
        parcel.writeString(wilayah);
    }
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getWilayah() {
        return wilayah;
    }

    public void setWilayah(String wilayah) {
        this.wilayah = wilayah;
    }

}
