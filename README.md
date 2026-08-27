# Banking Management System

A backend banking management system built using **Java 21** and **Spring Boot**.
The project implements core banking operations such as customer management, account management, deposits, withdrawals, fund transfers, transaction history with filtering and pagination, idempotent request handling, concurrency-safe transaction processing, validation, exception handling, and automated API testing.

---

# Version 2

Version 2 builds on the core banking functionality from V1 and focuses on **production-oriented backend concepts** — the areas explicitly identified as gaps at the end of V1.

## V2 Includes

- Fund transfers between accounts
- Idempotency key support for safe request retries
- Optimistic/pessimistic concurrency control on account balances
- Race-condition prevention under simultaneous transactions
- Transaction history filtering
- Transaction history pagination
- Dedicated concurrency and idempotency test suites
- Everything from V1: customer management, account management, deposits, withdrawals, validation, global exception handling, MySQL persistence

---

## Architecture

The application follows a layered Spring Boot architecture:

![System Architecture](./docs/banking%20mangement%20system%20system%20architecture.png)

### Concurrency and Idempotency Flow

Fund transfers and repeated requests are protected using idempotency keys and locking, illustrated below:

![Concurrency and Idempotency Architecture](./docs/concurrency%20and%20idempotency%20architecture.png)

### Database Schema

![Database Schema](./docs/database%20schemas.png)

### V1 Architecture (for reference)

![V1 Architecture](./docs/v1-architecture.png)

---

## Layers

### Controller Layer

Responsible for:

- Receiving HTTP requests
- Request validation
- Returning HTTP responses

Main controllers:

- `CustomerController`
- `AccountController`
- `TransactionController`

### Service Layer

Contains the application's business logic, including concurrency-safe balance updates and idempotency checks.

Main services:

- `CustomerService`
- `AccountService`
- `TransactionService`

### Repository Layer

Handles database operations using Spring Data JPA.

Main repositories:

- `CustomerRepository`
- `AccountRepository`
- `TransactionRepository`
- `IdempotencyKeyRepository`

### Database Layer

MySQL is used for persistent data storage.

---

# Domain Model

```text
Customer
    |
    | 1 : N
    v
Account
    |
    | 1 : N
    v
Transaction

Transaction
    |
    | 1 : 1 (optional)
    v
IdempotencyKey
```

- A customer can have multiple accounts.
- An account can have multiple transactions.
- A transaction may be linked to an idempotency key to guarantee it is processed exactly once, even if the request is retried.

---

# Features

## Customer Management

- Create a customer
- Retrieve all customers
- Prevent duplicate customer email addresses
- Validate customer input

## Account Management

- Create a bank account
- Retrieve an account by account number
- Retrieve all accounts
- Associate an account with a customer
- Support account status management
- Maintain account balance

## Transaction Management

- Deposit money
- Withdraw money
- **Transfer money between accounts**
- Maintain transaction records
- Check account balance before withdrawal
- Prevent withdrawal when balance is insufficient
- Retrieve transaction history for an account
- **Filter transaction history** (e.g. by type, date range, status)
- **Paginate transaction history** for large result sets

## Concurrency Control (New in V2)

Simultaneous requests against the same account (e.g. two withdrawals or a withdrawal racing a transfer) are handled safely to prevent lost updates and overdrafts.

- Optimistic locking on `Account` balances to detect concurrent modification
- Retry / conflict handling exposed as a clear API error rather than silent data corruption
- Dedicated test suites (`ConcurrencyTest`, `ConcurrencyExceptionHandlerTest`) simulate concurrent requests against the same account to verify correctness

## Idempotency (New in V2)

Clients can safely retry a transaction request (e.g. after a network timeout) without the risk of it being processed twice.

- Each transaction-mutating request can carry an idempotency key
- Repeated requests with the same key return the original result instead of creating a duplicate transaction
- `IdempotencyKey` entity and `IdempotencyKeyRepository` persist key state
- Combined concurrency + idempotency behavior is verified in `IdempotencyConcurrencyTest` and `IdempotencyTest`

## Validation

Request validation is implemented using Jakarta Bean Validation.

Examples include:

- Required fields
- Valid email format
- Positive transaction amounts
- Valid account information

## Exception Handling

The application uses a global exception handler with `@RestControllerAdvice`.

Handled cases include:

- Customer already exists
- Customer not found
- Account not found
- Account not active
- Insufficient balance
- Concurrent modification / conflicting update
- Validation errors

Example error response:

```json
{
  "status": 404,
  "message": "Account Not Found",
  "timestamp": "2026-08-26T12:00:00"
}
```

Example concurrency conflict response:

```json
{
  "status": 409,
  "message": "The account was modified concurrently. Please retry the request.",
  "timestamp": "2026-08-26T12:00:00"
}
```

