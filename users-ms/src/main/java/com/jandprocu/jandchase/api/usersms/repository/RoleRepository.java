package com.jandprocu.jandchase.api.usersms.repository;

import com.jandprocu.jandchase.api.usersms.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
