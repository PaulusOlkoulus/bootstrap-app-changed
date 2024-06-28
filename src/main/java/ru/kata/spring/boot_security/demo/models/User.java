package ru.kata.spring.boot_security.demo.models;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Введите имя!")
    @Size(min = 2, max = 40, message = "Имя должно быть в диапазоне от 2 до 40")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "Введите фамилию!")
    @Size(min = 2, max = 40, message = "Фамилия должна быть в диапазоне от 2 до 40")
    @Column(name = "name")
    private String name;

    @Min(value = 0,message = "Возраст не может быть отрицательным!")
    @Column(name = "age")
    private int age;

    @NotEmpty(message = "Заполните поле email!")
    @Email(message = "Неверный формат email")
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private List<Role> role;

    public User() {
    }

    public User(String username, String name, int age, String email) {

        this.username = username;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSurName() {
        return name;
    }

    public void setSurName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }
}
