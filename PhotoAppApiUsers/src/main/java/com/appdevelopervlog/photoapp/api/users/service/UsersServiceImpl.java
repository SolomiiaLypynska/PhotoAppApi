package com.appdevelopervlog.photoapp.api.users.service;

import com.appdevelopervlog.photoapp.api.users.data.UserEntity;
import com.appdevelopervlog.photoapp.api.users.data.UserRepository;
import com.appdevelopervlog.photoapp.api.users.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    UserRepository userRepository;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        userEntity.setEncryptedPassword("test");
        userRepository.save(userEntity);
        UserDto responseUserDto = modelMapper.map(userEntity, UserDto.class);
        return responseUserDto;
    }
}
