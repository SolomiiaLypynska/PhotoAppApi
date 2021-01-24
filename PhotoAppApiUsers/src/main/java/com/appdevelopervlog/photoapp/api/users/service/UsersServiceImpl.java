package com.appdevelopervlog.photoapp.api.users.service;

import com.appdevelopervlog.photoapp.api.users.data.UserEntity;
import com.appdevelopervlog.photoapp.api.users.data.UserRepository;
import com.appdevelopervlog.photoapp.api.users.shared.UserDto;
import com.appdevelopervlog.photoapp.api.users.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    RestTemplate restTemplate;
    Environment environment;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                            RestTemplate restTemplate, Environment environment) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        userRepository.save(userEntity);
        UserDto responseUserDto = modelMapper.map(userEntity, UserDto.class);
        return responseUserDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByEmail(email));
        if (!userEntity.isPresent()) throw new UsernameNotFoundException("Not found " + userEntity);
        return new User(userEntity.get().getEmail(), userEntity.get().getEncryptedPassword(),
                true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByEmail(email));
        if (!userEntity.isPresent()) throw new UsernameNotFoundException("Not found " + userEntity);
        return new ModelMapper().map(userEntity.get(), UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId) {
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByUserId(userId));
        if (!userEntity.isPresent()) throw new UsernameNotFoundException("User not found");
        UserDto userDto = new ModelMapper().map(userEntity.get(), UserDto.class);
        //String albumsUrl = "http://miyaws:8005/users/7/albums";
        String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
        ResponseEntity<List<AlbumResponseModel>> albumsListResponse =
                restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
                });
        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
        userDto.setAlbumsList(albumsList);
        return userDto;
    }

}
