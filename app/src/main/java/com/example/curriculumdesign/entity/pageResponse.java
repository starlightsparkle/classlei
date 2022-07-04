package com.example.curriculumdesign.entity;

public class pageResponse {

    private String message;
    private int code;
    private Page result;

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

    public Page getResult() {
        return result;
    }

    public void setResult(Page result) {
        this.result = result;
    }
}