package com.appdevelopervlog.photoapp.api.users.service;

import com.appdevelopervlog.photoapp.api.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {

    UserDto createUser(UserDto userDetails);
}
