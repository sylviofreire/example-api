package com.sylvio.exampleapi.api.resource;

import com.sylvio.exampleapi.api.dto.UserDTO;
import com.sylvio.exampleapi.api.exception.ApiErrors;
import com.sylvio.exampleapi.model.entity.User;
import com.sylvio.exampleapi.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

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
    public UserDTO create(@RequestBody @Valid UserDTO dto){
        User entity = modelMapper.map(dto, User.class);
        entity = service.save(entity);
        return modelMapper.map(entity, UserDTO.class);
    }

    @GetMapping("{id}")
    public UserDTO get(@PathVariable Long id){
        return service
                .getByID(id)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        User user = service.getByID(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(user);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex){
        BindingResult bindingResult =  ex.getBindingResult();

        return new ApiErrors(bindingResult);

    }
}
