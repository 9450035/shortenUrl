package org.neshan.shortenurl.services;

import lombok.RequiredArgsConstructor;
import org.neshan.shortenurl.entities.User;
import org.neshan.shortenurl.security.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {
    public static final String USER_ID = "userId";
    private final JwtEncoder encoder;

    public String generateLoginToken(UserDetailsImpl user) {
            return generateToken(user, Duration.ofDays(365));
    }

    public String generateRegisterToken(User user) {
        return generateToken(new UserDetailsImpl(user), Duration.ofHours(1));
    }

    public String generateVerifyToken(User user) {
        return generateToken(new UserDetailsImpl(user), Duration.ofDays(365));
    }


    private String generateToken(UserDetailsImpl user, Duration amountToAdd) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(amountToAdd))
                .subject(user.getUsername())
                .claim("scope", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" ")))
                .claim("enabled", user.isEnabled())
                .claim(USER_ID, user.getUserId())
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
