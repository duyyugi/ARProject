package com.example.arproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arproject.api.ApiService;
import com.example.arproject.model.HocSinh;
import com.example.arproject.model.Ulti;
import com.example.arproject.responseModel.DangKyResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etTenDangNhap ;
    private EditText etHoTen ;
    private EditText etMatKhau ;
    private EditText etXacNhanMatKhau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etTenDangNhap = (EditText) findViewById(R.id.ed_tendangnhap);
        etHoTen = (EditText) findViewById(R.id.ed_hoten);
        etMatKhau = (EditText) findViewById(R.id.ed_matkhau);
        etXacNhanMatKhau = (EditText) findViewById(R.id.ed_xacnhanmatkhau);
        Ulti ulti = new Ulti();
        ulti.setupUI(findViewById(R.id.register_view),RegisterActivity.this);
        EditText etNgaySinh = (EditText) findViewById(R.id.ed_ngaysinh);
        etNgaySinh.setFocusable(false);
        etNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ulti.setCalendar(RegisterActivity.this,etNgaySinh);
            }
        });
        Button btnDangKy = (Button) findViewById(R.id.btn_dangky2);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenDangNhap = etTenDangNhap.getText().toString();
                String hoTen = etHoTen.getText().toString();
                String ngaySinhText = etNgaySinh.getText().toString();
                String matKhau = etMatKhau.getText().toString();
                String xacNhanMatKhau = etXacNhanMatKhau.getText().toString();
                if(tenDangNhap.length()==0){
                    etTenDangNhap.requestFocus();
                    etTenDangNhap.setError("Mục này không được để trống");
                }
                else if(hoTen.length()==0){
                    etHoTen.requestFocus();
                    etHoTen.setError("Mục này không được để trống");
                } else if(ngaySinhText.length()==0){
                    etNgaySinh.requestFocus();
                    etNgaySinh.setError("Mục này không được để trống");
                } else if (matKhau.length() == 0){
                    etMatKhau.requestFocus();
                    etMatKhau.setError("Mục này không được để trống");
                } else if (xacNhanMatKhau.length()==0) {
                    etXacNhanMatKhau.requestFocus();
                    etXacNhanMatKhau.setError("Mục này không được để trống");
                }
                else if (matKhau.length() <6){
                    etMatKhau.requestFocus();
                    etMatKhau.setError("Mật khẩu phải có ít nhất 6 ký tự");
                }
                else if (!matKhau.equals(xacNhanMatKhau)){
                    etXacNhanMatKhau.requestFocus();
                    etXacNhanMatKhau.setError("Mật khẩu xác nhận phải trùng");
                }
                else {
                    String[] ngayThangNam = ngaySinhText.split("/");
                    int ngay = Integer.parseInt(ngayThangNam[0]);
                    int thang = Integer.parseInt(ngayThangNam[1]);
                    int nam = Integer.parseInt(ngayThangNam[2]);
                    Date ngaySinh = new GregorianCalendar(nam,thang-1,ngay).getTime();
                    HocSinh hocSinh = new HocSinh(1,hoTen,ngaySinh,tenDangNhap,matKhau);
                    DangKy(hocSinh);
                }
            }
        });
    }
    public void DangKy(HocSinh hocSinh){
        ApiService.apiService.DangKy(hocSinh).enqueue(new Callback<DangKyResponse>() {
            @Override
            public void onResponse(Call<DangKyResponse> call, Response<DangKyResponse> response) {
                DangKyResponse dangKyResponse = response.body();
                if (response.code()==200){
                    if(dangKyResponse.getStatus().equals("Ten Dang Nhap da ton tai")){
                        etTenDangNhap.requestFocus();
                        etTenDangNhap.setError("Tên đăng nhập đã tồn tại");
                    }else if(dangKyResponse.getStatus().equals("success")){
                        startActivity(new Intent(RegisterActivity.this,LoginAcitivity.class));
                    }
                }else{
                    Toast.makeText(RegisterActivity.this,"Lỗi không xác định",Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(context,"Call api success",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<DangKyResponse> call, Throwable t) {
//                Toast.makeText(context,"Call api failed",Toast.LENGTH_SHORT).show();
            }
        });
   }
}