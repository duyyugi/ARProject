package com.example.arproject.responseModel;

import com.example.arproject.model.BaiGiang;

import java.util.List;

public class LayDSBGResponse {
    List<BaiGiang> DSBaiGiang;
    public LayDSBGResponse(List<BaiGiang> DSBaiGiang) {
        this.DSBaiGiang = DSBaiGiang;
    }
}
