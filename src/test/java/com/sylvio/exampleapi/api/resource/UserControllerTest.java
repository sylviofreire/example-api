package com.sylvio.exampleapi.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sylvio.exampleapi.api.dto.UserDTO;
import com.sylvio.exampleapi.model.entity.User;
import com.sylvio.exampleapi.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class UserControllerTest {

    static String USER_API = "/api/users";

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService service;

    @Test
    @DisplayName("Should create a user")
    public void createUserTest() throws Exception{

        UserDTO dto = UserDTO.builder().name("Sylvio").gender("Masculino").birthDate("25/08/1994").age(32).build();
        User savedUser = User.builder().id(1l).name("Sylvio").gender("Masculino").birthDate("25/08/1994").age(32).build();
        BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(savedUser);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1l))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(dto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("gender").value(dto.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("birthDate").value(dto.getBirthDate()))
                .andExpect(MockMvcResultMatchers.jsonPath("age").value(dto.getAge()))
            ;
    }

    @Test
    @DisplayName("Should throws validation error when trying create a user with insufficient data")
    public void createInvalidUserTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new UserDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(4)));

    }
    @Test
    @DisplayName("Should get user details")
    public void getUserDetailsTest() throws Exception{
        //Scenario
        Long id = 1l;

        User user = User.builder()
                .id(id)
                .name(createNewUser().getName())
                .age(createNewUser().getAge())
                .birthDate(createNewUser().getBirthDate())
                .gender(createNewUser().getGender())
                .build();

        BDDMockito.given(service.getByID(id)).willReturn(Optional.of(user));

        //Execution
        MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                .get(USER_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);

        //Verification
        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(createNewUser().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("gender").value(createNewUser().getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("birthDate").value(createNewUser().getBirthDate()))
                .andExpect(MockMvcResultMatchers.jsonPath("age").value(createNewUser().getAge()));
    }

    @Test
    @DisplayName("Should returns resource not found when user doesn't exist")
    public void userNotFoundTest() throws Exception{
        //Scenario
        BDDMockito.given(service.getByID(Mockito.anyLong())).willReturn(Optional.empty());

        //Execution
        MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                .get(USER_API.concat("/"+ 1))
                .accept(MediaType.APPLICATION_JSON);

        //Verification
        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DisplayName("Should delete an user")
    public void deleteUserTest() throws Exception {

        //Scenario
        BDDMockito.given(service.getByID(Mockito.anyLong())).willReturn(Optional.of(User.builder().id(1l).build()));

        //Execution
        MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                .delete(USER_API.concat("/"+ 1))
                .accept(MediaType.APPLICATION_JSON);

        //Verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Should return not found when delete an nonexistent user")
    public void deleteNonexistentUserTest() throws Exception {

        //Scenario
        BDDMockito.given(service.getByID(Mockito.anyLong())).willReturn(Optional.empty());

        //Execution
        MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                .delete(USER_API.concat("/"+ 1))
                .accept(MediaType.APPLICATION_JSON);

        //Verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should update an user")
    public void updateUserTest() throws Exception {
        //Scenario
        Long id = 1l;
        String json = new ObjectMapper().writeValueAsString(createNewUser());

        User updatingUser = User.builder().id(1l).name("Camilla").age(27).gender("Feminino").birthDate("19/04/1993").build();
        BDDMockito.given(service.getByID(id)).willReturn(Optional.of(updatingUser));
        User updatedUser = User.builder().id(id).name("Sylvio").age(32).birthDate("25/08/1994").gender("Masculino").build();
        BDDMockito.given(service.update(updatingUser)).willReturn(updatedUser);

        //Execution
        MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                .put(USER_API.concat("/"+ 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        //Verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(createNewUser().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("gender").value(createNewUser().getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("birthDate").value(createNewUser().getBirthDate()))
                .andExpect(MockMvcResultMatchers.jsonPath("age").value(createNewUser().getAge()));

    }

    @Test
    @DisplayName("Should return not found when update an nonexistent user")
    public void updateNonexistentUserTest() throws Exception {
        //Scenario
        String json = new ObjectMapper().writeValueAsString(createNewUser());

        User updatingUser = User.builder().id(1l).name("Camilla").age(27).gender("Feminino").birthDate("19/04/1993").build();
        BDDMockito.given(service.getByID(Mockito.anyLong()))
                .willReturn(Optional.empty());

        //Execution
        MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                .put(USER_API.concat("/"+ 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        //Verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    private UserDTO createNewUser() {
        return UserDTO.builder().name("Sylvio").age(32).birthDate("25/08/1994").gender("Masculino").build();
    }
}
