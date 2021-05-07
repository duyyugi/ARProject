package com.example.arproject.api;

import com.example.arproject.model.BaiGiang;
import com.example.arproject.model.HocSinh;
import com.example.arproject.requestModel.DangNhapRequest;
import com.example.arproject.requestModel.ThemBaiGiangRequest;
import com.example.arproject.responseModel.DangKyResponse;
import com.example.arproject.responseModel.DangNhapResponse;
import com.example.arproject.responseModel.LayDSBGResponse;
import com.example.arproject.responseModel.ThemBaiGiangResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.2:3001/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
//    @GET("actor")
//    Call<NoiDungHoc> getNoiDungHoc();
//@FormUrlEncoded
@POST("signup/mobile")
    Call<DangKyResponse> DangKy(@Body HocSinh hocSinh);
@POST("login/mobile")
    Call<DangNhapResponse> DangNhap(@Body DangNhapRequest dangNhapRequest);
@GET("/lesson")
    Call<List<BaiGiang>> LayDSBG(@Query("TenDangNhap") String tenDangNhap, @Header("Authorization") String authorization);
@POST("/lesson")
    Call<ThemBaiGiangResponse> ThemBaiGiang(@Body ThemBaiGiangRequest themBaiGiangRequest, @Header("Authorization") String authorization);
}