---

# Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Backend framework |
| Spring MVC | REST API development |
| Spring Data JPA | Database access |
| Hibernate | ORM, optimistic locking (`@Version`) |
| MySQL | Relational database |
| Maven | Build and dependency management |
| JUnit | Automated testing |
| MockMvc | API integration testing |
| IntelliJ IDEA / VS Code | Development environment |
| Postman | API testing |

---

# API Documentation

Complete API documentation is available in:

[API Documentation](./docs/API.md)

The API documentation contains:

- HTTP method
- Endpoint
- Request body
- Response body
- HTTP status codes
- Error responses
- Example requests
- Idempotency key usage for transaction endpoints
- Transaction filtering and pagination query parameters

---

# Database Configuration

The application uses **MySQL**.

## Requirements

Make sure MySQL is installed and running.

Default local configuration:

```text
Host: localhost
Port: 3306
Database: banking_system
Username: root
```

## Create the Database

Open MySQL Workbench or MySQL Command Line Client and run:

```sql
CREATE DATABASE banking_system;
```

Verify the database:

```sql
SHOW DATABASES;
```

You should see:

```text
banking_system
```

---

# Application Configuration

After cloning the repository, open:

```text
src/main/resources/application.properties
```

Configure your local MySQL connection:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_system
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Replace:

```text
YOUR_MYSQL_PASSWORD
```

with your local MySQL password.

> **Important:** Do not commit your real MySQL password to GitHub.

For production environments, database credentials should be provided using environment variables or a secure configuration mechanism.

---

# Running the Application

## Requirements

Install the following:

- Java 21
- MySQL
- Git
- IntelliJ IDEA or VS Code

Maven does not need to be installed separately because this project includes the Maven Wrapper.

---

## 1. Clone the Repository

Clone the repository:

```bash
git clone https://github.com/prem-kumar3405/BANKING-MANGEMENT-SYSYEM.git
```

Move into the project directory:

```bash
cd BANKING-MANGEMENT-SYSYEM
```

The project root should contain:

```text
banking-management-system/
├── .mvn/
├── docs/
├── src/
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

## 2. Configure MySQL

Make sure MySQL is running.

Create the database:

```sql
CREATE DATABASE banking_system;
```

Then configure your MySQL credentials in:

```text
src/main/resources/application.properties
```

---

## 3. Run Using IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Select **File → Open**.
3. Select the cloned project folder.
4. IntelliJ should detect `pom.xml` as a Maven project.
5. Wait for Maven dependencies to finish downloading.
6. Open the main Spring Boot application class (`BankingManagementSystemApplication`).
7. Click the green **Run** button.

The application will start at:

```text
http://localhost:8080
```

---

## 4. Run Using VS Code

1. Open VS Code.
2. Select **File → Open Folder**.
3. Select the cloned project folder.
4. Install the Java and Spring Boot extensions if required.
5. Wait for Maven dependencies to load.
6. Open the main Spring Boot application class.
7. Click **Run** above the `main()` method.

The application will start at:

```text
http://localhost:8080
```

---

## 5. Run Using Maven Wrapper

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

After successful startup:

```text
http://localhost:8080
```

---

# Testing

Version 2 includes automated API integration tests using:

- JUnit
- Spring Boot Test
- MockMvc

The tests verify actual HTTP API behavior, including concurrent and idempotent request handling.

## Customer API Tests

- Successful customer creation
- Duplicate email
- Invalid customer request

## Account API Tests

- Successful account creation
- Customer not found
- Invalid account request

## Transaction API Tests

- Successful deposit
- Successful withdrawal
- Successful transfer between accounts
- Insufficient balance
- Negative deposit amount
- Deposit with non-existing account
- Withdrawal with non-existing account
- Transaction history filtering
- Transaction history pagination

## Concurrency & Idempotency Tests

- `ConcurrencyTest` — simulates simultaneous transactions against the same account and verifies the final balance is correct
- `ConcurrencyExceptionHandlerTest` — verifies conflicting concurrent updates return a proper error response instead of corrupting data
- `IdempotencyTest` — verifies a repeated request with the same idempotency key does not create a duplicate transaction
- `IdempotencyConcurrencyTest` — verifies idempotency guarantees hold even when duplicate requests arrive concurrently

All V2 API and concurrency tests are passing successfully.

---

# Running Tests

## IntelliJ IDEA

1. Open:

```text
src/test/java
```

2. Right-click the test package or test class.
3. Select **Run Tests**.

## VS Code

Open the test class and click **Run Test** above the test method.

## Windows

```powershell
.\mvnw.cmd test
```

## Linux / macOS

```bash
./mvnw test
```

---

# Using the API

Once the application is running, the APIs can be tested using Postman.

Base URL:

```text
http://localhost:8080
```

Example — create a customer:

```http
POST http://localhost:8080/api/customers
```

Example — transfer funds with an idempotency key:

```http
POST http://localhost:8080/api/transactions/transfer
Idempotency-Key: 3f29a1c4-7b3e-4d3a-9c2e-2f4b8e6a1d90
Content-Type: application/json

