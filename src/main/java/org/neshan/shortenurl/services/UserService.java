package org.neshan.shortenurl.services;

import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.neshan.shortenurl.dto.UserDTO;
import org.neshan.shortenurl.dto.UserOutDTO;
import org.neshan.shortenurl.entities.User;
import org.neshan.shortenurl.mappers.UserMapper;
import org.neshan.shortenurl.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TokenService tokenService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserOutDTO register(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.username())) {
            throw new UserException("user.Exist");
        }

        var pass = passwordEncoder.encode(userDTO.password());
        return userMapper.toDTO(userRepository.save(userMapper.toEntity(userDTO.username(), pass)).getId());
    }

    public UserOutDTO login(UserDTO userDTO) {
        return userRepository.findByUsername(userDTO.username())
                .map(user -> {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user.getUsername(), userDTO.password());
                    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return userMapper.toDTO(user, tokenService.generateVerifyToken(user));
                }).orElseThrow(() -> new UserException("authError"));
    }

    public Optional<User> getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    @StandardException
    public static class UserException extends RuntimeException {

    }
}
