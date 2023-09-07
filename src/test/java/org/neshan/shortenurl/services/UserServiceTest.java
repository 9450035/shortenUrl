package org.neshan.shortenurl.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.neshan.shortenurl.dto.UserDTO;
import org.neshan.shortenurl.dto.UserOutDTO;
import org.neshan.shortenurl.entities.User;
import org.neshan.shortenurl.mappers.UserMapper;
import org.neshan.shortenurl.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final static String USERNAME = "admin";
    private final static String PASSWORD = "admin";
    private final static String ENCODE_PASSWORD = "$2a$10$FrHteVtpnj74WCRAuFbCn.ZUUljtsUncDBmoFwi88Xam.XVRQA4BW";
    private final static String token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzdHJpbmciLCJzY29wZSI6InVzZXIiLCJpc3MiOiJzZWxmIiwiZXhwIjoxNzI1NTcxMDIxLCJpYXQiOjE2OTQwMzUwMjEsInVzZXJJZCI6MSwiZW5hYmxlZCI6dHJ1ZX0.OAzrZqBRDVqHQdYKXbWxc0_tqccFULpQ5FpLq8V2i1dtUkFGTL6bPWSLtWd2vjk_rjNWNrwqt01FiIX67qm8YOcmyddd5hOj-IeYk_pDz3d314ORGBowjqcHSsoZ9RiWm0w1bG53JvsfByA6d0HOtin46dBBLbnkhfshY7RqyB-d4z8GPphYSjP1oUrL34ZmARD9CtuPY8J0gFP0K6Vz_ZwNDSUzoqPebaXPMV_H1wH9kKKB-Fzsnirrptvvd-kbbonjy-SrGdzUSuc_L6o0fBGla81Zl1-UqZ2tx4gSCxXYtBjQ10f5PqX9DU3AVFSKEw1hjqZJOdPnDW7QBDqYeA";

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @Mock
    private Authentication authentication;
    @Mock
    private ProviderManager providerManager;

    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService(userRepository, passwordEncoder, userMapper, tokenService, authenticationManagerBuilder);
    }

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;


    @Test
    public void register() {
        var dummyUser = new UserDTO(USERNAME, PASSWORD);
        var user = new User(null, USERNAME, ENCODE_PASSWORD, 10);
        var savedUser = new User(1L, USERNAME, ENCODE_PASSWORD, 10);
        var userDTO = new UserOutDTO(1L, null, null, null);
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODE_PASSWORD);
        Mockito.when(userMapper.toEntity(USERNAME, ENCODE_PASSWORD)).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(savedUser);
        Mockito.when(userMapper.toDTO(savedUser.getId())).thenReturn(userDTO);

        Assertions.assertEquals(userDTO, userService.register(dummyUser));

    }

    @Test
    public void login() {
        var dummyUser = new UserDTO(USERNAME, PASSWORD);
        var savedUser = new User(1L, USERNAME, ENCODE_PASSWORD, 10);
        var userDTO = new UserOutDTO(1L, USERNAME, 10, token);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dummyUser.username(), dummyUser.password());
        Mockito.when(userRepository.findByUsername(dummyUser.username())).thenReturn(Optional.of(savedUser));
        Mockito.when(tokenService.generateVerifyToken(savedUser)).thenReturn(token);
        Mockito.when(userMapper.toDTO(savedUser, token)).thenReturn(userDTO);
        Mockito.when(authenticationManagerBuilder.getObject()).thenReturn(providerManager);
        Mockito.when(providerManager.authenticate(authenticationToken)).thenReturn(authentication);

        Assertions.assertEquals(userDTO, userService.login(dummyUser));
    }

}
