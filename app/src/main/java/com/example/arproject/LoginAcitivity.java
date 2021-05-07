package com.example.arproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arproject.api.ApiService;
import com.example.arproject.model.Ulti;
import com.example.arproject.requestModel.DangNhapRequest;
import com.example.arproject.responseModel.DangNhapResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAcitivity extends AppCompatActivity {

    private EditText edTenDangNhap;
    private EditText edMatKhau;
    private Button btnDangNhap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Ulti skb = new Ulti();
        skb.setupUI(findViewById(R.id.login_view),LoginAcitivity.this);
        edTenDangNhap = (EditText) findViewById(R.id.ed_tendangnhapLogin);
        edMatKhau = (EditText) findViewById(R.id.ed_matkhauLogin);
        btnDangNhap = (Button) findViewById(R.id.btn_dangnhap);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenDangNhap = edTenDangNhap.getText().toString();
                String matKhau = edMatKhau.getText().toString();
                DangNhapRequest dangNhapRequest = new DangNhapRequest(tenDangNhap,matKhau);
                DangNhap(dangNhapRequest);
            }
        });
    }

    public void DangNhap(DangNhapRequest dangNhapRequest){
        ApiService.apiService.DangNhap(dangNhapRequest).enqueue(new Callback<DangNhapResponse>() {
            @Override
            public void onResponse(Call<DangNhapResponse> call, Response<DangNhapResponse> response) {
               if (response.code()==200){
                   DangNhapResponse dangNhapResponse = response.body();
                   if (dangNhapResponse.getStatus().equals("dang nhap thanh cong")){
                       String token = dangNhapResponse.getToken();
                       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginAcitivity.this);
                       preferences.edit().putString("ACCESS_TOKEN",token).apply();
                       preferences.edit().putString("TenDangNhap",dangNhapResponse.getTenDangNhap()).apply();
                       LoginAcitivity.this.finish();
                       startActivity(new Intent(LoginAcitivity.this,HomePageActivity.class));
                   } else if (dangNhapResponse.getStatus().equals("ten dang nhap hoac mat khau khong dung")){
                       Toast.makeText(LoginAcitivity.this,"Tên đăng nhập hoặc mật khẩu không đúng",Toast.LENGTH_LONG).show();
                   } else if (dangNhapResponse.getStatus().equals("ten dang nhap khong ton tai")){
                       edTenDangNhap.requestFocus();
                       edTenDangNhap.setError("Tên đăng nhập không tồn tại");
                   } else if (dangNhapResponse.getStatus().equals("sai mat khau")) {
                       edMatKhau.requestFocus();
                       edMatKhau.setError("Sai mật khẩu");
                   }
               }else{
                   Toast.makeText(LoginAcitivity.this,"Lỗi không xác định",Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onFailure(Call<DangNhapResponse> call, Throwable t) {

            }
        });
    }

}