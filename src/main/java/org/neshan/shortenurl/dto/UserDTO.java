package org.neshan.shortenurl.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDTO(@NotEmpty String username, @NotEmpty String password) {
}
