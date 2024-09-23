package com.shtamov.pastebin.application.services;

import com.shtamov.pastebin.domain.models.Person;
import com.shtamov.pastebin.domain.models.Text;
import com.shtamov.pastebin.extern.exceptions.TextIsNotCreatedException;
import com.shtamov.pastebin.extern.exceptions.TextIsNotFoundException;
import com.shtamov.pastebin.extern.repositories.TextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class TextService {

    @Value("${secret.key}")
    private String SECRET_TRANS_KEY;

    private final TextRepository textRepository;
    private final RestTemplate restTemplate;
    private final HashUrlService hashUrlService;
    private final PersonService personService;

    public TextService(TextRepository textRepository, RestTemplate restTemplate, HashUrlService hashUrlService, PersonService personService) {
        this.textRepository = textRepository;
        this.restTemplate = restTemplate;
        this.hashUrlService = hashUrlService;
        this.personService = personService;
    }

    /**
     * Метод для создания текста
     * @param maker сущность пользователя (создателя текста)
     * @param text сущность текста
     * @param expirationDate срок годности текста
     * @return сущность созданного тектса
     * @throws TextIsNotCreatedException ошибка при неуспешном создании текста
     */
    public Text createText(Person maker, String text, LocalDate expirationDate) throws TextIsNotCreatedException {
        if (text == null) throw new TextIsNotCreatedException("Text couldn't be empty");

        HttpHeaders headers = getHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(text, headers);

        ResponseEntity<String> response = restTemplate.
                    postForEntity("http://localhost:8083/v1/texts", entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()){
            String uuid = response.getBody();

            String hashUrl = hashUrlService.encode(uuid);
            if (expirationDate == null) expirationDate = LocalDate.MAX;

            Text createText = Text.builder()
                    .hashUrl(hashUrl)
                    .maker(maker)
                    .uuid(uuid)
                    .creationDate(LocalDate.now())
                    .expirationDate(expirationDate)
                    .build();
            personService.addText(maker, createText);
            Text createdText = textRepository.save(createText);
            log.info("text with url {} is saved", hashUrl);
            return createdText;
        }
        throw new TextIsNotCreatedException(response.getBody());
    }

    /**
     * Метод получения текста по уникальному хэшу
     * @param hash хэш
     * @return сущность текста
     * @throws TextIsNotFoundException ошибка в случае отсвутсвия текста (может быть просрочен)
     */
    @Cacheable(value = "TextService::getTextByUrl", key = "#hash", cacheManager = "cacheManagerWithTtl")
    public Text getTextByUrl(String hash) throws TextIsNotFoundException {
        Optional<Text> optionalText = textRepository.findById(hash);

        if (optionalText.isPresent()){
            Text text = optionalText.get();
            log.info("Text with url {} is found", text.getHashUrl());

            ResponseEntity<String> response = restTemplate
                    .getForEntity("http://localhost:8083/v1/texts/" + text.getUuid(), String.class);

            if (response.getStatusCode().is2xxSuccessful()){
                log.info("Text is successful got");
                text.setText(response.getBody());
                return text;
            }
            //Ошибка должна появляться, когда срок годности истекает
            throw new TextIsNotFoundException("hash " + hash + " is not available");
        }
        throw new TextIsNotFoundException("uuid for hash " + hash + " is not found");
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("SECRET-KEY", SECRET_TRANS_KEY);
        return headers;
    }

}
