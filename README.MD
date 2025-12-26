# 🏥 Happy Health Clinic - Backend API

> **Final Backend Development Project**  
> Complete clinic management system with RESTful APIs, JWT authentication, and external integrations.

---

## 📋 Table of Contents

- [Description](#-description)
- [Technologies](#-technologies)
- [Architecture](#-architecture)
- [Project Setup](#-project-setup)
- [Configuration](#-configuration)
- [Running the Application](#-running-the-application)
- [API Documentation](#-api-documentation)
- [Postman Collection](#-postman-collection)
- [Database](#-database)
- [Security](#-security)
- [External APIs](#-external-apis)
- [Testing](#-testing)
- [Project Structure](#-project-structure)

---

## 📖 Description

**Happy Health Clinic** is a comprehensive clinic management system that includes:

- 👥 **User Management**: Patients, Doctors, Administrative Staff
- 📅 **Appointment System** with integrated weather forecasts (OpenWeather API)
- 📋 **Medical Records** with diagnoses and ICD-10 codes
- 💊 **Medical Prescriptions** with pharmaceutical information (FDA Drug API)
- 💰 **Billing & Invoicing** with automatic email notifications (Mailgun API)
- 🔐 **JWT Authentication** and role-based authorization

---

## 🛠 Technologies

### Backend
- **Java 21**
- **Spring Boot 4.0.1**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA**
- **PostgreSQL** (Database)
- **Maven** (Build Tool)

### External APIs
- **OpenWeather API** - Weather forecasts for appointments
- **FDA Drug API** - Pharmaceutical information for prescriptions
- **Mailgun API** - Email delivery for invoices
- **UI Avatars API** - User avatars

### Tools
- **Lombok** - Boilerplate reduction
- **Jakarta Validation** - Data validation
- **Postman** - API Testing (57 requests)

---

## 🏗 Architecture
```
┌─────────────┐
│   Client    │ (Postman)
└──────┬──────┘
       │ HTTP/REST
┌──────▼──────────────────────────────┐
│     Spring Boot Application          │
│  ┌────────────────────────────────┐ │
│  │   Security Layer (JWT)         │ │
│  └────────────┬───────────────────┘ │
│  ┌────────────▼───────────────────┐ │
│  │   Controllers (REST)           │ │
│  └────────────┬───────────────────┘ │
│  ┌────────────▼───────────────────┐ │
│  │   Services (Business Logic)    │ │
│  └────────────┬───────────────────┘ │
│  ┌────────────▼───────────────────┐ │
│  │   Repositories (Data Access)   │ │
│  └────────────┬───────────────────┘ │
└───────────────┼─────────────────────┘
                │
        ┌───────▼────────┐
        │   PostgreSQL   │
        └────────────────┘
                │
     ┌──────────┴──────────┐
     │   External APIs      │
     │ - OpenWeather        │
     │ - FDA Drug           │
     │ - Mailgun            │
     │ - UI Avatars         │
     └─────────────────────┘
```

### Architectural Patterns
- **Layered Architecture** (Controller → Service → Repository)
- **DTO Pattern** (Request/Response separation)
- **Repository Pattern** (Data access abstraction)
- **Dependency Injection** (Spring IoC)

---

## 🚀 Project Setup

### Prerequisites
```bash
# Java 21+
java -version

# PostgreSQL 14+
psql --version

# Maven 3.8+ (or use ./mvnw wrapper)
mvn -version
```

### 1. Clone Repository
```bash
git clone <repository-url>
cd happy-health-clinic
```

### 2. Database Setup
```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create database
CREATE DATABASE happy_health_clinic;

-- Create user (optional)
CREATE USER clinic_admin WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE happy_health_clinic TO clinic_admin;
```

### 3. Configuration
```bash
# Copy configuration template
cp src/main/resources/application-local.properties.template src/main/resources/application-local.properties

# Edit with your values
nano src/main/resources/application-local.properties
```

---

## ⚙️ Configuration

### File: `application-local.properties`
```properties
# ===================================
# DATABASE CONFIGURATION
# ===================================
spring.datasource.url=jdbc:postgresql://localhost:5432/happy_health_clinic
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# ===================================
# SERVER CONFIGURATION
# ===================================
server.port=3001

# ===================================
# JWT CONFIGURATION
# ===================================
jwt.secret=YOUR_SECRET_KEY_HERE_MUST_BE_AT_LEAST_256_BITS_LONG
jwt.expiration=86400000

# ===================================
# EXTERNAL APIs (Optional - not required for basic functionality)
# ===================================

# OpenWeather API (for weather forecasts in appointments)
openweather.api.key=YOUR_OPENWEATHER_API_KEY
openweather.api.url=https://api.openweathermap.org/data/2.5/forecast

# FDA Drug API (for drug information in prescriptions)
fda.api.url=https://api.fda.gov/drug/label.json

# Mailgun API (for sending invoice emails)
mailgun.api.key=YOUR_MAILGUN_API_KEY
mailgun.domain=YOUR_MAILGUN_DOMAIN
mailgun.from.email=noreply@YOUR_DOMAIN.com
mailgun.from.name=Happy Health Clinic
```

### Obtaining API Keys (Optional)

| API | Registration | Free Tier |
|-----|--------------|-----------|
| **OpenWeather** | https://openweathermap.org/api | ✅ 1000 calls/day |
| **FDA Drug** | https://open.fda.gov | ✅ Unlimited (no key needed) |
| **Mailgun** | https://www.mailgun.com | ✅ 5000 emails/month |

**⚠️ Note:** External APIs are **optional**. The application works without them (related features will be disabled).

---

## ▶️ Running the Application

### Option 1: Maven Wrapper (Recommended)
```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

### Option 2: Installed Maven
```bash
mvn spring-boot:run
```

### Option 3: JAR File
```bash
# Build
./mvnw clean package

# Run
java -jar target/happy-health-clinic-0.0.1-SNAPSHOT.jar
```

### Verify Startup
```bash
# Application should be available at:
http://localhost:3001

# Test public endpoint:
curl http://localhost:3001/actuator/health
```

---

## 📚 API Documentation

### Base URL
```
http://localhost:3001
```

### Authentication

All APIs (except `/auth/*` and `/setup/*`) require JWT authentication.

**Required header:**
```
Authorization: Bearer <JWT_TOKEN>
```

### Main Endpoints

#### 🔐 Authentication
```http
POST   /auth/login           # User login
POST   /auth/register        # New patient registration
```

#### 👨‍⚕️ Doctors
```http
GET    /doctors              # List doctors (paginated)
GET    /doctors/{id}         # Doctor details
GET    /doctors/specialization/{specialization}  # Search by specialization
POST   /doctors              # Create doctor (ADMIN)
PUT    /doctors/{id}         # Update doctor (ADMIN)
DELETE /doctors/{id}         # Delete doctor (ADMIN)
```

#### 👔 Staff
```http
GET    /staff                # List staff (paginated)
GET    /staff/{id}           # Staff details
GET    /staff/role/{role}    # Search by role
POST   /staff                # Create staff (ADMIN)
PUT    /staff/{id}           # Update staff (ADMIN)
DELETE /staff/{id}           # Delete staff (ADMIN)
```

#### 👥 Patients
```http
GET    /patients             # List patients (paginated)
GET    /patients/{id}        # Patient details
GET    /patients/code/{code} # Search by patient code
PUT    /patients/{id}        # Update patient (ADMIN)
DELETE /patients/{id}        # Delete patient (ADMIN)
```

#### 📅 Appointments (+ OpenWeather API)
```http
GET    /appointments                    # List appointments (paginated)
GET    /appointments/{id}               # Appointment details
GET    /appointments/patient/{patientId}  # Patient appointments
GET    /appointments/doctor/{doctorId}    # Doctor appointments
GET    /appointments/status/{status}      # Search by status
GET    /appointments/date-range           # Search by date range
POST   /appointments                      # Create appointment (with weather)
PUT    /appointments/{id}                 # Update appointment
DELETE /appointments/{id}                 # Delete appointment (ADMIN)
```

#### 📋 Medical Records
```http
GET    /medical-records                 # List records (paginated)
GET    /medical-records/{id}            # Record details
GET    /medical-records/patient/{patientId}  # Patient records
GET    /medical-records/doctor/{doctorId}    # Doctor records
GET    /medical-records/date-range           # Search by date range
POST   /medical-records                      # Create record (DOCTOR)
PUT    /medical-records/{id}                 # Update record (DOCTOR)
DELETE /medical-records/{id}                 # Delete record (ADMIN)
```

#### 💊 Prescriptions (+ FDA Drug API)
```http
GET    /prescriptions                          # List prescriptions (paginated)
GET    /prescriptions/{id}                     # Prescription details
GET    /prescriptions/medical-record/{recordId}  # By medical record
GET    /prescriptions/patient/{patientId}        # By patient
GET    /prescriptions/patient/{patientId}/active # Active prescriptions
GET    /prescriptions/search?drugName={name}     # Search by drug name
POST   /prescriptions                            # Create prescription (with FDA)
PUT    /prescriptions/{id}                       # Update prescription
DELETE /prescriptions/{id}                       # Delete prescription (ADMIN)
```

#### 💰 Invoices (+ Mailgun Email)
```http
GET    /invoices                       # List invoices (paginated)
GET    /invoices/{id}                  # Invoice details
GET    /invoices/number/{number}       # Search by invoice number
GET    /invoices/patient/{patientId}   # Patient invoices
GET    /invoices/status/{status}       # Search by status
GET    /invoices/overdue               # Overdue invoices
GET    /invoices/total/{status}        # Total by status
POST   /invoices                       # Create invoice (with email)
PUT    /invoices/{id}                  # Update invoice (ADMIN)
PATCH  /invoices/{id}/pay              # Mark as paid
DELETE /invoices/{id}                  # Delete invoice (ADMIN)
```

---

## 📬 Postman Collection

### Import Collection

1. Open Postman
2. Click **Import**
3. Select file: `happy_health_clinic.postman_collection.json`
4. Import

### Variable Configuration

The collection includes pre-configured variables:
```javascript
baseUrl = http://localhost:3001
token = (auto-populated after login)
patientId = (auto-populated)
doctorId = (auto-populated)
// ... other auto-managed variables
```

### Recommended Workflow
```
1. Setup → Create Admin User
2. Authentication → Login (saves token)
3. Authentication → Register Random Patient (3-5 times)
4. Doctors → Create Random Doctor (2-3 times)
5. Staff → Create Random Staff (1-2 times)
6. Appointments → Create Appointment
7. Appointments → Mark as COMPLETED
8. Medical Records → Create Medical Record
9. Prescriptions → Create Prescription
10. Invoices → Create Invoice
```

### Collection Features

- ✅ **57 Complete Requests**
- ✅ **Randomized Data** for realistic testing
- ✅ **Auto-save IDs** between requests
- ✅ **Pre/Post-request Scripts** for automation
- ✅ **Detailed Console Logging**
- ✅ **Automatic Test Assertions**

---

## Database

### ER Schema

Below is the Entity–Relationship (ER) schema of the database, which provides a clear overview of the core entities and their relationships within the system.

![ER Schema](./er-schema.png)

The schema can also be viewed and explored interactively on drawSQL at the following link:  
[View ER Schema on drawSQL](https://drawsql.app/teams/epicode-university/diagrams/happy-health-clinic-system)


### Main Tables

| Table | Description | Relations |
|-------|-------------|-----------|
| `users` | Base users (Single Table Inheritance) | - |
| `patients` | Patients | → users |
| `employees` | Employees | → users |
| `doctors` | Doctors | → employees |
| `staff` | Administrative staff | → employees |
| `appointments` | Appointments | → patients, doctors |
| `medical_records` | Medical records | → patients, doctors, appointments |
| `prescriptions` | Prescriptions | → medical_records, patients |
| `invoices` | Invoices | → patients, appointments |

### Initialization
```sql
-- Tables are automatically created by Hibernate
-- with spring.jpa.hibernate.ddl-auto=create

-- Admin is created via endpoint /setup/create-admin
-- All other data via Postman Collection
```

---

## 🔒 Security

### JWT Authentication
```
1. User → POST /auth/login
2. Server → Validates credentials
3. Server → Generates JWT token
4. Client → Saves token
5. Client → Sends token in header: Authorization: Bearer <token>
6. Server → Validates token for each request
```

### Roles and Permissions

| Role | Access |
|------|--------|
| **ADMIN** | Full access to all resources |
| **DOCTOR** | Manage appointments, medical records, prescriptions |
| **PATIENT** | View own information, book appointments |
| **STAFF** | Manage appointments, view patients |

### Public Endpoints
```
/auth/login
/auth/register
/setup/create-admin (only if admin doesn't exist)
```

### Password Security

- Hashing with **BCrypt**
- Min 8 characters
- At least 1 uppercase letter
- At least 1 lowercase letter
- At least 1 number
- At least 1 special character

---

## 🌐 External APIs

### 1. OpenWeather API (Appointments)

**Functionality:**
- Weather forecast for appointment date
- Severe weather alerts
- Temperature and conditions

**Configuration:**
```properties
openweather.api.key=YOUR_KEY
```

**Behavior:**
- ✅ API available → Weather data included in response
- ❌ API unavailable → Appointment created without weather

---

### 2. FDA Drug API (Prescriptions)

**Functionality:**
- Drug information
- Side effects
- Contraindications

**Configuration:**
```properties
fda.api.url=https://api.fda.gov/drug/label.json
# No API key required
```

**Behavior:**
- ✅ Drug found → FDA data included
- ❌ Drug not found → Prescription created without FDA data

---

### 3. Mailgun API (Invoices)

**Functionality:**
- Send invoice email to patient
- Professional HTML template
- Delivery tracking

**Configuration:**
```properties
mailgun.api.key=YOUR_KEY
mailgun.domain=YOUR_DOMAIN
mailgun.from.email=noreply@{mailgun.domain}
```

**Behavior:**
- ✅ Email sent → Success logged
- ❌ Send error → Invoice created, error logged

---

### 4. UI Avatars (User Profiles)

**Functionality:**
- Automatic avatars for users
- Based on name/initials
- Colors by role

**Example:**
```
https://ui-avatars.com/api/?name=John+Doe&background=0D8ABC&color=fff
```

**Behavior:**
- Always available (public service)
- No configuration required

---

## 🧪 Testing

### Manual Testing (Postman)
```bash
# 1. Import collection
# 2. Execute in order:

Folder 0: Setup (1 request)
Folder 1: Authentication (2 requests)
Folder 2: Doctors (6 requests)
Folder 3: Staff (6 requests)
Folder 4: Patients (5 requests)
Folder 5: Appointments (9 requests)
Folder 6: Medical Records (8 requests)
Folder 7: Prescriptions (9 requests)
Folder 8: Invoices (11 requests)

Total: 57 requests
```

### Test Scenarios

#### Scenario 1: Registration and Login
```
1. Create Admin
2. Login as Admin
3. Register Patient
4. Login as Patient
```

#### Scenario 2: Appointment Booking
```
1. Login as Patient
2. Get All Doctors
3. Create Appointment
4. Get Appointment (verify weather)
```

#### Scenario 3: Complete Visit
```
1. Create Appointment (DOCTOR/ADMIN)
2. Mark as COMPLETED
3. Create Medical Record (DOCTOR)
4. Create Prescription (DOCTOR)
5. Create Invoice (ADMIN)
6. Verify Email Sent
```

---

## 📁 Project Structure
```
happy-health-clinic/
├── src/
│   ├── main/
│   │   ├── java/it/fpili/happy_health_clinic/
│   │   │   ├── controllers/     # REST Controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── request/     # Request DTOs
│   │   │   │   └── response/    # Response DTOs
│   │   │   ├── entities/        # JPA Entities
│   │   │   ├── enums/           # Enumerations
│   │   │   ├── exceptions/      # Custom Exceptions
│   │   │   ├── repositories/    # Spring Data Repositories
│   │   │   ├── security/        # JWT Filter & Utils
│   │   │   ├── services/        # Business Logic
│   │   │   └── HappyHealthClinicApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-local.properties.template
│   └── test/                    
├── pom.xml
├── happy_health_clinic.postman_collection.json
└── README.md
```

---

## 🎯 Implemented Features

### Core Features
- ✅ Multi-role user management (Admin, Doctor, Staff, Patient)
- ✅ JWT Authentication
- ✅ Role-based authorization
- ✅ Complete CRUD for all entities
- ✅ Data validation (Jakarta Validation)
- ✅ Centralized exception handling
- ✅ Pagination and sorting
- ✅ Search and filters

### External Integrations
- ✅ OpenWeather API (weather forecasts)
- ✅ FDA Drug API (drug information)
- ✅ Mailgun API (email notifications)
- ✅ UI Avatars API (user avatars)

### Advanced Features
- ✅ Single Table Inheritance (User → Patient/Employee)
- ✅ Joined Table Inheritance (Employee → Doctor/Staff)
- ✅ Cascade operations
- ✅ Soft deletes (via status)
- ✅ Auto-generated codes (Patient, Invoice)
- ✅ Business logic validation
- ✅ Date range queries
- ✅ Financial calculations

---

## 📝 Development Notes

### Code Conventions
- **Naming**: CamelCase for classes, camelCase for variables
- **Package structure**: By layer (controllers, services, repositories)
- **DTOs**: Separated into request/response
- **Lombok**: Used to reduce boilerplate

### Best Practices
- Repository Pattern for data access
- Service layer for business logic
- DTO Pattern for API contracts
- Exception handling with @RestControllerAdvice
- Validation with Bean Validation
- Security with Spring Security + JWT

---

## 👨‍💻 Author

**Filippo Pili**

**Project**: Happy Health Clinic

**Academic Year**: 2025/2026

---

## 📄 License

This project was developed for educational purposes.

---

## 🆘 Troubleshooting

### Issue: Database connection error
```bash
# Verify PostgreSQL is running
sudo systemctl status postgresql

# Verify credentials in application-local.properties
```

### Issue: Invalid JWT Token
```bash
# Verify jwt.secret in application-local.properties
# Must be at least 256 bits (32+ characters)
```

### Issue: External APIs not working
```bash
# External APIs are optional
# Verify API keys in application-local.properties
# App works without them
```

### Issue: Port 3001 already in use
```bash
# Change port in application-local.properties
server.port=8080

# Also update baseUrl in Postman Collection
```

---

**🎉 Happy Testing!**