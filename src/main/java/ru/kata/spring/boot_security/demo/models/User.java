package ru.kata.spring.boot_security.demo.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NotEmpty(message = "Введите имя!")
    @Size(min = 2, max = 255, message = "Имя должно быть в диапазоне от 2 до 255")
    @Column(name = "username")
    private String username;

    @Size(min=4, message="Пароль должен иметь как минимум 4 символа")
    @Column(name = "password")
    private String password;

    @NotEmpty(message = "Заполните поле email!")
    @Email(message = "Неверный формат email")
    @Column(name = "email")
    private String email;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    public User(){}

    public User(String username, String password, String email) {
        this.username = username;//maybe here will be a mistake because of not using @data annotation
        this.password = password;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
