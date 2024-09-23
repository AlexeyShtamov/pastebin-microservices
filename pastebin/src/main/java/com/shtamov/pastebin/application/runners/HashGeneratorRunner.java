package com.shtamov.pastebin.application.runners;

import com.shtamov.pastebin.application.services.HashUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HashGeneratorRunner implements CommandLineRunner {
    private final HashUrlService hashUrlService;

    @Autowired
    public HashGeneratorRunner(HashUrlService hashUrlService) {
        this.hashUrlService = hashUrlService;
    }

    @Override
    public void run(String... args) {
        hashUrlService.generateUniqueCode(); // Запуск генерации в асинхронном режиме
    }
}
