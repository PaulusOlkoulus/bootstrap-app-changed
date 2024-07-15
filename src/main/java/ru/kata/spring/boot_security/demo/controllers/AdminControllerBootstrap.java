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
import java.util.stream.Collectors;


@Controller
@RequestMapping("/main_page")
public class AdminControllerBootstrap {
    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;
    private List<User> usersInDb;

    @Autowired
    public AdminControllerBootstrap(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }


    @GetMapping()
    public String index(@ModelAttribute("user") User user,
                        Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("thisUser", currentUser);

        model.addAttribute("isAdmin",
                currentUser.getRoles().stream()
                        .map(Role::getName).collect(Collectors.toList()).contains("ROLE_ADMIN")
        );
        usersInDb = userService.getUsers();
        model.addAttribute("users", usersInDb);
        model.addAttribute("usersCount", userService
                .getUsers().size());
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("isAddingUser", false);
        model.addAttribute("isUpdatingUser", false);

        return "users";
    }


    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam Map<String, String> params,
                         Model model,
                         Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());

        model.addAttribute("users", usersInDb);
        model.addAttribute("thisUser", currentUser);
        model.addAttribute("isAddingUser", true);
        model.addAttribute("isUpdatingUser", false);
        model.addAttribute("isAdmin",
                currentUser.getRoles().stream()
                        .map(Role::getName).collect(Collectors.toList()).contains("ROLE_ADMIN")
        );
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("isEmpty", false);

        List<Role> userRoles = new ArrayList<>();
        for (Role role : roleService.getRoles()) {
            if (params.containsKey(role.getName())) {
                userRoles.add(role);
            }
        }

        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors() || user.getRoles().isEmpty()) {
            if (user.getRoles().isEmpty()) {
                model.addAttribute("isEmpty", true);
            }
            return "users";
        }

        userService.addUser(user);
        return "redirect:/main_page";
    }


    @GetMapping("/edit")
    public String editUser(Model model,
                           @RequestParam("id") int id) {

        User user = userService.getUserDetail(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("isEmpty", false);
        return "editUser";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam Map<String, String> params, @RequestParam(name = "roleIds", required = false) List<Long> roleIds, Model model, @PathVariable("id") Integer id,
                         Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        List<Role> rolesUp = roleService.getRoles();
        model.addAttribute("roles", rolesUp);
        model.addAttribute("isUpdatingUser", false);

        model.addAttribute("users", usersInDb);
        model.addAttribute("thisUser", currentUser);
        model.addAttribute("isAddingUser", false);
        model.addAttribute("isAdmin",
                currentUser.getRoles().stream()
                        .map(Role::getName).collect(Collectors.toList()).contains("ROLE_ADMIN")
        );
        List<Role> userRoles = new ArrayList<>();
        if (roleIds != null)
            for (Long roleId : roleIds) {
                userRoles.add(roleService.getRoleById(roleId));
            }
        user.setRoles(userRoles);
        user.setPassword(params.get("passwordEd"));
        if (!user.getUsername().equals(userService.getUserDetail(id).getUsername())) {
            userValidator.validate(user, bindingResult);
        }
        if (bindingResult.hasErrors() || user.getRoles().isEmpty()) {
            if (user.getRoles().isEmpty()) {
                model.addAttribute("isEmpty", true);
            }
            String modalUpdate = "#edituser" + user.getId();
            model.addAttribute("isUpdatingUser", true);
            model.addAttribute("modalUpdate", modalUpdate);
            return "users";
        }

        userService.update(user.getId(), user);
        return "redirect:/main_page";
    }


    @PostMapping("/delete/{id}")
    public String delete(@ModelAttribute("user") User user, Principal principal) {
        userService.deleteUser(user);
        return "redirect:/main_page";
    }
}