{
  "fromAccountNumber": "ACC1001",
  "toAccountNumber": "ACC1002",
  "amount": 500.00
}
```

Example — paginated and filtered transaction history:

```http
GET http://localhost:8080/api/accounts/ACC1001/transactions?type=WITHDRAWAL&page=0&size=10
```

For complete endpoint information, request examples, response examples, and status codes:

[API Documentation](docs/API.md)

---

# Project Structure

```text
banking-management-system/
│
├── .mvn/
│
├── docs/
│   ├── API.md
│   ├── banking mangement system system architecture.png
│   ├── concurrency and idempotency architecture.png
│   ├── database schemas.png
│   └── v1-architecture.png
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/prem/banking_management_system/
│   │   │       ├── accounts/
│   │   │       │   ├── Account.java
│   │   │       │   ├── AccountController.java
│   │   │       │   ├── AccountRepository.java
│   │   │       │   ├── AccountService.java
│   │   │       │   ├── AccountStatus.java
│   │   │       │   ├── AccountType.java
│   │   │       │   └── transactions/
│   │   │       │       ├── DepositRequest.java
│   │   │       │       ├── IdempotencyKey.java
│   │   │       │       ├── IdempotencyKeyRepository.java
│   │   │       │       ├── Transaction.java
│   │   │       │       ├── TransactionController.java
│   │   │       │       ├── TransactionRepository.java
│   │   │       │       ├── TransactionResponse.java
│   │   │       │       ├── TransactionService.java
│   │   │       │       ├── TransactionStatus.java
│   │   │       │       ├── TransactionType.java
│   │   │       │       ├── TransferRequest.java
│   │   │       │       ├── TransferResponse.java
│   │   │       │       └── WithdrawalRequest.java
│   │   │       │
│   │   │       ├── customers/
│   │   │       │   ├── Customer.java
│   │   │       │   ├── CustomerController.java
│   │   │       │   ├── CustomerRepository.java
│   │   │       │   └── CustomerService.java
│   │   │       │
│   │   │       ├── dtos/
│   │   │       │   ├── AccountRequest.java
│   │   │       │   ├── AccountResponse.java
│   │   │       │   ├── CustomerRequest.java
│   │   │       │   └── CustomerResponse.java
│   │   │       │
│   │   │       └── exceptions/
│   │   │           ├── AccountNotActiveException.java
│   │   │           ├── AccountNotFoundException.java
│   │   │           ├── ApiErrorResponse.java
│   │   │           ├── CustomerAlreadyExistsExpection.java
│   │   │           ├── CustomerNotFoundException.java
│   │   │           ├── GlobalExceptionHandler.java
│   │   │           └── InsufficientBalanceException.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/com/prem/banking_management_system/
│           ├── BankingManagementSystemApplicationTests.java
│           ├── ConcurrencyExceptionHandlerTest.java
│           ├── ConcurrencyTest.java
│           ├── IdempotencyConcurrencyTest.java
│           ├── IdempotencyTest.java
│           ├── accounts/
│           │   ├── AccountControllerTest.java
│           │   └── transaction/
│           │       ├── TransactionControllerTest.java
│           │       ├── TransactionFilterTest.java
│           │       ├── TransactionPaginationTest.java
│           │       └── TransferTest.java
│           └── customers/
│               └── CustomerControllerTest.java
│
├── .gitattributes
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

# Version Status

**Version 1 — Complete.** Core banking functionality: REST APIs, MySQL persistence, business validation, global exception handling, automated API integration tests.

**Version 2 — Complete.** Production-oriented backend concepts: fund transfers, concurrency-safe transaction processing, idempotent request handling, transaction filtering and pagination, expanded automated test coverage.

---

# Future Improvements

Planned for future versions:

- Authentication and authorization (JWT-based access control)
- Role-based access for admin vs. customer operations
- Rate limiting on transaction endpoints
- Dockerized deployment with `docker-compose`
- CI/CD pipeline (GitHub Actions)
- Deployment to a cloud environment (AWS)
- Caching layer (Redis) for high-read endpoints
- Audit logging for all account and transaction changes

---

## Author

**Prem Kumar**
Backend Developer | Java | Spring Boot

[Portfolio](https://portfolio-prem3405.vercel.app/) •
[LeetCode](https://leetcode.com/u/prem_cse_03/) •
[LinkedIn](https://www.linkedin.com/in/premkumarrajamani/)