package com.appdevelopervlog.photoapp.api.users.service;

import com.appdevelopervlog.photoapp.api.users.shared.UserDto;

import java.util.UUID;

public class UsersServiceImpl implements UsersService {
    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        return null;
    }
}
