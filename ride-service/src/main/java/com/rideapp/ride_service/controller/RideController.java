package com.rideapp.ride_service.controller;

import com.rideapp.ride_service.dto.RideRequest;
import com.rideapp.ride_service.dto.RideResponse;
import com.rideapp.ride_service.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
@Slf4j
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    // Rider requests a new ride
    @PostMapping("/request")
    public ResponseEntity<RideResponse> requestRide(@Valid @RequestBody RideRequest rideRequest){

        log.info("Ride request received from rider: {}", rideRequest.getRiderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(rideService.requestRide(rideRequest));
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable String rideId){
        log.info("Fetching ride details for rideId: {}", rideId);
        return ResponseEntity.ok(rideService.getRideById(rideId));
    }

    @GetMapping("/rider/{riderId}")
    public ResponseEntity<List<RideResponse>> getRidesByRider(@PathVariable String riderId){
        log.info("Fetching rides for riderId: {}", riderId);
        return ResponseEntity.ok(rideService.getRidesByRider(riderId));
    }

    // Driver starts the Ride
    @PutMapping("/{rideId}/start")
    public ResponseEntity<RideResponse> startRide(@PathVariable String rideId){
        log.info("Starting ride for rideId: {}", rideId);
        return ResponseEntity.ok(rideService.startRide(rideId));
    }

    @PutMapping("/{rideId}/complete")
    public ResponseEntity<RideResponse> completeRide(@PathVariable String rideId){
        log.info("Completing ride for rideId: {}", rideId);
        return ResponseEntity.ok(rideService.completeRide(rideId));
    }

    @PutMapping("/{rideId}/cancel")
    public ResponseEntity<RideResponse> cancelRide(@PathVariable String rideId){
        log.info("Cancelling ride for rideId: {}", rideId);
        return ResponseEntity.ok(rideService.cancelRide(rideId));
    }

}