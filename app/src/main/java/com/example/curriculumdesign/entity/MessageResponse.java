package com.example.curriculumdesign.entity;

import java.io.Serializable;
import java.util.List;

public class MessageResponse implements Serializable {
    private String message;
    private int code;
    private List<MessageEntity> result;

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

    public List<MessageEntity> getResult() {
        return result;
    }

    public void setResult(List<MessageEntity> result) {
        this.result = result;
    }
}
