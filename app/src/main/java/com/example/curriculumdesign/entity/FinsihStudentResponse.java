package com.example.curriculumdesign.entity;

import java.util.List;

public class FinsihStudentResponse {
    private String message;
    private int code;
    private List<TblUserSign> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<TblUserSign> getResult() {
        return result;
    }

    public void setResult(List<TblUserSign> result) {
        this.result = result;
    }
}
