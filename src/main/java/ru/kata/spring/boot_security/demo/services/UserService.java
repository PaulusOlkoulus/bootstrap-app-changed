package ru.kata.spring.boot_security.demo.services;




import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserService {

    List<User> getUsers();
    User getUserDetail(long id);
    void addUser(User user);

    void update(long id, User user);

    void deleteUser(User user);

    User findByUsername(String username);
}
