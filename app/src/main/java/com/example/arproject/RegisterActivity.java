package com.example.arproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arproject.api.ApiService;
import com.example.arproject.model.Student;
import com.example.arproject.model.Ulti;
import com.example.arproject.responseModel.RegisterResponse;

import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtUsername ;
    private EditText edtName ;
    private EditText edtPassword ;
    private EditText edtPasswordAgian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtPasswordAgian = (EditText) findViewById(R.id.edt_passwordagian);
        Ulti ulti = new Ulti();
        ulti.setupUI(findViewById(R.id.register_view),RegisterActivity.this);
        EditText edtDateOfBirth = (EditText) findViewById(R.id.edt_dateOfBirth);
        edtDateOfBirth.setFocusable(false);
        edtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ulti.setCalendar(RegisterActivity.this,edtDateOfBirth);
            }
        });
        Button btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String name = edtName.getText().toString();
                String dateOfBirthText = edtDateOfBirth.getText().toString();
                String password = edtPassword.getText().toString();
                String passwordagian = edtPasswordAgian.getText().toString();
                if(username.length()==0){
                    edtUsername.requestFocus();
                    edtUsername.setError("M???c n??y kh??ng ???????c ????? tr???ng");
                }
                else if(name.length()==0){
                    edtName.requestFocus();
                    edtName.setError("M???c n??y kh??ng ???????c ????? tr???ng");
                } else if(dateOfBirthText.length()==0){
                    edtDateOfBirth.requestFocus();
                    edtDateOfBirth.setError("M???c n??y kh??ng ???????c ????? tr???ng");
                } else if (password.length() == 0){
                    edtPassword.requestFocus();
                    edtPassword.setError("M???c n??y kh??ng ???????c ????? tr???ng");
                } else if (passwordagian.length()==0) {
                    edtPasswordAgian.requestFocus();
                    edtPasswordAgian.setError("M???c n??y kh??ng ???????c ????? tr???ng");
                }
                else if (password.length() <6){
                    edtPassword.requestFocus();
                    edtPassword.setError("M???t kh???u ph???i c?? ??t nh???t 6 k?? t???");
                }
                else if (!password.equals(passwordagian)){
                    edtPasswordAgian.requestFocus();
                    edtPasswordAgian.setError("M???t kh???u x??c nh???n ph???i tr??ng");
                }
                else {
                    String[] dateMonthYear = dateOfBirthText.split("/");
                    int date = Integer.parseInt(dateMonthYear[0]);
                    int month = Integer.parseInt(dateMonthYear[1]);
                    int year = Integer.parseInt(dateMonthYear[2]);
                    Date dateOfBirth = new GregorianCalendar(year,month-1,date).getTime();
                    Student student = new Student(1,name,dateOfBirth,username,password);
                    Register(student);
                }
            }
        });
    }
    public void Register(Student student){
        ApiService.apiService.Register(student).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse registerResponse = response.body();
                if (response.code()==200){
                    if(registerResponse.getStatus().equals("username is exsited")){
                        edtUsername.requestFocus();
                        edtUsername.setError("T??n ????ng nh???p ???? t???n t???i");
                    }else if(registerResponse.getStatus().equals("success")){
                        startActivity(new Intent(RegisterActivity.this,LoginAcitivity.class));
                    }
                }else{
                    Toast.makeText(RegisterActivity.this,"L???i kh??ng x??c ?????nh",Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(context,"Call api success",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
//                Toast.makeText(context,"Call api failed",Toast.LENGTH_SHORT).show();
            }
        });
   }
}