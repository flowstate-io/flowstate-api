package io.flowstate.api.dto;

public record UserProfileDto(String email, String username, String firstName, String lastName, boolean emailVerified) {
}
