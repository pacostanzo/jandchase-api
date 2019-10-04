package com.jandprocu.jandchase.api.usersms.service;

import com.jandprocu.jandchase.api.usersms.exception.UserNotCreatedException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotFoundException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotUpdatedException;
import com.jandprocu.jandchase.api.usersms.model.User;
import com.jandprocu.jandchase.api.usersms.repository.UserRepository;
import com.jandprocu.jandchase.api.usersms.rest.UserRest;
import com.jandprocu.jandchase.api.usersms.rest.response.UserCreateResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetOAuthResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserUpdateResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public UserCreateResponse createUser(UserRest userRequest) {

        User userEntity = modelMapper.map(userRequest, User.class);
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setCreatedAt(new Date());
        userEntity.setEnable(Boolean.TRUE);

        try {
            userRepository.save(userEntity);
        } catch (DataAccessException exception) {
            throw new UserNotCreatedException("User " + userEntity.getUserName() + " not created");
        }

        UserCreateResponse userResponse = modelMapper.map(userEntity, UserCreateResponse.class);

        return userResponse;

    }

    @Override
    public UserGetResponse getUserByUserId(String userId) {
        User userEntity = getUserEntityByUserId(userId);
        UserGetResponse getUserResponse = this.modelMapper.map(userEntity, UserGetResponse.class);
        return getUserResponse;
    }

    @Override
    public UserGetResponse getUserByUserName(String userName) {
        User userEntity = getUserEntityByUserName(userName);
        UserGetResponse getUserResponse = this.modelMapper.map(userEntity, UserGetResponse.class);
        return getUserResponse;
    }

    @Override
    public UserGetOAuthResponse getUserByUserOAuthName(String userName) {
        User userEntity = getUserEntityByUserName(userName);
        UserGetOAuthResponse getUserOAuthResponse = this.modelMapper.map(userEntity, UserGetOAuthResponse.class);
        return getUserOAuthResponse;
    }

    private User getUserEntityByUserName(String userName) {
        User userEntity = this.userRepository.findByUserName(userName);
        if (userEntity == null) throw new UserNotFoundException("User " + userName + " not found");
        return userEntity;
    }

    //TODO: Refactor update Method: Not need to send the full object
    // Only the updated fields
    @Override
    public UserUpdateResponse updateUserByUserId(String userId, UserRest userUpdate) {
        User userEntity = getUserEntityByUserId(userId);
        User updatedUser = this.updateUserEntity(userUpdate, userEntity);
        try {
            userRepository.save(updatedUser);
        } catch (DataAccessException exception) {
            throw new UserNotUpdatedException("User " + userId + " not updated");
        }
        UserUpdateResponse updateResponse = this.modelMapper.map(updatedUser, UserUpdateResponse.class);
        return updateResponse;
    }

    @Override
    public UserGetResponse deleteUserByUserId(String userId) {
        User userEntity = getUserEntityByUserId(userId);
        this.userRepository.deleteById(userEntity.getId());
        UserGetResponse deleteUser = this.modelMapper.map(userEntity, UserGetResponse.class);
        return deleteUser;
    }



    private User updateUserEntity(UserRest userUpdate, User userEntity) {
        userEntity.setFirstName(userUpdate.getFirstName());
        userEntity.setLastName(userUpdate.getLastName());
        userEntity.setEmail(userUpdate.getEmail());
        return userEntity;
    }

    private User getUserEntityByUserId(String userId) {
        User userEntity = this.userRepository.findByUserId(userId);
        if (userEntity == null) throw new UserNotFoundException("User " + userId + " not found");
        return userEntity;
    }
}
