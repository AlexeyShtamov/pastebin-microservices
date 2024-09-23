package com.shtamov.pastebin.application.services;

import com.shtamov.pastebin.domain.models.Person;
import com.shtamov.pastebin.domain.models.Text;
import com.shtamov.pastebin.extern.exceptions.IncorrectDataException;
import com.shtamov.pastebin.extern.exceptions.ResourceNotFoundException;
import com.shtamov.pastebin.extern.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Метод для добавления нового пользователя
     * @param person сущность пользователя, которого нужно создать
     * @return сущность созданного пользователя
     * @throws IncorrectDataException ошибка в случае неккоректных данных
     */
    public Person post(Person person) throws IncorrectDataException {

        Optional<Person> existedPerson = personRepository.findByUsername(person.getUsername());
        if (existedPerson.isPresent())
            throw new IncorrectDataException("User with username " + person.getUsername() +" already exist");

        person.setPassword(passwordEncoder.encode(person.getPassword()));
        Person savedPerson = personRepository.save(person);

        log.info("Person was saved");
        return savedPerson;
    }

    /**
     * Метод для поиска пользователя по id
     * @param id итендификатор пользователя
     * @return пользователя по итендификатору
     * @throws ResourceNotFoundException ошибка в случае отсутсвия пользователя по данному id
     */
    @Cacheable(value = "PersonService::findById", key = "#id")
    public Person findById(Long id) throws ResourceNotFoundException {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        log.info("Person with id {} found", id);
        return person;
    }

    /**
     * Метод для поиска пользователя по username
     * @param username имя пользователя
     * @return пользователя по имени
     * @throws ResourceNotFoundException ошибка в случае отсутсвия пользователя по данному username
     */
    @Cacheable(value = "PersonService::findByUsername", key = "#username")
    public Person findByUsername(String username) throws ResourceNotFoundException {
        Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));
        log.info("Person with username {} found", username);
return person;
    }

    /**
     * Метод для добавления текста пользователю
     * @param person сущность пользователя
     * @param text сущность текста
     */
    public void addText(Person person, Text text){
        person.getTextList().add(text);
        personRepository.save(person);
        log.info("Text: {} is saved for person {}", text.getHashUrl(), person.getUsername());
    }

    /**
     * Метод для поиска пользователя по username (используется для настройки Spring security)
     * @param username имя пользователя
     * @return userDetails (security core)
     * @throws UsernameNotFoundException ошибка в случае отсутсвия пользователя по данному username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);
        return person.orElse(null);
    }


}
