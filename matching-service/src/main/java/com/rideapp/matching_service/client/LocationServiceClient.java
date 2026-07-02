package com.rideapp.matching_service.client;

import com.rideapp.matching_service.dto.NearByDriverResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class LocationServiceClient {

    private final RestClient restClient;

    public LocationServiceClient(@Value("${location.service.url}") String locationServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(locationServiceUrl)
                .build();
    }

    public List<NearByDriverResponse> getNearByDrivers(double latitude, double longitude, double radius) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/locations/drivers/nearby")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("radius", radius)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<NearByDriverResponse>>() {
                });
    }
}
