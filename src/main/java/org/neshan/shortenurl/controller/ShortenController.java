package org.neshan.shortenurl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.neshan.shortenurl.dto.ShortenUrlDTO;
import org.neshan.shortenurl.dto.ShortenUrlInDTO;
import org.neshan.shortenurl.dto.ShortenUrlOutDTO;
import org.neshan.shortenurl.services.ShortenUrlService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shorten")
@RequiredArgsConstructor
public class ShortenController {

    private final ShortenUrlService shortenUrlService;

    @PostMapping
    public ResponseEntity<ShortenUrlOutDTO> create(@Valid @RequestBody ShortenUrlDTO shortenUrlDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shortenUrlService.create(shortenUrlDTO));
    }

    @GetMapping
    public ResponseEntity<Page<ShortenUrlOutDTO>> getShortenUrl(@RequestParam(required = false)Long userId,
                                                                @RequestParam Pageable pageable) {
        return ResponseEntity.ok(shortenUrlService.getUrls(userId, pageable));
    }

    @PostMapping("/real-url")
    public ResponseEntity<ShortenUrlOutDTO> getUrl(@Valid @RequestBody ShortenUrlInDTO url) {
        return ResponseEntity.of(shortenUrlService.getRealUrl(url));
    }

}
