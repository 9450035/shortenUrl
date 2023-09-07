package org.neshan.shortenurl.repositories;

import org.neshan.shortenurl.dto.ShortenUrlOutDTO;
import org.neshan.shortenurl.entities.ShortenUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortenUrlRepository extends JpaRepository<ShortenUrl, Long> {
    Optional<ShortenUrl>findByShortUrl(String shortUrl);

    void deleteByCreateTimeLessThan(long deleteTime);

    Page<ShortenUrl> findByUserId(Long userId, Pageable pageable);
}
