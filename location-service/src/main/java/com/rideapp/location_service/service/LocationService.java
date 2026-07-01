package com.rideapp.location_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.rideapp.location_service.dto.DriverLocationRequest;
import com.rideapp.location_service.dto.NearByDriverResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {

    private final RedisTemplate<String, String> redisTemplate;

    // Redis key for storing driver locations
    private static final String DRIVER_GEO_KEY = "drivers:locations";

    /**
     * Updates the location of a driver in Redis.
     * Called every 3 seconds by the driver to update their current location.
     * Maps to Redis GEOADD command.
    */

    public void updateDriverLocation(DriverLocationRequest driverLocationRequest){
        log.info("Updating driver location: {}", driverLocationRequest.getDriverId());

        Point driverPoint = new Point(
            driverLocationRequest.getLongitude(),
            driverLocationRequest.getLatitude()
        );

        //.opsForGeo() - this method will return a GeoOperations object that provides methods for working with geospatial data in Redis.
        //.add() - this method will add a geospatial item to the specified key in Redis. It takes three parameters: the key, the point (longitude and latitude), and the member (driverId).
        redisTemplate.opsForGeo().add(DRIVER_GEO_KEY, driverPoint, driverLocationRequest.getDriverId());
    }

    /**
     * Find nearby drivers within a specified radius from the given location.
     * Called by the matching-service when a rider requests a ride.
     * Maps to Redis GEORADIUS command.
    */

    public List<NearByDriverResponse> findNearbyDrivers(double latitude, double longitude, double radiusInKm){
        log.info("Finding nearby drivers for location: ({}, {}) within radius: {} km", latitude, longitude, radiusInKm);

        Circle searchArea = new Circle(
            new Point(longitude, latitude),
            new Distance(radiusInKm, Metrics.KILOMETERS)
        );

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo()
            .radius(DRIVER_GEO_KEY,
                 searchArea,
                  RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                                                        .includeCoordinates()
                                                        .includeDistance()
                                                        .sortAscending()
                                                        .limit(10)
                                                    );

        List<NearByDriverResponse> nearbyDrivers = new ArrayList<>();
        if(results!=null){
            results.getContent().forEach(result -> {
                RedisGeoCommands.GeoLocation<String> location = result.getContent();
                nearbyDrivers.add(new NearByDriverResponse(
                    location.getName(),
                    location.getPoint().getY(), // latitude
                    location.getPoint().getX(), // longitude
                    result.getDistance().getValue() // distance in km
                ));
            });
        }
        log.info("Found {} nearby drivers", nearbyDrivers.size());
        return nearbyDrivers;
    }

    /**
     * Removes a driver from Redis when they go offline.
     * Maps to Redis ZREM command.
    */
    public void removeDriver(String driverId){
        log.info("Removing driver: {}", driverId);
        redisTemplate.opsForGeo().remove(DRIVER_GEO_KEY, driverId);
    }


}
