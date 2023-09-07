package org.neshan.shortenurl.dto;

public record ShortenUrlOutDTO(Long id, String realUrl, String shortUrl, Long createTime,Long view, UserOutDTO user) {
}
