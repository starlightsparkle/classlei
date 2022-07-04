package com.example.curriculumdesign.entity;


import java.io.Serializable;
import java.time.LocalDateTime;

public class ClassEntity implements Serializable {
    private long classId;
    /**
     * 创建人id
     */
    private Long createId;
    /**
     * 课程名
     */
    private String className;

    /**
     * 课程内容
     */
    private String classContent;

    public ClassEntity(long id, Long createId, String className, String classContent, String gmtCreated, String gmtModified) {
        this.classId = id;
        this.createId = createId;
        this.className = className;
        this.classContent = classContent;
        this.gmtCreated = gmtCreated;
        this.gmtModified = gmtModified;
    }


    private String gmtCreated;

    public void setGmtCreated(String gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    private String gmtModified;

    public ClassEntity(String className, String classContent) {
        this.className = className;
        this.classContent = classContent;
    }

    @Override
    public String toString() {
        return "ClassEntity{" +
                "id=" + classId +
                ", createId=" + createId +
                ", className='" + className + '\'' +
                ", classContent='" + classContent + '\'' +
                ", gmtCreated='" + gmtCreated + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                '}';
    }

    public long getId() {
        return classId;
    }

    public void setId(long id) {
        this.classId = id;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassContent() {
        return classContent;
    }



}
