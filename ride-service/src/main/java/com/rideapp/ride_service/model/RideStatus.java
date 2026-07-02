package com.rideapp.ride_service.model;

/*
*Flow of the ride:
REQUESTED->MATCHING->ACCEPTED->DRIVER_ARRIVING->RIDE_STARTED->COMPLETED
->CANCELLED
*/
public enum RideStatus {
    REQUESTED,
    MATCHING,
    ACCEPTED,
    DRIVER_ARRIVING,
    RIDE_STARTED,
    COMPLETED,
    CANCELLED
}
