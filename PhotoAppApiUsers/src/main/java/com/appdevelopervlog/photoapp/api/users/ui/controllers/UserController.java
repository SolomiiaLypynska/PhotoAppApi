package com.appdevelopervlog.photoapp.api.users.ui.controllers;

import com.appdevelopervlog.photoapp.api.users.ui.model.CreateUserRequestModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/status/check")
    public String status() {
        return "Working users";
    }

    @PostMapping
    public String createUser(@Valid @RequestBody CreateUserRequestModel userDetails){
        return "Create user";
    }
}
