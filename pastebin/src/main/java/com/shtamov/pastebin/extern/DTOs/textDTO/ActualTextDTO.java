package com.shtamov.pastebin.extern.DTOs.textDTO;

import com.shtamov.pastebin.extern.DTOs.personDTO.PersonInfoDTO;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ActualTextDTO {

    String text;

    PersonInfoDTO maker;
}
