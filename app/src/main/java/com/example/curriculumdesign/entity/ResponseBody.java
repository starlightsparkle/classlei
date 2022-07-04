package com.example.curriculumdesign.entity;

public class ResponseBody {
    private Object result;
    private String message;
    private int code;

    public ResponseBody(Object result, String message, int code) {
        this.result = result;
        this.message = message;
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

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
