package com.example.arproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.arproject.adapter.BaiGiangAdapter;
import com.example.arproject.api.ApiService;
import com.example.arproject.model.BaiGiang;
import com.example.arproject.model.MyCustomDialog;
import com.example.arproject.responseModel.LayDSBGResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonListAcitivity extends AppCompatActivity {
    ListView lvBaiGiang;
    ImageView imvBack;
    List<BaiGiang> mangBaiGiang;
    Button btnThemBaiGiang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list);
        // Get access token
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = preferences.getString("ACCESS_TOKEN",null);
        String tenDangNhap = preferences.getString("TenDangNhap",null);
        lvBaiGiang = (ListView) findViewById(R.id.listViewBaiGiang);
        LayDanhSachBaiGiang("TenDangNhap",accessToken);
        imvBack = (ImageView) findViewById(R.id.imageViewBack);
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LessonListAcitivity.this,HomePageActivity.class));
            }
        });
        btnThemBaiGiang = (Button) findViewById(R.id.btn_thembaigiang);
        btnThemBaiGiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog dialog = new MyCustomDialog();
                dialog.setContext(LessonListAcitivity.this);
                dialog.show(getSupportFragmentManager(),"MyCustomFragment");
            }
        });
    }

    private void LayDanhSachBaiGiang(String tenDangNhap, String accessToken) {
        ApiService.apiService.LayDSBG(tenDangNhap,"Bearer "+accessToken).enqueue(new Callback <List<BaiGiang>>() {
            @Override
            public void onResponse(Call<List<BaiGiang>> call, Response<List<BaiGiang>> response) {
                mangBaiGiang = new ArrayList<>();
                mangBaiGiang = response.body();
                BaiGiangAdapter adapter = new BaiGiangAdapter(
                        LessonListAcitivity.this,R.layout.dong_bai_giang,mangBaiGiang
                );
                lvBaiGiang.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lvBaiGiang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(LessonListAcitivity.this,""+mangBaiGiang.get(position).Ten,Toast.LENGTH_SHORT).show();
                    }
                });
//                Toast.makeText(LessonListAcitivity.this,"Call Api sucess",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<List<BaiGiang>> call, Throwable t) {
                Toast.makeText(LessonListAcitivity.this,"Call Api failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ThemBaiGiang(BaiGiang baiGiang){
        mangBaiGiang.add(baiGiang);
    }
}