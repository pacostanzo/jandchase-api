package com.jandprocu.jandchase.api.usersms.rest.response;

import java.util.List;

public class UserGetOAuthResponse extends UserGetResponse {
    private String password;
    private List<RoleResponse> roles;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RoleResponse> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleResponse> roles) {
        this.roles = roles;
    }
}
