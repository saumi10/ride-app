package com.rideapp.ride_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rideapp.ride_service.model.Ride;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, String> {
    List<Ride> findByRiderIdOrderByCreatedAtDesc(String riderId);
}
