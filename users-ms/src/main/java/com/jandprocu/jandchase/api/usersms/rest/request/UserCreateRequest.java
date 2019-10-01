package com.jandprocu.jandchase.api.usersms.rest.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserCreateRequest extends UserRequest {

    @NotNull(message="Password is a required field")
    @Size(min=8, max=16, message="Password must be equal to or greater than 8 characters and less than 16 characters")
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
