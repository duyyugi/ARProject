package com.example.arproject.api;

import com.example.arproject.model.Lesson;
import com.example.arproject.model.Student;
import com.example.arproject.requestModel.LoginRequest;
import com.example.arproject.requestModel.AddLessonRequest;
import com.example.arproject.responseModel.RegisterResponse;
import com.example.arproject.responseModel.LoginResponse;
import com.example.arproject.responseModel.GetMarkerListResponse;
import com.example.arproject.responseModel.AddLessonResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
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
    Call<RegisterResponse> Register(@Body Student student);
@POST("login/mobile")
    Call<LoginResponse> Login(@Body LoginRequest loginRequest);
@GET("/lesson")
    Call<List<Lesson>> getLessonList(@Query("username") String username, @Header("Authorization") String authorization);
@POST("/lesson")
    Call<AddLessonResponse> addLesson(@Body AddLessonRequest addLessonRequest, @Header("Authorization") String authorization);
@GET("/lesson/marker")
    Call<GetMarkerListResponse> getMarkerList(@Query("lessonID") String lessonID);
}

