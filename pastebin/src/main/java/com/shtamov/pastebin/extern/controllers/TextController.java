package com.shtamov.pastebin.extern.controllers;

import com.shtamov.pastebin.application.services.PersonService;
import com.shtamov.pastebin.application.services.TextService;
import com.shtamov.pastebin.domain.models.Person;
import com.shtamov.pastebin.domain.models.Text;
import com.shtamov.pastebin.extern.DTOs.personDTO.PersonInfoDTO;
import com.shtamov.pastebin.extern.DTOs.textDTO.CreateTextDTO;
import com.shtamov.pastebin.extern.DTOs.textDTO.CreatedTextDTO;
import com.shtamov.pastebin.extern.DTOs.textDTO.ActualTextDTO;
import com.shtamov.pastebin.extern.exceptions.ResourceNotFoundException;
import com.shtamov.pastebin.extern.exceptions.TextIsNotCreatedException;
import com.shtamov.pastebin.extern.exceptions.TextIsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/texts")
public class TextController {

    @Value("${url.prefix}")
    private String prefix;

    private final TextService textService;
    private final PersonService personService;

    @Autowired
    public TextController(TextService textService, PersonService personService) {
        this.textService = textService;
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<CreatedTextDTO> postText(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody CreateTextDTO createTextDTO) throws TextIsNotCreatedException, ResourceNotFoundException {
        Person person = personService.findByUsername(userDetails.getUsername());
        Text text = textService.createText(person, createTextDTO.getText(), createTextDTO.getExpirationDate());

        CreatedTextDTO createdTextDTO = CreatedTextDTO
                .builder().creationDate(text.getCreationDate()).hashUrl(prefix + text.getHashUrl()).makerName(person.getUsername()).build();
        return new ResponseEntity<>(createdTextDTO, HttpStatus.OK);
    }

    @GetMapping("/{hash}")
    public ResponseEntity<ActualTextDTO> getText(@PathVariable String hash) throws TextIsNotFoundException {
        Text text = textService.getTextByUrl(hash);
        ActualTextDTO textDTO = ActualTextDTO.builder()
                .text(text.getText())
                .maker(PersonInfoDTO.builder().email(text.getMaker().getEmail()).username(text.getMaker().getUsername()).build())
                .build();
        return new ResponseEntity<>(textDTO, HttpStatus.OK);
    }

}
