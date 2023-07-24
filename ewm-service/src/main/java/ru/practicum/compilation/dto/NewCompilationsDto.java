package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationsDto {
    private List<Integer> events = Collections.emptyList();
    private boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
