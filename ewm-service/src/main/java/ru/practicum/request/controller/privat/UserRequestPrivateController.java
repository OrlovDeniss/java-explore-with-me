package ru.practicum.request.controller.privat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdate;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

public interface UserRequestPrivateController {

    @GetMapping("{userId}/requests")
    ResponseEntity<List<ParticipationRequestDto>> getRequestsByPrivate(@PathVariable @Positive Integer userId);

    @PostMapping("{userId}/requests")
    ResponseEntity<ParticipationRequestDto> saveNewRequestByPrivate(@PathVariable @Positive Integer userId,
                                                                    @RequestParam @Positive Integer eventId);

    @PatchMapping("{userId}/requests/{requestId}/cancel")
    ResponseEntity<ParticipationRequestDto> cancelRequestByPrivate(@PathVariable @Positive Integer userId,
                                                                   @PathVariable @Positive Integer requestId);

    @GetMapping("{userId}/events/{eventId}/requests")
    ResponseEntity<List<ParticipationRequestDto>> getEventRequestsByPrivate(@PathVariable @Positive Integer userId,
                                                                            @PathVariable @Positive Integer eventId);

    @PatchMapping("{userId}/events/{eventId}/requests")
    ResponseEntity<EventRequestStatusUpdate> updateRequestStatusByPrivate(@PathVariable @Positive Integer userId,
                                                                          @PathVariable @Positive Integer eventId,
                                                                          @Valid @RequestBody EventRequestStatusUpdateRequest dto);

}
