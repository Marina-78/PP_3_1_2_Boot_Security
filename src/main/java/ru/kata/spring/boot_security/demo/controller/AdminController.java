package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;


@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private final UserService userService;


    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String allUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin";
    }

    @GetMapping("/createUser")
    public String createUserForm(@ModelAttribute("user") User user) {
        return "/createUser";
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute("user") User user,
                             Model model,
                             @RequestParam("roles") List<String> roles) {
        userService.saveUser(user, roles);
        model.addAttribute(userService.getAllRoles());
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/updateUser/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("user", user);
        return "/updateUser";
    }

    @PatchMapping("/updateUser")
    public String updateUser(User user,
                             @RequestParam("roles") List<String> roles) {
        userService.editUser(user, roles);
        return "redirect:/admin";
    }
}