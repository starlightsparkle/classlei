package com.example.curriculumdesign.entity;

public class BaseResponse {

    /**
     * msg : success
     * code : 0
     */

    private String message;
    private int code;

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
}
