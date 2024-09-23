package com.shtamov.pastebin.application.services;

import com.shtamov.pastebin.extern.repositories.TextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class HashUrlService {

    private final TextRepository textRepository;

    private final List<String> HASHES = new ArrayList<>();
    private Integer counter = 0;

    public HashUrlService(TextRepository textRepository) {
        this.textRepository = textRepository;
    }

    /**
     * Метод для возвращения уникального хэша
     * @param uuid идентификатор текста
     * @return хэш
     */
    public String encode(String uuid){
        String hashUrl = HASHES.get(0);
        HASHES.remove(0);
        log.info("Encoding of {} is ready", uuid);
        return hashUrl;
    }

    /**
     * Метод для ассинхронного генерирования уникальных хэшей для формировния ссылок
     */
    @Async
    public void generateUniqueCode(){
        while (true){
            if (HASHES.size() >= 100) continue;
            String hashUrl = Base64.getEncoder()
                    .encodeToString(String.valueOf(textRepository.count() + ++counter).getBytes());

            HASHES.add(hashUrl);
            log.info("Generated hash: {}", hashUrl);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }
}
