package com.thoughtworks.taskmaster.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionDto {
    private final OffsetDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
}
