package com.example.arproject.requestModel;

public class DangNhapRequest {
    private String TenDangNhap;
    private String MatKhau;

    public DangNhapRequest(String tenDangNhap, String matKhau) {
        TenDangNhap = tenDangNhap;
        MatKhau = matKhau;
    }
    public String getTenDangNhap() {
        return TenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        TenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }
}