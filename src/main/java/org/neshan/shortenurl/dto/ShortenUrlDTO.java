package org.neshan.shortenurl.dto;

import jakarta.validation.constraints.NotNull;

import java.net.URL;

public record ShortenUrlDTO(@NotNull URL realUrl) {
}
