package org.neshan.shortenurl.services;

import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.neshan.shortenurl.dto.ShortenUrlDTO;
import org.neshan.shortenurl.dto.ShortenUrlInDTO;
import org.neshan.shortenurl.dto.ShortenUrlOutDTO;
import org.neshan.shortenurl.mappers.ShortenMapper;
import org.neshan.shortenurl.repositories.ShortenUrlRepository;
import org.neshan.shortenurl.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ShortenUrlService {
    private final ShortenUrlRepository shortenUrlRepository;
    private final UserService userService;
    private final ShortenMapper shortenMapper;

    public ShortenUrlOutDTO create(ShortenUrlDTO shortenUrlDTO) {
        return SecurityUtils.getCurrentUserLogin().flatMap(userService::getUser)
                .filter(user -> user.getRemainedShortUrl() > 0)
                .map(user -> {
                    user.setRemainedShortUrl(user.getRemainedShortUrl() - 1);
                    userService.update(user);
                    var shortUrl = generateShortURLFromOriginalURL(shortenUrlDTO.realUrl().toString());
                    return shortenUrlRepository
                            .save(shortenMapper.toEntity(shortenUrlDTO.realUrl().toString(), shortUrl, user,
                                    System.currentTimeMillis(), 0L));
                }).map(shortenMapper::toDTO).orElseThrow(() -> new ShortenUrlException("can not create"));
    }

    private static String generateShortURLFromOriginalURL(String realUrl) {
        var random = new Random();
        String characters = extractCharacters(realUrl);
        int length = 6;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return "rb.gy/" + sb;
    }

    private static String extractCharacters(String longURL) {
        String withoutHttps = longURL.replaceFirst("^https://", "");
        String withoutWWW = withoutHttps.replaceFirst("^www.", "");
        String characters = withoutWWW.replaceAll("[^a-zA-Z0-9]", "");
        if (characters.isEmpty()) {
            throw new IllegalArgumentException("Invalid URL");
        }
        return characters;
    }

    public Page<ShortenUrlOutDTO> getUrls(Long userId, Pageable pageable) {
        if (userId == null)
            return shortenUrlRepository.findAll(pageable).map(shortenMapper::toDTO);
        return shortenUrlRepository.findByUserId(userId, pageable).map(shortenMapper::toDTO);
    }

    public Optional<ShortenUrlOutDTO> getRealUrl(ShortenUrlInDTO shortUrl) {
        try {
            var path = shortUrl.shortUrl().getPath().split("/")[0].concat("/".concat(shortUrl.shortUrl().getPath().split("/")[1]));
            return shortenUrlRepository.findByShortUrl(path)
                    .map(shortenUrl -> {
                        shortenUrl.setView(shortenUrl.getView() + 1);
                        shortenUrl = shortenUrlRepository.save(shortenUrl);
                        shortenUrl.setRealUrl(shortUrl.shortUrl().getPath().split("/").length < 3 ?
                                shortenUrl.getRealUrl().concat(shortUrl.shortUrl().getPath().substring(path.length())).concat(shortUrl.shortUrl().getQuery() != null ? "?".concat(shortUrl.shortUrl().getQuery()) : "") :
                                shortenUrl.getRealUrl().concat(shortUrl.shortUrl().getPath().substring(path.length())));
                        return shortenMapper.toDTO(shortenUrl);
                    });
        } catch (IllegalArgumentException ex) {
            throw new ShortenUrlException("notValid url");
        }

    }

    public void deleteShortenUrl(long deleteTime) {

        shortenUrlRepository.deleteByCreateTimeLessThan(deleteTime);

    }

    @StandardException
    public static class ShortenUrlException extends RuntimeException {

    }
}
