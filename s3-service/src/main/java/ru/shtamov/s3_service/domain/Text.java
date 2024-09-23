package ru.shtamov.s3_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

/**
 * Сущность текста (сам текст не хранит, а лишь ссылку на него в object storage)
 */
@Entity
@Data
public class Text implements Serializable{

    /** интендификатор текста */
    @Id
    private String uuid;

    /** метаданные текста (его длина) */
    private Long metadata;

    /** имя бакета в object storage, где хранится текст */
    private String bucketName;

    /** ссылка на текст текста */
    private String url;
}
