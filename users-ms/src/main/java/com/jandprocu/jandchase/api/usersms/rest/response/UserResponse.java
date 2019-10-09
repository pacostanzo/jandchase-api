package com.jandprocu.jandchase.api.usersms.rest.response;

import com.jandprocu.jandchase.api.usersms.rest.UserRest;

import java.util.Date;
import java.util.List;

public abstract class UserResponse extends UserRest {

    private String userId;
    private List<RoleResponse> roles;
    private Boolean enable;
    private Date createdAt;

    public List<RoleResponse> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleResponse> roles) {
        this.roles = roles;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
