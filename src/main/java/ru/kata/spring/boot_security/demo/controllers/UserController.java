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

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private List<Role> roles;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping()
    public String index(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        System.out.println();
        List<String> namesOfRole = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        model.addAttribute("admin", namesOfRole.contains("ROLE_ADMIN"));
        return "user";
    }

    @GetMapping("/edit")
    public String editUser(Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
//        roles = user.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("isEmpty", false);
        return "editUser";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam Map<String, String> params,
                         Model model) {

        List<Role> userRoles = new ArrayList<>();
        for (Role role : roleService.getRoles()) {
            if (params.containsKey(role.getName())) {
                userRoles.add(role);
            }
        }
        if (bindingResult.hasErrors() || userRoles.isEmpty()) {
            if (userRoles.isEmpty()) {
                model.addAttribute("isEmpty", true);
                model.addAttribute("roles", roleService.getRoles());
                model.addAttribute("user", user);
            }
            return "editUser";
        }
        user.setRoles(userRoles);
        userService.update(user.getId(), user);
        return "redirect:/user";
    }


    @PostMapping("/delete/{id}")
    public String delete(@ModelAttribute("user") User user) {
        userService.deleteUser(user);
        return "redirect:/logout";
    }


}