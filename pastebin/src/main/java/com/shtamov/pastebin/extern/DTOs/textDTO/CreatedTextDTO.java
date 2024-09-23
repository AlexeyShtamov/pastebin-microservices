package com.shtamov.pastebin.extern.DTOs.textDTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreatedTextDTO {

    private String hashUrl;

    private LocalDate creationDate;

    private String makerName;
}
