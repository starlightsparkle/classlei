package com.example.curriculumdesign.entity.responseBody;

import com.example.curriculumdesign.entity.BaseResponse;
import com.example.curriculumdesign.entity.TblUser;

import java.util.List;

public class SignStuResponse extends BaseResponse {
    private List<TblUser> result;

    public List<TblUser> getResult() {
        return result;
    }

    public void setResult(List<TblUser> result) {
        this.result = result;
    }
}
