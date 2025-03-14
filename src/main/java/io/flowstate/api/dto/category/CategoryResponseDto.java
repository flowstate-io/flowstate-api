package io.flowstate.api.dto.category;

import java.util.UUID;

public record CategoryResponseDto(
        UUID id,
        String name,
        String color
) {
}
