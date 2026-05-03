#  Hotel Booking System API

A Spring Boot REST API for a hotel booking platform with JWT authentication, role-based access control, booking management, payments, and admin operations.
Team's LinkedIn profiles
Mansura--http://linkedin.com/in/mansura-badalova-3079b4293
Ulvi--https://www.linkedin.com/in/ülvihaciyev2006?utm_source=share_via&utm_content=profile&utm_medium=member_android
Gurbanali--https://www.linkedin.com/in/gurban-al%C4%B1ev-075b67407/

trello-----https://trello.com/b/bHep5idw/hotelbookingapp
---
Our Project Entity diagram
https://dbdiagram.io/d/69d81fea80896296845f911b

## 🚀 Features

### 👤 User Features
- Register & Login (JWT authentication)
- Update profile
- View hotels and rooms
- Create bookings
- Cancel bookings
- Make payments
- View personal profile & bookings

### 🛠 Admin Features
- Manage users (update, soft delete, balance management)
- Manage hotels (CRUD)
- Manage rooms (CRUD)
- View all users

### 🏨 Hotel & Room Features
- View all hotels
- Filter hotels by city
- Manage rooms per hotel

### 💳 Payment System
- Pay for bookings
- Refund on cancellation
- Balance-based payment system

---

## 🔐 Security

- JWT-based authentication
- Spring Security
- Role-based authorization:
  - ADMIN
  - USER
- Stateless session management
- Password encryption with BCrypt

---

## 🧱 Tech Stack

- Java 17+
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- MySQL
- Hibernate
- Lombok

---

##  Setup Instructions

### 1. Clone project
```bash
git clone https://github.com/your-repo/hotel-booking-app.git
cd hotel-booking-app  

## 2
Configure database

Create MySQL database:

CREATE DATABASE hotel_app_2;

## 3
Configure application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/hotel_app_2
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.application.name=HotelBookingApp

jwt.secret=YOUR_BASE64_SECRET
jwt.expiration-ms=3600000

## 4 
 Run application
mvn spring-boot:run

------------------------------
## Authentication

Register

POST /users/register

Login

POST /users/login

Response:

{
  "token": "JWT_TOKEN"
}

Use token:

Authorization: Bearer JWT_TOKEN


## API Endpoints

👤 Users
POST   /users/register
POST   /users/login
GET    /users/me
PUT    /users/me

🛠 Admin
GET    /admin
PUT    /admin/{id}
DELETE /admin/{id}
PUT    /admin/{id}/balance

🏨 Hotels
GET    /hotels
GET    /hotels/city?city=...

🏨 Admin Hotels
POST   /admin/hotels
PUT    /admin/hotels/{id}
PATCH  /admin/hotels/{id}
GET    /admin/hotels/{id}
DELETE /admin/hotels/{id}

🛏 Rooms
GET    /admin/rooms
GET    /admin/rooms/{id}
GET    /admin/rooms/hotel/{hotelId}
POST   /admin/rooms
PUT    /admin/rooms/{id}
PATCH  /admin/rooms/{id}
DELETE /admin/rooms/{id}

📅 Bookings
POST   /bookings
DELETE /bookings/{id}

 
💳 Payments
POST /payments


 Business Rules
Booking dates cannot be in the past
Check-out must be after check-in
Preventing Booking overlapping
Rooms cannot be double-booked
Cancelation rules:
PENDING → always cancellable
CONFIRMED → refund only if allowed time
Payment requires sufficient user balance


Validation
Bean Validation used (@NotNull, @Size, etc.)
Global exception handler implemented
Structured error responses

Status Flow
BookingStatus

PENDING → CONFIRMED → CANCELLED

PaymentStatus

PENDING → SUCCESS / FAILED / REFUNDED

Architecture

Controller → Service → Repository → DB
                ↓
              Mapper
                ↓
              DTO
Entities

USER

id (PK)
first_name
last_name
email (unique)
password
number
role
status
balance

BOOKING

id (PK)
check_in_date
check_out_date
total_price
booking_status
user_id (FK)
room_id (FK)   

ROOM

id (PK)
room_number (unique)
price_per_night
type
available
hotel_id (FK)  


HOTEL

id (PK)
name
description
country
city
address
zip_code
phone
rating  


PAYMENT

id (PK)
booking_id (FK)
amount
currency
payment_method
status
created_at




hotel-booking-app/
│
├── src/main/java/org/ironhack/hotelbookingapp
│
│
├── 📁 controller
│   ├── AdminController.java
│   ├── UserController.java
│   ├── HotelController.java
│   ├── RoomController.java
│   ├── BookingController.java
│   ├── PaymentController.java
│   └── AllHotelController.java
│
│
├── 📁 service
│   ├── UserService.java
│   ├── HotelService.java
│   ├── RoomService.java
│   ├── BookingService.java
│   ├── PaymentService.java
│   ├── JWTService.java
│   └── MyUserDetailsService.java
│
│
├── 📁 repository
│   ├── UserRepository.java
│   ├── HotelRepository.java
│   ├── RoomRepository.java
│   ├── BookingRepository.java
│   └── PaymentRepository.java
│
│
├── 📁 entity
│   ├── User.java
│   ├── UserPrincipal.java
│   ├── Hotel.java
│   ├── Room.java
│   ├── Booking.java
│   └── Payment.java
│
│
├── 📁 dto
│   ├── 📁 request
│   │   ├── UserRequestDto.java
│   │   ├── LoginRequest.java
│   │   ├── UpdateUserRequestDto.java
│   │   ├── AdminUpdateRequestDto.java
│   │   ├── HotelRequestDto.java
│   │   ├── HotelRequestUpdateDto.java
│   │   ├── RoomRequestDto.java
│   │   ├── RoomRequestUpdateDto.java
│   │   ├── BookingRequestDto.java
│   │   └── PaymentRequestDto.java
│   │
│   ├── 📁 response
│       ├── UserResponseDto.java
│       ├── UserResponseForBooking.java
│       ├── HotelResponseDto.java
│       ├── HotelResponseDtoForUser.java
│       ├── RoomResponseDto.java
│       ├── BookingResponseDto.java
│       ├── PaymentResponseDto.java
│       ├── ErrorResponseDto.java
│       └── ValidationErrorResponseDto.java
│
│
├── 📁 mapper
│   ├── UserMapper.java
│   ├── HotelMapper.java
│   ├── RoomMapper.java
│   ├── BookingMapper.java
│   └── PaymentMapper.java
│
│
├── 📁 security
│   ├── SecurityConfig.java
│   ├── JWTFilter.java
│
│
├── 📁 enums
│   ├── Role.java
│   ├── Status.java
│   ├── RoomType.java
│   ├── BookingStatus.java
│   ├── PaymentStatus.java
│   ├── PaymentMethod.java
│   └── Currency.java
│
│
├── 📁 exception
│   ├── GlobalExceptionHandler.java
│   ├── UserNotFoundException.java
│   ├── HotelNotFound.java
│   ├── RoomNotFound.java
│   ├── BookingNotFound.java
│   ├── UserExistsException.java
│   ├── RoomExistsException.java
│   ├── HotelExistsException.java
│   ├── InsufficientBalanceException.java
│   ├── UserNotActiveException.java
│   ├── InvalidCheckInDateException.java
│   ├── InvalidDateException.java
│   └── ... (digər custom exceptions)
│
│
├── HotelBookingAppApplication.java
│
└── resources
    ├── application.properties



