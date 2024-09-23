package com.shtamov.pastebin.extern.DTOs.personDTO;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class CreatePersonDTO {

    private String username;

    private String password;

    @Email
    private String email;
}
