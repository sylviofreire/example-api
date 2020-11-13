package com.sylvio.exampleapi.api.resource;

import com.sylvio.exampleapi.api.dto.UserDTO;
import com.sylvio.exampleapi.model.entity.User;
import com.sylvio.exampleapi.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService service;
    private ModelMapper modelMapper;

    public UserController(UserService service, ModelMapper mapper) {
        this.service = service;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody UserDTO dto){
        User entity = modelMapper.map(dto, User.class);
        entity = service.save(entity);
        return modelMapper.map(entity, UserDTO.class);
    }
}
