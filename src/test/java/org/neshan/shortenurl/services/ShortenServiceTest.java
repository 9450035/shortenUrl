package org.neshan.shortenurl.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.neshan.shortenurl.dto.ShortenUrlInDTO;
import org.neshan.shortenurl.dto.ShortenUrlOutDTO;
import org.neshan.shortenurl.dto.UserOutDTO;
import org.neshan.shortenurl.entities.ShortenUrl;
import org.neshan.shortenurl.entities.User;
import org.neshan.shortenurl.mappers.ShortenMapper;
import org.neshan.shortenurl.repositories.ShortenUrlRepository;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ShortenServiceTest {

    private final URL REAL_URL = URI.create("https://google.com").toURL();
    private final static String USERNAME = "admin";
    private final static String ENCODE_PASSWORD = "$2a$10$FrHteVtpnj74WCRAuFbCn.ZUUljtsUncDBmoFwi88Xam.XVRQA4BW";

    @Mock
    private ShortenUrlRepository shortenUrlRepository;
    @Mock
    private ShortenMapper shortenMapper;
    @Mock
    private UserService userService;

    private ShortenUrlService shortenUrlService;

    public ShortenServiceTest() throws MalformedURLException {
    }

    @BeforeEach
    void setup() {
        shortenUrlService = new ShortenUrlService(shortenUrlRepository, userService, shortenMapper);
    }

    @Test
    void getUrl() {

        var dummyInput = new ShortenUrlInDTO(URI.create("rb.gy/3buest"));
        var path = dummyInput.shortUrl().getPath().split("/")[0].concat("/".concat(dummyInput.shortUrl().getPath().split("/")[1]));
        var savedUser = new User(1L, USERNAME, ENCODE_PASSWORD, 10);
        var userDTO = new UserOutDTO(1L, USERNAME, 10, null);
        var shortenUrl = new ShortenUrl(1L, REAL_URL.toString(), dummyInput.shortUrl().toString(), System.currentTimeMillis(), 2L, savedUser);
        var updatedShortenUrl = new ShortenUrl(1L, REAL_URL.toString(), dummyInput.shortUrl().toString(), System.currentTimeMillis(), 3L, savedUser);
        var shortenUrlDTO = new ShortenUrlOutDTO(1L, REAL_URL.toString(), dummyInput.shortUrl().toString(), System.currentTimeMillis(), 3L, userDTO);

        Mockito.when(shortenUrlRepository.findByShortUrl(path)).thenReturn(Optional.of(shortenUrl));
        Mockito.when(shortenUrlRepository.save(updatedShortenUrl)).thenReturn(updatedShortenUrl);
        Mockito.when(shortenMapper.toDTO(shortenUrl)).thenReturn(shortenUrlDTO);

        Assertions.assertEquals(Optional.of(shortenUrlDTO), shortenUrlService.getRealUrl(dummyInput));
    }
}
