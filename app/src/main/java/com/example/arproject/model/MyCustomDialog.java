package com.example.arproject.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.example.arproject.LessonListAcitivity;
import com.example.arproject.R;
import com.example.arproject.RegisterActivity;
import com.example.arproject.api.ApiService;
import com.example.arproject.requestModel.ThemBaiGiangRequest;
import com.example.arproject.responseModel.ThemBaiGiangResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCustomDialog extends DialogFragment {
    private EditText edtThem;
    private Button btnThem;
    private Button btnHuy;
    private static final String TAG="MyCustomDialog";
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_them_bai_giang, container,false);
        btnThem = (Button) view.findViewById(R.id.btn_them);
        edtThem = (EditText) view.findViewById(R.id.ed_code);
        btnHuy = (Button) view.findViewById(R.id.btn_huy);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtThem.length()==0){
                    edtThem.requestFocus();
                    edtThem.setError("Vui lòng điền mã bài giảng");
                }else{
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    String accessToken = preferences.getString("ACCESS_TOKEN",null);
                    ThemBaiGiangRequest themBaiGiangRequest = new ThemBaiGiangRequest(Integer.parseInt(edtThem.getText().toString()));
                    ApiService.apiService.ThemBaiGiang(themBaiGiangRequest,"Bearer "+accessToken).enqueue(new Callback<ThemBaiGiangResponse>() {
                        @Override
                        public void onResponse(Call<ThemBaiGiangResponse> call, Response<ThemBaiGiangResponse> response) {
                            if(response.code()==200){
                                ThemBaiGiangResponse themBaiGiangResponse = response.body();
                                String status = themBaiGiangResponse.getStatus();
                                if (status.equals("Nhap sai ma bai giang")){
                                    edtThem.requestFocus();
                                    edtThem.setError("Nhập sai mã bài giảng");
                                } else if (status.equals("Ma bai giang da co")){
                                    edtThem.requestFocus();
                                    edtThem.setError("Mã bài giảng đã có trong danh sách bài giảng");
                                } else if (status.equals("Them bai giang thanh cong")){
                                    LessonListAcitivity lessonListAcitivity = (LessonListAcitivity) getContext();
                                    lessonListAcitivity.ThemBaiGiang(themBaiGiangResponse.getBaiGiang());
                                    getDialog().dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ThemBaiGiangResponse> call, Throwable t) {
                            Log.i("response","hihi");
                        }
                    });
                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
