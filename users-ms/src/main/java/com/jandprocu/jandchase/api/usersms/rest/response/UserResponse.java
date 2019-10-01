package com.jandprocu.jandchase.api.usersms.rest.response;

import com.jandprocu.jandchase.api.usersms.rest.UserRest;

public abstract class UserResponse extends UserRest {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
