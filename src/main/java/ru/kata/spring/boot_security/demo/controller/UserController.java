package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;


import javax.validation.Valid;


@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public String index(Model model, @RequestParam(name = "id", required = false) Integer id) {
        if (id != null) {
            model.addAttribute("users", userService.getUserDetail(id));
            model.addAttribute("check", false);
            model.addAttribute("isDetail", true);
            model.addAttribute("usersCount", 1);
        } else {
            model.addAttribute("usersCount", userService.getUsers().size());
            model.addAttribute("check", true);
            model.addAttribute("users", userService.getUsers());
            model.addAttribute("isDetail", false);
        }
        return "users";
    }

    @GetMapping("/{id}")
    public String getUserDetails(@ModelAttribute("user") User user,
                                 Model model) {
        model.addAttribute("user", userService.getUserDetail(user.getId()));
        return "user";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "newUser";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "newUser";
        }
        userService.addUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String editUser(Model model,
                           @RequestParam("id") int id) {
        model.addAttribute("user", userService.getUserDetail(id));
        return "editUser";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "editUser";
        }
        userService.update(user.getId(), user);
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@ModelAttribute("user") User user) {
        userService.deleteUser(user);
        return "redirect:/users";
    }


}