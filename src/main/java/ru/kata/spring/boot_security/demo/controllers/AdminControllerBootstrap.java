package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;


import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/main_page")
public class AdminControllerBootstrap {
    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    @Autowired
    public AdminControllerBootstrap(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }


    @GetMapping()
    public String index(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("thisUser", currentUser);
        model.addAttribute("user", new User());
        model.addAttribute("isAdmin",
                currentUser.getRoles().stream()
                        .map(Role::getName).collect(Collectors.toList()).contains("ROLE_ADMIN")
        );
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("usersCount", userService
                .getUsers().size());
        return "users";
    }

    @GetMapping("/{id}")
    public String getUserDetails(@ModelAttribute("user") User user,
                                 Model model) {
        model.addAttribute("user", userService.getUserDetail(user.getId()));
        return "user";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user,
                          Model model) {
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("isEmpty", false);
        return "newUser";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam Map<String, String> params, Model model) {
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("isEmpty", false);
        userValidator.validate(user, bindingResult);
        List<Role> userRoles = new ArrayList<>();
        for (Role role : roleService.getRoles()) {
            if (params.containsKey(role.getName())) {
                userRoles.add(role);
            }
        }
        if (bindingResult.hasErrors() || userRoles.isEmpty()) {
            if (userRoles.isEmpty()) {
                model.addAttribute("isEmpty", true);
            }
            return "newUser";
        }
        user.setRoles(userRoles);
        userService.addUser(user);
        return "redirect:/admin/users";
    }


    @GetMapping("/edit")
    public String editUser(Model model,
                           @RequestParam("id") int id) {

        User user = userService.getUserDetail(id);
        //userRolesBeforeUpdating = user.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("isEmpty", false);
        return "editUser";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam Map<String, String> params, Model model, @PathVariable("id") Integer id) {
        model.addAttribute("roles", roleService.getRoles());

        List<Role> userRoles = new ArrayList<>();
        for (Role role : roleService.getRoles()) {
            if (params.containsKey(role.getName())) {
                userRoles.add(role);
            }
        }
        user.setRoles(userRoles);
        if (!user.getUsername().equals(userService.getUserDetail(id).getUsername())) {
            userValidator.validate(user, bindingResult);
        }
        if (bindingResult.hasErrors() || userRoles.isEmpty()) {
            if (userRoles.isEmpty()) {
                model.addAttribute("isEmpty", true);
            }
            return "editUser";
        }
        user.setRoles(userRoles);
        userService.update(user.getId(), user);
        return "redirect:/admin/users";
    }


    @PostMapping("/delete/{id}")
    public String delete(@ModelAttribute("user") User user, Principal principal) {
        userService.deleteUser(user);
        return "redirect:/admin/users";
    }
}