package com.uniquindio.backend.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponse {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
