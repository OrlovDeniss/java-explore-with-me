package ru.practicum.location.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewLocationDto {
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
    @NotNull
    private Double radius;
}
