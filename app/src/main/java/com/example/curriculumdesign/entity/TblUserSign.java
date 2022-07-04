package com.example.curriculumdesign.entity;

import java.io.Serializable;

public class TblUserSign implements Serializable {

    private String locationXy;

    private String username;

    public String getLocationXy() {
        return locationXy;
    }

    public void setLocationXy(String locationXy) {
        this.locationXy = locationXy;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
