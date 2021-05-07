package com.example.arproject.responseModel;

public class DangNhapResponse {
    private String token;
    private String status;
    private String TenDangNhap;

    public DangNhapResponse(String token, String status, String tenDangNhap) {
        this.token = token;
        this.status = status;
        TenDangNhap = tenDangNhap;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenDangNhap() {
        return TenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        TenDangNhap = tenDangNhap;
    }
}
