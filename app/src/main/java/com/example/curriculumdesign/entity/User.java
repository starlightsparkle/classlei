package com.example.curriculumdesign.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {
    private Long userId;

    /**
     * 用户名
     */
    private String username;



    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 角色 0为管理员 1为学生 2为老师
     */
    private Long roleId;



    public User(Long userId, String username, String password, String avatarUrl, String phone, Long roleId, LocalDateTime gmtCreated, LocalDateTime gmtModified) {
        this.userId = userId;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.phone = phone;
        this.roleId = roleId;

    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }


}
