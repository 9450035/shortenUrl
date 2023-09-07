package org.neshan.shortenurl.configuration;

import lombok.RequiredArgsConstructor;
import org.neshan.shortenurl.services.ShortenUrlService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ShortenUrlService shortenUrlService;

    @Scheduled(cron = "@yearly")
    void deleteShortLink() {
        var deleteTime = ZonedDateTime.now().minusYears(1L).toInstant().toEpochMilli();
        shortenUrlService.deleteShortenUrl(deleteTime);
    }
}
