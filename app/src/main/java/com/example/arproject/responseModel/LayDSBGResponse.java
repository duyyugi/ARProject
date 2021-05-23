package com.example.arproject.responseModel;

import com.example.arproject.model.Lesson;

import java.util.List;

public class LayDSBGResponse {
    List<Lesson> DSLesson;
    public LayDSBGResponse(List<Lesson> DSLesson) {
        this.DSLesson = DSLesson;
    }

    @Override
    public String toString() {
        return "LayDSBGResponse{" +
                "DSBaiGiang=" + DSLesson +
                '}';
    }
}
