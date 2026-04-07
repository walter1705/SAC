package com.uniquindio.backend.model.dto.response;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {}
