package com.shtamov.pastebin.extern.controllers;

import com.shtamov.pastebin.application.services.PersonService;
import com.shtamov.pastebin.domain.models.Person;
import com.shtamov.pastebin.extern.DTOs.personDTO.CreatePersonDTO;
import com.shtamov.pastebin.extern.assemblers.PersonAssembler;
import com.shtamov.pastebin.extern.exceptions.IncorrectDataException;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/people")
public class PersonController {

    private final PersonService personService;
    private final PersonAssembler personAssembler;

    public PersonController(PersonService personService, @Lazy PersonAssembler personAssembler) {
        this.personService = personService;
        this.personAssembler = personAssembler;
    }

    @PostMapping
    public ResponseEntity<CreatePersonDTO> postPerson(@RequestBody CreatePersonDTO personDTO) throws IncorrectDataException {
        Person person = personAssembler.convertToPerson(personDTO);
        Person createdPerson = personService.post(person);
        CreatePersonDTO createdPersonDTO = personAssembler.convertTOPersonDTO(createdPerson);

        return new ResponseEntity<>(createdPersonDTO, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() throws IncorrectDataException {
        return "hello world";
    }
}
