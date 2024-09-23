package com.shtamov.pastebin.extern.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TextIsNotFoundException extends Exception{

    public TextIsNotFoundException(String message) {
        super(message);
    }
}
