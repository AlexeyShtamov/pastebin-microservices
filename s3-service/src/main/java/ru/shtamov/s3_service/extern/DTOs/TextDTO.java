package ru.shtamov.s3_service.extern.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TextDTO {

    private String text;

}