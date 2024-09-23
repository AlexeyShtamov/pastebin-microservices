package com.shtamov.pastebin.extern.DTOs.personDTO;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonInfoDTO {

    private String username;

    @Email
    private String email;
}
