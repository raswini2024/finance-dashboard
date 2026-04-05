# Finance Dashboard Backend

A backend system for a finance dashboard that supports user role management, financial record tracking, and summary analytics.

## Tech Stack

- Java 17
- Spring Boot 3.5
- Spring Security + JWT
- MySQL
- Spring Data JPA
- Lombok
- Maven

## Project Structure
```
src/main/java/com/finance/finance_dashboard/
├── config/         # JWT and Security configuration
├── controller/     # REST API endpoints
├── dto/            # Request and response objects
├── exception/      # Global error handling
├── model/          # Database entities
├── repository/     # Database queries
└── service/        # Business logic
```
## Roles and Access

| Role    | Access |
|---------|--------|
| ADMIN   | Full access - manage users, records, dashboard |
| ANALYST | View records and access dashboard summary |
| VIEWER  | View records and recent transactions only |

## Setup Instructions

### Prerequisites
- Java 17
- MySQL
- Maven

### Steps

1. Clone the repository
```bash
git clone <your-repo-url>
cd finance-dashboard
```

2. Create MySQL database
```sql
CREATE DATABASE finance_db;
```

3. Update `src/main/resources/application.properties`
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

4. Run the application
```bash
./mvnw spring-boot:run
```

Server starts on `http://localhost:8080`

## API Endpoints

### Auth
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | /api/auth/register | Public | Register new user |
| POST | /api/auth/login | Public | Login and get JWT token |

### Users
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | /api/users | ADMIN | Get all users |
| GET | /api/users/{id} | ADMIN | Get user by ID |
| PATCH | /api/users/{id}/status | ADMIN | Update user status |
| PATCH | /api/users/{id}/role | ADMIN | Update user role |
| DELETE | /api/users/{id} | ADMIN | Delete user |

### Transactions
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | /api/transactions | ADMIN, ANALYST | Create transaction |
| GET | /api/transactions | ALL | Get all transactions |
| GET | /api/transactions/{id} | ALL | Get transaction by ID |
| PUT | /api/transactions/{id} | ADMIN | Update transaction |
| DELETE | /api/transactions/{id} | ADMIN | Soft delete transaction |
| GET | /api/transactions/filter/type | ALL | Filter by type |
| GET | /api/transactions/filter/category | ALL | Filter by category |
| GET | /api/transactions/filter/date | ALL | Filter by date range |

### Dashboard
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | /api/dashboard/summary | ADMIN, ANALYST | Get financial summary |
| GET | /api/dashboard/recent | ALL | Get recent transactions |

## Authentication

All protected endpoints require a Bearer token in the request header:

Authorization: Bearer <your-jwt-token>

Get the token by calling the login endpoint.

## Key Design Decisions

- JWT based stateless authentication for scalable API access
- Role based access control using Spring Security @PreAuthorize
- Soft delete to preserve data integrity
- DTO pattern to separate API layer from database entities
- Global exception handler for consistent error responses
- BCrypt password hashing for secure credential storage

## Assumptions

- A user can have only one role at a time
- Soft deleted transactions are excluded from all queries and summaries
- JWT token expires after 24 hours
- All monetary values use BigDecimal for precision