package com.sylvio.exampleapi.service;

import com.sylvio.exampleapi.model.entity.User;
import com.sylvio.exampleapi.model.repository.UserRepository;
import com.sylvio.exampleapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    UserService service;

    @MockBean
    UserRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new UserServiceImpl( repository );
    }

    @Test
    @DisplayName("Should save a user")
    public void saveUserTest(){
        //Scenario
        User user = User.builder().name("Sylvio").age(32).birthDate("25/08/2020").gender("Masculino").build();
        Mockito.when(repository.save(user))
                .thenReturn(
                        User.builder().id(1l)
                                .name("Sylvio")
                                .age(32)
                                .birthDate("25/08/2020")
                                .gender("Masculino")
                                .build());
        //Execution
        User savedUser = service.save(user);

        //Verifications
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Sylvio");
        assertThat(savedUser.getAge()).isEqualTo(32);
        assertThat(savedUser.getBirthDate()).isEqualTo("25/08/2020");
        assertThat(savedUser.getGender()).isEqualTo("Masculino");

    }

    @Test
    @DisplayName("Should get an user")
    public void getUserById(){
        //Scenario
        Long id = 1l;
        User user = User.builder().id(id).name("Sylvio").gender("Masculino").birthDate("25/08/1994").age(32).build();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(user));

        //Execution
        Optional<User> foundUser = service.getByID(id);

        //Verifications
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getId()).isEqualTo(id);
        assertThat(foundUser.get().getName()).isEqualTo(user.getName());
        assertThat(foundUser.get().getGender()).isEqualTo(user.getGender());
        assertThat(foundUser.get().getAge()).isEqualTo(user.getAge());
        assertThat(foundUser.get().getBirthDate()).isEqualTo(user.getBirthDate());

    }

    @Test
    @DisplayName("Should return empty when id doesn't exist")
    public void userNotFoundById(){
        //Scenario
        Long id = 1l;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //Execution
        Optional<User> user = service.getByID(id);

        //Verifications
        assertThat(user.isPresent()).isFalse();

    }



}
