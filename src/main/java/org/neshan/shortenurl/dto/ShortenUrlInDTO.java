package org.neshan.shortenurl.dto;

import jakarta.validation.constraints.NotNull;

import java.net.URI;

public record ShortenUrlInDTO(@NotNull URI shortUrl) {
}
