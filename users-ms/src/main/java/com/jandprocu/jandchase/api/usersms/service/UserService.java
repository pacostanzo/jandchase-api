package com.jandprocu.jandchase.api.usersms.service;

import com.jandprocu.jandchase.api.usersms.exception.RoleNotFoundException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotCreatedException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotFoundException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotUpdatedException;
import com.jandprocu.jandchase.api.usersms.model.Role;
import com.jandprocu.jandchase.api.usersms.model.User;
import com.jandprocu.jandchase.api.usersms.repository.RoleRepository;
import com.jandprocu.jandchase.api.usersms.repository.UserRepository;
import com.jandprocu.jandchase.api.usersms.rest.UserRest;
import com.jandprocu.jandchase.api.usersms.rest.request.UserUpdateRequest;
import com.jandprocu.jandchase.api.usersms.rest.response.UserCreateResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetOAuthResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("userService")
public class UserService implements IUserService {
    private final String ROLE_USER = "ROLE_USER";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @Transactional
    public UserCreateResponse createUser(UserRest userRequest) {
        User userEntity = modelMapper.map(userRequest, User.class);
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setCreatedAt(new Date());
        userEntity.setEnable(Boolean.TRUE);

        addRole(userEntity, ROLE_USER);

        try {
            userRepository.save(userEntity);
        } catch (DataAccessException exception) {
            throw new UserNotCreatedException("User " + userEntity.getUserName() + " not created.");
        }

        UserCreateResponse userResponse = modelMapper.map(userEntity, UserCreateResponse.class);

        return userResponse;

    }

    @Override
    @Transactional(readOnly = true)
    public UserGetResponse getUserByUserId(String userId) {
        User userEntity = getUserEntityByUserId(userId);
        UserGetResponse getUserResponse = this.modelMapper.map(userEntity, UserGetResponse.class);
        return getUserResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public UserGetOAuthResponse getUserByUserName(String userName) {
        User userEntity = getUserEntityByUserName(userName);
        UserGetOAuthResponse getUserResponse = this.modelMapper.map(userEntity, UserGetOAuthResponse.class);
        return getUserResponse;
    }

    @Override
    @Transactional
    public UserGetResponse updateUserByUserId(String userId, UserRest userUpdate) {
        User userEntity = getUserEntityByUserId(userId);
        User updatedUser = this.updateUserEntity((UserUpdateRequest) userUpdate, userEntity);
        try {
            userRepository.save(updatedUser);
        } catch (DataAccessException exception) {
            throw new UserNotUpdatedException("User " + userId + " not updated");
        }
        UserGetResponse updateResponse = this.modelMapper.map(updatedUser, UserGetResponse.class);
        return updateResponse;
    }

    @Override
    @Transactional
    public UserGetResponse deleteUserByUserId(String userId) {
        User userEntity = getUserEntityByUserId(userId);
        this.userRepository.deleteById(userEntity.getId());
        UserGetResponse deleteUser = this.modelMapper.map(userEntity, UserGetResponse.class);
        return deleteUser;
    }

    @Override
    @Transactional
    public UserResponse addRolesByUserId(String userId, List<String> rolesNames) {
        User userEntity = getUserEntityByUserId(userId);
        rolesNames.forEach(role -> addRole(userEntity, role));
        return this.modelMapper.map(userEntity, UserGetResponse.class);
    }

    @Override
    @Transactional
    public UserResponse removeRolesByUserId(String userId, List<String> rolesNames) {
        User userEntity = getUserEntityByUserId(userId);
        rolesNames.forEach(role -> removeRole(userEntity, role));
        return this.modelMapper.map(userEntity, UserGetResponse.class);

    }


    private User getUserEntityByUserName(String userName) {
        User userEntity = this.userRepository.findByUserName(userName);
        if (userEntity == null) throw new UserNotFoundException("User " + userName + " not found.");
        return userEntity;
    }

    private User getUserEntityByUserId(String userId) {
        User userEntity = this.userRepository.findByUserId(userId);
        if (userEntity == null) throw new UserNotFoundException("User " + userId + " not found.");
        return userEntity;
    }

    private void addRole(User userEntity, String roleName) {
        Role role = this.roleRepository.findByName(roleName);

        if (role == null) {
            throw new RoleNotFoundException("Role: " + roleName + " not found.");
        }
        if (userEntity.getRoles().isEmpty() || !userEntity.getRoles().contains(role)) {
            userEntity.getRoles().add(role);
        }
    }

    private void removeRole(User userEntity, String roleName) {
        Role role = this.roleRepository.findByName(roleName);

        if (role == null) {
            throw new RoleNotFoundException("Role: " + roleName + " not found.");
        }
        if (!userEntity.getRoles().isEmpty() || userEntity.getRoles().contains(role)) {
            userEntity.getRoles().remove(role);
        }
    }

    private User updateUserEntity(UserUpdateRequest userUpdate, User userEntity) {
        userEntity.setUserName(userUpdate.getUserName());
        userEntity.setFirstName(userUpdate.getFirstName());
        userEntity.setLastName(userUpdate.getLastName());
        userEntity.setEmail(userUpdate.getEmail());
        userEntity.setEnable(userUpdate.getEnable());
        return userEntity;
    }
}
