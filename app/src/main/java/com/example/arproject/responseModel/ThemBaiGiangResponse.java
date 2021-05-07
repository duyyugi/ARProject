package com.example.arproject.responseModel;

import com.example.arproject.model.BaiGiang;

public class ThemBaiGiangResponse {
    private String status;
    private BaiGiang baiGiang;

    public ThemBaiGiangResponse(String status, BaiGiang baiGiang) {
        this.status = status;
        this.baiGiang = baiGiang;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BaiGiang getBaiGiang() {
        return baiGiang;
    }

    public void setBaiGiang(BaiGiang baiGiang) {
        this.baiGiang = baiGiang;
    }

    @Override
    public String toString() {
        return "ThemBaiGiangResponse{" +
                "status='" + status + '\'' +
                ", baiGiang=" + baiGiang +
                '}';
    }
}
