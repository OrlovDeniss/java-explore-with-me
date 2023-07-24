package ru.practicum.compilation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {
    @JsonProperty("events")
    private List<Integer> eventsIds;
    private Boolean pinned;

    @Size(min = 1, max = 50)
    private String title;
}
