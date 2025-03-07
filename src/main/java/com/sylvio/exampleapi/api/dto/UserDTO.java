package com.sylvio.exampleapi.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String birthDate;

    @NotNull
    private Integer age;


}

