package com.rideapp.ride_service.dto;

import java.time.LocalDateTime;

import com.rideapp.ride_service.model.RideStatus;


public class RideResponse {
    private String id;

 
    private String riderId;
   
    private String driverId;

 
    private double pickupLatitude;

    private double pickupLongitude;

    private String pickupAddress;

    private double dropLatitude;

    private double dropLongitude;

    private String dropAddress;

    //Ride status - tracks lifecycle of the ride
    private RideStatus status; 

    //fare details
    private double estimatedFare;
    private double actualFare;

  
    private LocalDateTime createdAt;
  
    private LocalDateTime updatedAt;
    
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
