package com.sylvio.exampleapi.model.repository;

import com.sylvio.exampleapi.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository repository;

    @Test
    @DisplayName("Should get user by id")
    public void findByIdTest(){
        //Scenario
        User user = User.builder().name("Sylvio").gender("Masculino").birthDate("25/08/1994").age(32).build();
        entityManager.persist(user);

        //Execution
        Optional<User> foundUser =  repository.findById(user.getId());

        //Verifications
        assertThat(foundUser.isPresent()).isTrue();

    }

}
