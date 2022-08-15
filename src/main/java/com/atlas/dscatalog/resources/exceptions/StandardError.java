package com.atlas.dscatalog.resources.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class StandardError {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}