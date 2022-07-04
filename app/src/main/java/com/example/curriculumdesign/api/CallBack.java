package com.example.curriculumdesign.api;

import okhttp3.Response;

public interface CallBack {
    void OnSuccess(String res, Response response);

    void OnFailure(Exception e);
}
