package ru.khrebtov.crew_raketa.dto;

import javax.validation.constraints.NotBlank;

public class ValueDto {
    @NotBlank
    private String value;

    public String getValue() {
        return value;
    }
}
