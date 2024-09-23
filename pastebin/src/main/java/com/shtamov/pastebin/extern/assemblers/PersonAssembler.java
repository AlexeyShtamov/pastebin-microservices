package com.shtamov.pastebin.extern.assemblers;

import com.shtamov.pastebin.domain.models.Person;
import com.shtamov.pastebin.extern.DTOs.personDTO.CreatePersonDTO;
import org.springframework.stereotype.Component;

@Component
public class PersonAssembler {

    public Person convertToPerson(CreatePersonDTO personDTO){
        Person person = new Person();
        person.setEmail(personDTO.getEmail());
        person.setUsername(personDTO.getUsername());
        person.setPassword(personDTO.getPassword());
        return person;
    }

    public CreatePersonDTO convertTOPersonDTO(Person person) {
        CreatePersonDTO personDTO = new CreatePersonDTO();
        personDTO.setEmail(person.getEmail());
        personDTO.setUsername(person.getUsername());
        personDTO.setPassword(person.getPassword());
        return personDTO;
    }
}
