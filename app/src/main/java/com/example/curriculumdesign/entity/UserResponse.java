package com.example.curriculumdesign.entity;

import java.io.Serializable;

public class UserResponse implements Serializable {
    private String message;
    private int code;
    private TblUser result;

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

    public TblUser getResult() {
        return result;
    }

    public void setResult(TblUser result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", result=" + result +
                '}';
    }
}
