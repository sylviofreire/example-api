package com.sylvio.exampleapi.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sylvio.exampleapi.api.dto.UserDTO;
import com.sylvio.exampleapi.model.entity.User;
import com.sylvio.exampleapi.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
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
    public void createInvalidUserTest(){

    }
}
