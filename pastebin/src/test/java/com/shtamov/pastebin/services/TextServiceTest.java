package com.shtamov.pastebin.services;

import com.shtamov.pastebin.application.services.HashUrlService;
import com.shtamov.pastebin.application.services.TextService;
import com.shtamov.pastebin.domain.models.Person;
import com.shtamov.pastebin.domain.models.Text;
import com.shtamov.pastebin.extern.exceptions.TextIsNotCreatedException;
import com.shtamov.pastebin.extern.exceptions.TextIsNotFoundException;
import com.shtamov.pastebin.extern.repositories.TextRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TextServiceTest {

    @Mock
    private TextRepository textRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private HashUrlService hashUrlService;

    @InjectMocks
    private TextService textService;

    private Text text;

    @BeforeEach
    void init(){
        this.text = new Text();
        text.setCreationDate(LocalDate.now());
        text.setHashUrl("qwerty/=");
    }

    @Test
    void TextService_CreateText_ReturnText() throws TextIsNotCreatedException {
        ResponseEntity<String> response = new ResponseEntity<>("6c432014-cbbc-45c2-b074-ec131fcf867a", HttpStatus.OK);

        when(restTemplate.postForEntity(any(String.class), any(Object.class), eq(String.class)))
                .thenReturn(response);
        when(hashUrlService.encode(any(String.class))).thenReturn("qwerty/=");
        given(textRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        Text createdText = textService.createText(new Person(), "blablabla", LocalDate.MAX);


        assertAll(
                () -> assertNotNull(createdText),
                () -> assertEquals(createdText.getExpirationDate(), LocalDate.MAX),
                () -> assertEquals(createdText.getUuid(), "6c432014-cbbc-45c2-b074-ec131fcf867a")
        );
    }

    @Test
    void TextService_GetTextByUrl_ReturnText() throws TextIsNotFoundException {
        ResponseEntity<String> response = new ResponseEntity<>("blablabla", HttpStatus.OK);

        when(textRepository.findById("qwerty/=")).thenReturn(Optional.of(text));
        when(restTemplate.getForEntity(
                any(String.class),
                eq(String.class)
        )).thenReturn(response);

        Text foundText = textService.getTextByUrl("qwerty/=");

        assertAll(
                () -> assertNotNull(foundText),
                () -> assertEquals(foundText.getCreationDate(), LocalDate.now()),
                () -> assertEquals(foundText.getHashUrl(), "qwerty/="),
                () -> assertEquals(foundText.getText(), "blablabla")
        );
    }
}
