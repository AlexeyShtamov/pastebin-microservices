package com.shtamov.pastebin.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * Сущность текста
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Text implements Serializable {

    /** Идентификатор в виде хэшированного url */
    @Id
    private String hashUrl;

    /**  uuid является именем объекта (текста) в object storage */
    @UUID
    private String uuid;

    /** Дата  создания текста*/
    private LocalDate creationDate;

    /** Дата удаления текста текста (для автоматического удаления из БД спустя определенное врем)*/
    private LocalDate expirationDate;

    /** Создатель текста */
    @ManyToOne
    @Cascade(CascadeType.PERSIST)
    private Person maker;

    /** Поле фактического текста
     * (не сохраняется в БД, нужно для отправки ответа клиенту)
     */
    @Transient
    private String text;


}
