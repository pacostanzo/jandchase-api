package com.jandprocu.jandchase.api.usersms.service;

import com.jandprocu.jandchase.api.usersms.rest.request.RoleRequest;
import com.jandprocu.jandchase.api.usersms.rest.response.RoleResponse;

public interface IRoleService {
    RoleResponse createRole(RoleRequest createRequest);

    RoleResponse getRoleByRoleName(String roleName);

    RoleResponse updateRoleByRoleName(String roleName, String description);

    RoleResponse deleteRoleByRoleName(String roleName);
}
