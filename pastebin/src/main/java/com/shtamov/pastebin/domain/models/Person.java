package com.shtamov.pastebin.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Сущность пользователя
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person implements UserDetails, Serializable {

    /** Идентификатор */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Никнейм */
    private String username;

    /** Пароль */
    private String password;

    /** Электронная почта */
    @Email
    private String email;

    /**
     * Список текстов пользователя
     * @see Text
     * */
    @OneToMany(mappedBy = "maker", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    private List<Text> textList;

    public List<Text> getTextList() {
        if (textList == null) {
            textList = new ArrayList<>();  // Инициализация при первом вызове
        }
        return textList;
    }

    /** Методы для настройки авторизации */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
