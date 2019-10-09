package com.jandprocu.jandchase.api.usersms.repository;

import com.jandprocu.jandchase.api.usersms.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void getByUserId_OK_ReturnsUserDetails() {

        User toStoreUser = new User();
        toStoreUser.setFirstName("SixthUser");
        toStoreUser.setUserId(String.valueOf(UUID.randomUUID()));
        toStoreUser.setLastName("SixthUser");
        toStoreUser.setEmail("sixth_user@email.test");
        toStoreUser.setUserName("SIXTH_USER");
        toStoreUser.setPassword("12345678");
        toStoreUser.setCreatedAt(new Date());
        toStoreUser.setEnable(Boolean.TRUE);

        User savedUser = entityManager.persistAndFlush(toStoreUser);
        User user = repository.findByUserId(toStoreUser.getUserId());

        assertThat(user.getFirstName()).isEqualTo(savedUser.getFirstName());
    }

    @Test
    public void getByUserName_OK_ReturnsUserDetails() {

        User toStoreUser = new User();
        toStoreUser.setFirstName("SeventhUser");
        toStoreUser.setUserId(String.valueOf(UUID.randomUUID()));
        toStoreUser.setLastName("SeventhUser");
        toStoreUser.setEmail("seventh_user@email.test");
        toStoreUser.setUserName("SEVENTH_USER");
        toStoreUser.setPassword("12345678");
        toStoreUser.setCreatedAt(new Date());
        toStoreUser.setEnable(Boolean.TRUE);

        User savedUser = entityManager.persistAndFlush(toStoreUser);
        User user = repository.findByUserName(toStoreUser.getUserName());

        assertThat(user.getFirstName()).isEqualTo(savedUser.getFirstName());
    }
}

