package com.example.curriculumdesign.entity.responseBody;

import com.example.curriculumdesign.entity.BaseResponse;
import com.example.curriculumdesign.entity.SignEntity;

import java.util.List;

public class SignResponse extends BaseResponse {
    private List<SignEntity> result;

    public List<SignEntity> getResult() {
        return result;
    }

    public void setResult(List<SignEntity> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "SignResponse{" +
                "result=" + result +
                '}';
    }
}
