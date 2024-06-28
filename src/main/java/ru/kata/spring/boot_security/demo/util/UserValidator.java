package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.kata.spring.boot_security.demo.models.User;

import ru.kata.spring.boot_security.demo.services.RegistrationService;


@Component
public class UserValidator implements Validator {
    private final RegistrationService registrationService;

    @Autowired
    public UserValidator(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (registrationService.loadUserByUsername(
                ((User) target).getUsername()).isPresent()
        ) {
            errors.rejectValue("username","", "Such a person exists!");
        }

    }
}
