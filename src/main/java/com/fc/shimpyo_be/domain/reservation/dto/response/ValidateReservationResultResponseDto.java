package com.fc.shimpyo_be.domain.reservation.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ValidateReservationResultResponseDto(
    boolean isAvailable,
    List<Long> unavailableIds
) {
}
