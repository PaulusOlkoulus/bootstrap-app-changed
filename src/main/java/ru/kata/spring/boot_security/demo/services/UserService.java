package ru.kata.spring.boot_security.demo.services;




import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();
    User getUserDetail(int id);
    void addUser(User user);

    void update(int id, User user);

    void deleteUser(User user);
}
