package ru.shtamov.s3_service.extern.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shtamov.s3_service.application.services.TextService;
import ru.shtamov.s3_service.domain.Text;
import ru.shtamov.s3_service.extern.exceptions.TextIsNotFoundException;

@RestController
@RequestMapping("/v1/texts")
public class TextController {

    @Value("${secret.key}")
    private String SECRET_TRANS_KEY;

    private final TextService textService;

    @Autowired
    public TextController(TextService textService) {
        this.textService = textService;
    }

    @PostMapping
    public ResponseEntity<String> postText(@RequestBody String text, @RequestHeader("SECRET-KEY") String secretKey){
        if (!secretKey.equals(SECRET_TRANS_KEY)) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

        Text createdText = textService.upload(text);
        return new ResponseEntity<>(createdText.getUuid(), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{uuid}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getText(@PathVariable String uuid) throws TextIsNotFoundException {
        String text = textService.get(uuid);
        return new ResponseEntity<>(text, HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<HttpStatus> deleteText(@PathVariable String uuid, @RequestHeader("SECRET-KEY") String secretKey){
        if (!secretKey.equals(SECRET_TRANS_KEY)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        textService.delete(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
