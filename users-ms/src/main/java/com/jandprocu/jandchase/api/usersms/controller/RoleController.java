package com.jandprocu.jandchase.api.usersms.controller;

import com.jandprocu.jandchase.api.usersms.rest.request.RoleRequest;
import com.jandprocu.jandchase.api.usersms.rest.response.RoleResponse;
import com.jandprocu.jandchase.api.usersms.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
public class RoleController {

    @Autowired
    @Qualifier("roleService")
    private IRoleService roleService;

    @PostMapping(path = "/roles/",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest createRequest) {
        RoleResponse createdRole = roleService.createRole(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }


    @GetMapping(path = "/roles/{roleName}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<RoleResponse> getRole(@PathVariable String roleName) {
        RoleResponse roleResponse = roleService.getRoleByRoleName(roleName);
        return ResponseEntity.status(HttpStatus.OK).body(roleResponse);
    }

    @PutMapping(path = "/roles/{roleName}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<RoleResponse> updateRole(@PathVariable String roleName,
                                                   @RequestBody String description) {
        RoleResponse updateResponse = roleService.updateRoleByRoleName(roleName, description);
        return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
    }

    @DeleteMapping(path = "/roles/{roleName}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<RoleResponse> deleteRole(@PathVariable String roleName) {
        RoleResponse deleteRole = roleService.deleteRoleByRoleName(roleName);
        return ResponseEntity.status(HttpStatus.OK).body(deleteRole);
    }
}
