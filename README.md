# 🚖 Uber-Style Real-Time Driver Matching System

## About

How does Uber assign a driver within seconds, even when thousands of drivers are moving across a city in real time?

This project is a backend implementation of a real-time ride matching system inspired by modern ride-hailing platforms like Uber. It demonstrates how microservices, event-driven communication, and geospatial indexing work together to efficiently match riders with nearby drivers.

Instead of performing expensive SQL distance calculations over constantly moving drivers, the system stores live driver locations in **Redis** using **Geospatial Indexing**. When a rider requests a ride, the Matching Service performs a fast geospatial search to retrieve nearby drivers, ranks them using a weighted scoring algorithm based on distance and driver rating, and publishes the best match asynchronously through **Apache Kafka**.

The project follows a distributed microservices architecture where each service is responsible for a single business capability, making the system scalable, loosely coupled, and easy to extend. It showcases several real-world backend concepts including event-driven architecture, asynchronous messaging, geospatial search, inter-service communication, and distributed system design.

## Tech Stack

- Java 17
- Spring Boot
- Spring Cloud OpenFeign
- Apache Kafka
- Redis (Geospatial Indexing)
- Docker & Docker Compose
- Maven

---

## Services Overview

| Service | Port | Responsibility |
|---|---:|---|
| **location-service** | 8082 | Tracks real-time driver locations via Redis Geospatial |
| **ride-service** | 8083 | Manages ride lifecycle, publishes events to Kafka |
| **matching-service** | 8084 | Consumes ride events, finds and assigns the best driver |

---

## Architecture Flow

```text
Driver Phone
     │
     ▼
Location Service
     │
     ▼
Redis (GEOADD)

Rider App
     │
     ▼
Ride Service
     │
     ▼
Kafka (ride.requested)
     │
     ▼
Matching Service (Consumer)
     │
     ▼
Location Service (Find Nearby Drivers)
     │
     ▼
Redis (GEOSEARCH)
     │
     ▼
Matching Algorithm (Distance + Rating)
     │
     ▼
Kafka (ride.matched)
     │
     ▼
Ride Service
     │
     ▼
Driver Assigned
```

---

## How To Run

### Step 1: Start Infrastructure

```bash
docker-compose up -d
```

This starts Redis, MySQL, Zookeeper, and Kafka.

Wait **30 seconds** for Kafka to fully start before running the services.

### Step 2: Start Location Service

```bash
cd location-service
mvn spring-boot:run
```

### Step 3: Start Ride Service

```bash
cd ride-service
mvn spring-boot:run
```

### Step 4: Start Matching Service

```bash
cd matching-service
mvn spring-boot:run
```

---

## Testing End-to-End Flow

### Step 1: Add Driver Locations (Location Service)

```http
POST http://localhost:8082/api/v1/locations/drivers/update
Content-Type: application/json

{
    "driverId": "driver:1",
    "latitude": 12.9716,
    "longitude": 77.5946
}
```

```http
POST http://localhost:8082/api/v1/locations/drivers/update
Content-Type: application/json

{
    "driverId": "driver:2",
    "latitude": 12.9800,
    "longitude": 77.5800
}
```

```http
POST http://localhost:8082/api/v1/locations/drivers/update
Content-Type: application/json

{
    "driverId": "driver:3",
    "latitude": 12.9600,
    "longitude": 77.6100
}
```

---

### Step 2: Request a Ride (Ride Service)

```http
POST http://localhost:8083/api/v1/rides/request
Content-Type: application/json

{
    "riderId": "rider:1",
    "pickupLatitude": 12.9716,
    "pickupLongitude": 77.5946,
    "pickupAddress": "MG Road, Bangalore",
    "dropLatitude": 12.9352,
    "dropLongitude": 77.6245,
    "dropAddress": "Koramangala, Bangalore"
}
```

---

### Step 3: Check Ride Status

```http
GET http://localhost:8083/api/v1/rides/{rideId}
```

You should see:

- `driverId` assigned
- `status = ACCEPTED`

---

### Step 4: Start the Ride

```http
PUT http://localhost:8083/api/v1/rides/{rideId}/start
```

---

### Step 5: Complete the Ride

```http
PUT http://localhost:8083/api/v1/rides/{rideId}/complete
```

---

### Step 6: Get Rider History

```http
GET http://localhost:8083/api/v1/rides/rider/rider:1
```

---

## Verify in Redis CLI

```bash
docker exec -it redis-geo redis-cli
```

See all stored drivers:

```redis
ZRANGE drivers:locations 0 -1
```

Check a driver's coordinates:

```redis
GEOPOS drivers:locations "driver:1"
```

Calculate the distance between two drivers:

```redis
GEODIST drivers:locations "driver:1" "driver:2" km
```

---

## Key Backend Concepts Demonstrated

- Microservices Architecture
- Event-Driven Communication with Apache Kafka
- Redis Geospatial Indexing (GEOADD, GEOSEARCH, GEODIST)
- Asynchronous Event Processing
- REST APIs with Spring Boot
- OpenFeign for Inter-Service Communication
- Driver Matching Algorithm
- Dockerized Local Development
- Distributed System Design Principles

---

> **Note:** This project focuses on the backend architecture behind a real-time ride-hailing platform. It demonstrates how services communicate asynchronously, how live driver locations are indexed using Redis Geospatial features, and how riders are matched with nearby drivers efficiently using an event-driven microservices architecture.