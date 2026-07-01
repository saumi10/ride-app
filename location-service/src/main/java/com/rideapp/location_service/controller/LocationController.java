package com.rideapp.location_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rideapp.location_service.dto.DriverLocationRequest;
import com.rideapp.location_service.dto.NearByDriverResponse;
import com.rideapp.location_service.service.LocationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/locations")
@Slf4j
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    //driver sends his location to the server, hence calls this endpoint every 3 seconds
    @PostMapping("/drivers/update")
    public ResponseEntity<String> updateDriverLocation(@RequestBody DriverLocationRequest driverLocationRequest){
        locationService.updateDriverLocation(driverLocationRequest);
        return ResponseEntity.ok("Driver location updated successfully");
    }

    //matching-service calls this when a rider requests for a ride, to get the nearby drivers
    @GetMapping("/drivers/nearby")
    public ResponseEntity<List<NearByDriverResponse>> getNearbyDrivers(@RequestParam double latitude,
                                                                     @RequestParam double longitude, 
                                                                     @RequestParam (defaultValue = "5.0") double radius){
        return ResponseEntity.ok(locationService.getNearbyDrivers(latitude, longitude, radius));
    }
    
    //called when driver goes offline, to remove the driver
    @DeleteMapping("/drivers/{driverId}")
    public ResponseEntity<String> removeDriver(@PathVariable String driverId){
        locationService.removeDriver(driverId);
        return ResponseEntity.ok("Driver removed successfully");
    }
}
