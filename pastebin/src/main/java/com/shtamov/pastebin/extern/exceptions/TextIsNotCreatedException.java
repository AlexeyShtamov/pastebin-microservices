package com.shtamov.pastebin.extern.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TextIsNotCreatedException extends Exception{

    public TextIsNotCreatedException(String message) {
        super(message);
    }
}
