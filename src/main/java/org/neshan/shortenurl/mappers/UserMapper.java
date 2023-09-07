package org.neshan.shortenurl.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.neshan.shortenurl.dto.UserOutDTO;
import org.neshan.shortenurl.entities.User;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    @Mapping(target = "remainedShortUrl",constant = "10")
    User toEntity(String username, String password);

    UserOutDTO toDTO(Long id);
    UserOutDTO toDTO(User user,String token);
}
