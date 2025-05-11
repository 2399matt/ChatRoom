package com.example.Users_Demo.controller;

import com.example.Users_Demo.entity.CustomUser;
import com.example.Users_Demo.entity.Customer;
import com.example.Users_Demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public RegistrationController(UserService userService
            , JdbcUserDetailsManager jdbcUserDetailsManager, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginForm";
    }

    @GetMapping("/new")
    public String regForm(Model model) {
        model.addAttribute("user", new Customer());
        return "regForm";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("user") Customer customer) {
        CustomUser user = new CustomUser(customer.getUsername());
        userService.saveUser(user);
        UserDetails secUser = User.builder()
                .username(customer.getUsername())
                .password(bCryptPasswordEncoder.encode(customer.getPassword()))
                .roles("USER")
                .build();
        jdbcUserDetailsManager.createUser(secUser);
        return "loginForm";
    }
}
