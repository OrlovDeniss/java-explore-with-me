package ru.practicum.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
public class StatsClient {
    private final RestTemplate restTemplate;
    private final String serverUrl;
    private final HttpHeaders headers = new HttpHeaders();
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(RestTemplateBuilder builder,
                       ObjectMapper objectMapper,
                       @Value("${stats.server.url}") String serverUrl) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        this.serverUrl = serverUrl;
        this.objectMapper = objectMapper;
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    public ResponseEntity<Object> saveEndpointHit(EndpointHit endpointHit) {
        var json = objectMapper.writeValueAsString(endpointHit);
        log.info(json);
        return restTemplate.exchange(
                serverUrl + "/hit",
                HttpMethod.POST,
                new HttpEntity<>(json, headers),
                new ParameterizedTypeReference<>() {
                });
    }

    public ResponseEntity<List<ViewStats>> getViewStats(LocalDateTime start,
                                                        LocalDateTime end,
                                                        List<String> uris,
                                                        Boolean unique) {
        return restTemplate.exchange(
                serverUrl + "stats?start={start}&end={end}&uris={uris}&unique={unique}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                },
                Map.of("start", start.format(format),
                        "end", end.format(format),
                        "uris", uris.toString().replace("[", "").replace("]", ""),
                        "unique", unique)
        );
    }
}