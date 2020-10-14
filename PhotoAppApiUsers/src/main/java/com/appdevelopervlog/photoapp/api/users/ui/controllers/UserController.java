package com.appdevelopervlog.photoapp.api.users.ui.controllers;

import com.appdevelopervlog.photoapp.api.users.service.UsersService;
import com.appdevelopervlog.photoapp.api.users.shared.UserDto;
import com.appdevelopervlog.photoapp.api.users.ui.model.CreateUserRequestModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/status/check")
    public String status() {
        return "Working users";
    }

    @PostMapping
    public String createUser(@Valid @RequestBody CreateUserRequestModel userDetails){
        System.out.println("la " + userDetails.toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        usersService.createUser(userDto);
        return "Create user";
    }
}
