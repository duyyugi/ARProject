package com.example.arproject.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arproject.MainActivity;
import com.example.arproject.RegisterActivity;
import com.example.arproject.api.ApiService;
import com.example.arproject.responseModel.DangKyResponse;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HocSinh {
    private int MaHocSinh;
    private String Ten;
    private Date NgaySinh;
    private String TenDangNhap;
    private String MatKhau;

    public HocSinh(int maHocSinh, String ten, Date ngaySinh, String tenDangNhap, String matKhau) {
        MaHocSinh = maHocSinh;
        Ten = ten;
        NgaySinh = ngaySinh;
        TenDangNhap = tenDangNhap;
        MatKhau = matKhau;
    }
}
