package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.baseclient.BaseClient;
import ru.practicum.dto.EndpointHit;

import java.util.Map;

public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${statistic-server.url}") String serverUrl,
                       RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveEndpointHit(EndpointHit endpointHit) {
        return post("/hit", endpointHit);
    }

    public ResponseEntity<Object> getViewStats(Map<String, Object> parameters) {
        return get("/stats", parameters);
    }

}