package com.shtamov.pastebin.application.runners;

import com.shtamov.pastebin.application.services.ExpiringTextService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ExpiringTextRunner implements CommandLineRunner {

    private final ExpiringTextService expiringTextService;

    public ExpiringTextRunner(ExpiringTextService expiringTextService) {
        this.expiringTextService = expiringTextService;
    }

    @Override
    public void run(String... args) {
        expiringTextService.deleteExpiringTexts();
    }
}
