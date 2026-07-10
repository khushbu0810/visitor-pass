# 🏢 VisitorPass System


![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge)
![Spring
Boot](https://img.shields.io/badge/Spring_Boot-4.x-6DB33F?style=for-the-badge&logo=springboot)
![Angular](https://img.shields.io/badge/Angular-20-DD0031?style=for-the-badge&logo=angular)
![Kafka](https://img.shields.io/badge/Apache_Kafka-4.2-231F20?style=for-the-badge&logo=apachekafka)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker)
![JWT](https://img.shields.io/badge/JWT-Authentication-blue?style=for-the-badge)
![MySQL](https://img.shields.io/badge/TiDB-Cloud-4479A1?style=for-the-badge)

**A complete Visitor Management System built with Spring Boot
Microservices, Kafka, Docker, Angular and JWT Authentication.**


------------------------------------------------------------------------

# 📖 Overview

VisitorPass is a society visitor management application where residents
can register visitors and generate QR-based visitor passes. Guards
verify visitors by scanning the QR code and manage visitor entry and
exit.

The project follows a **Microservices Architecture** with both:

-   **Synchronous communication** using Spring RestClient
-   **Asynchronous communication** using Apache Kafka

------------------------------------------------------------------------

# ✨ Features

## Resident

-   Register/Login using JWT
-   Google Sign-In
-   Complete resident profile
-   Edit own profile
-   Add visitors
-   Update visitor details
-   Generate QR Pass
-   View generated passes

## Visitor

-   Store visitor information
-   Track visitor status
-   Linked with resident

Statuses: `PENDING → APPROVED → ENTERED → EXITED`

Additional statuses: - CANCELLED - EXPIRED

## Pass

-   QR Code generation (ZXing)
-   Pass validation
-   Delete pass when cancelled

## Guard

-   View today's visitors
-   Scan QR Code
-   Verify visitor
-   Approve / Reject visitor
-   Mark Entered
-   Mark Exited

------------------------------------------------------------------------

# 🏗️ Microservice Architecture

``` text
                        Angular Frontend
                               │
      ┌────────────────────────┼────────────────────────┐
      │                        │                        │
      ▼                        ▼                        ▼
Resident Service        Visitor Service         Guard Service
                              │
                     Publish Kafka Event
                              │
                              ▼
                      Apache Kafka (Aiven)
                              │
                              ▼
                        Pass Service
                              │
                        Generate QR
```

------------------------------------------------------------------------

# 📂 Project Structure

``` text
VisitorPass
│
├── Core
│   ├── Events
│   ├── DTOs
│   ├── Enums
│   └── Shared Models
│
├── ResidentMicroservice
├── VisitorMicroservice
├── PassMicroservice
├── GuardMicroservice
├── Frontend (Angular)
├── docker-compose.yml
├── pom.xml
└── README.md
```

------------------------------------------------------------------------

# 🛠️ Technology Stack

Layer              Technology
  ------------------ ---------------------------------
Backend            Java, Spring Boot
Security           Spring Security, JWT
Frontend           Angular
Database           TiDB Cloud (MySQL Compatible)
Messaging          Apache Kafka
QR Code            ZXing
Documentation      Swagger/OpenAPI
Containerization   Docker, Docker Compose
Cloud              Render, Docker Hub, Aiven Kafka

------------------------------------------------------------------------

# 🔐 Authentication

-   JWT Authentication
-   Google Login
-   Role Based Authorization

Flow:

``` text
Google Login
      │
      ▼
Verify Google ID Token
      │
      ▼
Generate JWT
      │
      ▼
Complete Resident Profile
```

------------------------------------------------------------------------

# 🔄 Application Flow

## Resident

``` text
Register/Login
      │
      ▼
Complete Profile
      │
      ▼
Create Visitor
      │
      ▼
Generate Pass
      │
      ▼
QR Generated
```

## Guard

``` text
Today's Visitors
      │
      ▼
   Scan QR
      │
      ▼
   Approve
      │
      ▼
   Entered
      │
      ▼
   Exited
```

------------------------------------------------------------------------

# 📡 Communication Between Services

## REST (Synchronous)

-   Pass Service → Visitor Service
-   JWT Validation
-   Visitor Verification

## Kafka (Asynchronous)

### Topic: `pass-created`

Producer: - Visitor Service

Consumer: - Pass Service

Purpose: Generate QR Pass.


------------------------------------------------------------------------

# 📨 Kafka Event Flow

``` text
Resident
    │
Create Visitor
    │
Generate Pass
    │
Visitor Service
    │
Publish PassCreatedEvent
    │
Apache Kafka
    │
Pass Service
    │
Generate QR Pass
```

------------------------------------------------------------------------

# 🐳 Docker

Each microservice contains its own Dockerfile.

``` text
ResidentMicroservice/
    Dockerfile

VisitorMicroservice/
    Dockerfile

PassMicroservice/
    Dockerfile

GuardMicroservice/
    Dockerfile
```

A common `docker-compose.yml` starts:

-   Resident Service
-   Visitor Service
-   Pass Service
-   Kafka

------------------------------------------------------------------------

# ☁️ Deployment

-   Docker Images → Docker Hub
-   Backend → Render
-   Database → TiDB Cloud
-   Kafka → Aiven Kafka

------------------------------------------------------------------------

# ☁️ Aiven Kafka Setup

1.  Create Kafka Service
2.  Create Kafka Topics
3.  Download SSL Certificates or enable SASL
4.  Configure Spring Boot
5.  Deploy services

------------------------------------------------------------------------

# 📚 Swagger [IN - PROGRESS]

Swagger UI is enabled for every microservice.

Example:

``` text
http://localhost:8080/swagger-ui/index.html
```


------------------------------------------------------------------------

# 👩‍💻 Author

**Khushbu Ahlawat**

Built as a learning project to demonstrate Microservices Architecture
using Spring Boot, Kafka, Docker, JWT, Angular and Cloud Deployment.
