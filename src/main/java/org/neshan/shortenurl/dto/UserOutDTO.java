package org.neshan.shortenurl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserOutDTO(Long id, String username, Integer remainedShortUrl, String token) {
}
