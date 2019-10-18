package com.jandprocu.jandchase.api.usersms.service;

import com.jandprocu.jandchase.api.usersms.exception.*;
import com.jandprocu.jandchase.api.usersms.model.Role;
import com.jandprocu.jandchase.api.usersms.repository.RoleRepository;
import com.jandprocu.jandchase.api.usersms.rest.request.RoleRequest;
import com.jandprocu.jandchase.api.usersms.rest.response.RoleResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roleService")
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private ModelMapper modelMapper;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest createRequest) {
        Role roleEntity = modelMapper.map(createRequest, Role.class);

        try {
            roleRepository.save(roleEntity);
        } catch (DataAccessException exception) {
            throw new RoleNotCreatedException("Role " + roleEntity.getName() + " not created.");
        }

        return modelMapper.map(roleEntity, RoleResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleByRoleName(String roleName) {
        Role roleEntity = getRoleEntityByRoleName(roleName);
        return this.modelMapper.map(roleEntity, RoleResponse.class);
    }


    @Override
    @Transactional
    public RoleResponse updateRoleByRoleName(String roleName, String description) {
        Role roleEntity = getRoleEntityByRoleName(roleName);
        roleEntity.setDescription(description);
        try {
            roleRepository.save(roleEntity);
        } catch (DataAccessException exception) {
            throw new RoleNotUpdatedException("Role " + roleName + " not updated");
        }
        return this.modelMapper.map(roleEntity, RoleResponse.class);
    }

    @Override
    @Transactional
    public RoleResponse deleteRoleByRoleName(String roleName) {
        Role roleEntity = getRoleEntityByRoleName(roleName);
        this.roleRepository.deleteById(roleEntity.getId());
        return this.modelMapper.map(roleEntity, RoleResponse.class);
    }

    private Role getRoleEntityByRoleName(String roleName) {
        Role roleEntity = this.roleRepository.findByName(roleName);
        if (roleEntity == null) throw new UserNotFoundException("Role " + roleName + " not found.");
        return roleEntity;
    }
}
