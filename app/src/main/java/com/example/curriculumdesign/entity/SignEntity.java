package com.example.curriculumdesign.entity;

import java.io.Serializable;

public class SignEntity implements Serializable {
    private String gmtCreated;
    private String gmtModified;
    private Long signId;

    private Long classId;

    private Long userId;

    /**
     * 是否是教师的第一次签到
     */
    private Integer isTeacher;

    /**
     * 是gps签到还是普通签到 1为gps 0为二维码
     */
    private Integer signType;

    /**
     * 每一节课每一次签到的标识
     */
    private Long classSignId;

    /**
     * gps签到
     */
    private String locationXy;

    //签到名字
    private String signName;

    //签到状态
    private Integer status;

    public SignEntity(String gmtCreated, String signName, Integer status) {
        this.gmtCreated = gmtCreated;
        this.signName = signName;
        this.status = status;
    }

    @Override
    public String toString() {
        return "SignEntity{" +
                "gmtCreated='" + gmtCreated + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                ", signId=" + signId +
                ", classId=" + classId +
                ", userId=" + userId +
                ", isTeacher=" + isTeacher +
                ", signType=" + signType +
                ", classSignId=" + classSignId +
                ", locationXy='" + locationXy + '\'' +
                ", signName='" + signName + '\'' +
                ", status=" + status +
                '}';
    }

    public SignEntity(String gmtCreated, String gmtModified, Long signId, Long classId, Long userId, Integer isTeacher, Integer signType, Long classSignId, String locationXy, String signName, Integer status) {
        this.gmtCreated = gmtCreated;
        this.gmtModified = gmtModified;
        this.signId = signId;
        this.classId = classId;
        this.userId = userId;
        this.isTeacher = isTeacher;
        this.signType = signType;
        this.classSignId = classSignId;
        this.locationXy = locationXy;
        this.signName = signName;
        this.status = status;
    }

    /**
     * gps签到
     */

    public String getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(String gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getSignId() {
        return signId;
    }

    public void setSignId(Long signId) {
        this.signId = signId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(Integer isTeacher) {
        this.isTeacher = isTeacher;
    }

    public Integer getSignType() {
        return signType;
    }

    public void setSignType(Integer signType) {
        this.signType = signType;
    }

    public Long getClassSignId() {
        return classSignId;
    }

    public void setClassSignId(Long classSignId) {
        this.classSignId = classSignId;
    }

    public String getLocationXy() {
        return locationXy;
    }

    public void setLocationXy(String locationXy) {
        this.locationXy = locationXy;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
