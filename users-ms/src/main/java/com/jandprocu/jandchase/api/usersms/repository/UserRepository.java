package com.jandprocu.jandchase.api.usersms.repository;

import com.jandprocu.jandchase.api.usersms.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUserId(String userId);
}
