package com.shtamov.pastebin.application.services;

import com.shtamov.pastebin.domain.models.Person;
import com.shtamov.pastebin.domain.models.Text;
import com.shtamov.pastebin.extern.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class ExpiringTextService {

    /** Один день в миллисекундах */
    private final long ONE_DAY = 86400000;

    private final PersonRepository personRepository;
    private final RestTemplate restTemplate;

    public ExpiringTextService(PersonRepository personRepository, RestTemplate restTemplate) {
        this.personRepository = personRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Метод для ассинхронного удлания текстов с истекшим сроком годности
     */
    @Async
    public void deleteExpiringTexts(){
        List<Person> personList = personRepository.findAll();

        for (Person person : personList){
            for (Text text: person.getTextList()){
                if (text.getExpirationDate().isBefore(LocalDate.now())){
                    person.getTextList().remove(text);
                    personRepository.save(person);
                    restTemplate.delete("http://localhost:8080/text/" + text.getUuid());
                    log.info("Text with hash {} is expired", text.getHashUrl());
                }
            }
        }

        try {
            Thread.sleep(ONE_DAY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
