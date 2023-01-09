package com.praca.komis.project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class DefaultErrorsDTO {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
