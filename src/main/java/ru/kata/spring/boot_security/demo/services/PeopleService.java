package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import java.util.Optional;

@Service
public class PeopleService {

    private final UserRepository userRepository;

    @Autowired
    public PeopleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }





    public Optional<User> loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);

    }


}
