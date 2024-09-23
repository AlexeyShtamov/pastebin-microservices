package com.shtamov.pastebin.services;

import com.shtamov.pastebin.application.services.PersonService;
import com.shtamov.pastebin.domain.models.Person;
import com.shtamov.pastebin.domain.models.Text;
import com.shtamov.pastebin.extern.exceptions.IncorrectDataException;
import com.shtamov.pastebin.extern.exceptions.ResourceNotFoundException;
import com.shtamov.pastebin.extern.repositories.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonService personService;

    private Person person;


    @BeforeEach
    void init(){
        this.person = new Person();
        person.setId(1L);
        person.setUsername("Lepexa");
        person.setEmail("shamov0@mail.ru");
        person.setPassword("Shamov123!!");
    }

    @Test
    void PersonService_Post_ReturnPerson() throws IncorrectDataException {

        when(personRepository.save(Mockito.any(Person.class))).thenReturn(person);
        when(passwordEncoder.encode(any())).thenReturn("Shamov123!!");

        Person createPerson = personService.post(person);

        assertAll(
                () -> assertNotNull(createPerson),
                () -> assertEquals(createPerson.getUsername(), person.getUsername())
        );
    }

    @Test
    void PersonService_FindById_ReturnPerson() throws ResourceNotFoundException {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        Person foundPerson = personService.findById(1L);

        assertAll(
                () -> assertNotNull(foundPerson),
                () -> assertEquals(foundPerson.getUsername(), person.getUsername()),
                () -> assertEquals(foundPerson.getId(), 1L)
        );
    }

    @Test
    void PersonService_FindByUsername_ReturnPerson() throws ResourceNotFoundException {
        when(personRepository.findByUsername(any(String.class))).thenReturn(Optional.of(person));

        Person foundPerson = personService.findByUsername(person.getUsername());

        assertAll(
                () -> assertNotNull(foundPerson),
                () -> assertEquals(foundPerson.getUsername(), person.getUsername()),
                () -> assertEquals(foundPerson.getId(), 1L)
        );
    }

    @Test
    void PersonService_AddText_Void(){
        Text text = new Text();
        text.setHashUrl("fgadsfh=/");
        text.setUuid("1511e2fa-7220-4c4a-8fc0-ca593725487f");
        text.setText("blablabla");

        when(personRepository.save(any())).thenReturn(person);

        personService.addText(person, text);

        Assertions.assertEquals(person.getTextList().get(0).getText(), text.getText());
    }

    @Test
    void PersonService_loadUserByUsername_UserDetails()  {
        when(personRepository.findByUsername(any(String.class))).thenReturn(Optional.of(person));

        UserDetails foundPerson = personService.loadUserByUsername(person.getUsername());

        assertAll(
                () -> assertNotNull(foundPerson),
                () -> assertEquals(foundPerson.getUsername(), person.getUsername())
        );
    }
}
