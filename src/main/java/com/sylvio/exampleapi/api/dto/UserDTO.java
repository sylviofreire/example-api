package com.sylvio.exampleapi.api.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String gender;
    private String birthDate;
    private Integer age;


}

