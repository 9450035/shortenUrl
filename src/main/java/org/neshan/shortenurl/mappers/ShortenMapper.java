package org.neshan.shortenurl.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.neshan.shortenurl.dto.ShortenUrlOutDTO;
import org.neshan.shortenurl.entities.ShortenUrl;
import org.neshan.shortenurl.entities.User;

import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(componentModel = SPRING)
public interface ShortenMapper {

    @Mapping(target = "id",ignore = true)
    ShortenUrl toEntity(String realUrl, String shortUrl, User user,Long createTime,Long view);

    ShortenUrlOutDTO toDTO(ShortenUrl shortenUrl);
}
