package com.example.arproject.model;

public class BaiGiang {
    public int MaBaiGiang;
    public String Ten;
    public String MoTa;
    public String TenGiaoVien;

    public BaiGiang(int maBaiGiang, String ten, String moTa, String tenGiaoVien) {
        MaBaiGiang = maBaiGiang;
        Ten = ten;
        MoTa = moTa;
        TenGiaoVien = tenGiaoVien;
    }

    public int getMaBaiGiang() {
        return MaBaiGiang;
    }

    public void setMaBaiGiang(int maBaiGiang) {
        MaBaiGiang = maBaiGiang;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getTenGiaoVien() {
        return TenGiaoVien;
    }

    public void setTenGiaoVien(String tenGiaoVien) {
        TenGiaoVien = tenGiaoVien;
    }
}
