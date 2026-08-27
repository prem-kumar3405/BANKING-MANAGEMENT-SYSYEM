# Banking Management System API

## Base URL

```text
http://localhost:8080
```

This document describes the REST APIs implemented in the Banking Management System.

The project is developed in multiple versions:

- **Version 1** — Core banking functionality
- **Version 2** — Transaction safety, transfer, concurrency, pagination, filtering, and idempotency

---

# Table of Contents

- [Customer APIs](#customer-apis)
- [Account APIs](#account-apis)
- [Transaction APIs](#transaction-apis)
- [Transfer API](#transfer-api)
- [Transaction History](#transaction-history)
- [Pagination](#pagination)
- [Transaction Filtering](#transaction-filtering)
- [Idempotency](#idempotency)
- [Concurrency and Transaction Safety](#concurrency-and-transaction-safety)
- [Validation](#validation)
- [Error Handling](#error-handling)
- [HTTP Status Codes](#http-status-codes)
- [Testing](#testing)
- [API Summary](#api-summary)
- [Version Overview](#version-overview)

---

# Customer APIs

## 1. Create Customer

Creates a new customer.

### Endpoint

```http
POST /api/customers
```

### Request Headers

```http
Content-Type: application/json
```

### Request Body

```json
{
  "name": "Prem",
  "email": "prem@example.com",
  "phone": "9876543210"
}
```

### Validation Rules

| Field | Rules |
|---|---|
| `name` | Required and cannot be blank |
| `email` | Required, cannot be blank, and must be a valid email |
| `phone` | Required and cannot be blank |

### Success Response

**HTTP Status: `201 Created`**

```json
{
  "id": 1,
  "name": "Prem",
  "email": "prem@example.com",
  "phone": "9876543210",
  "createdAt": "2026-08-26T12:00:00"
}
```

### Duplicate Email

If the email is already registered:

**HTTP Status: `400 Bad Request`**

```json
{
  "status": 400,
  "message": "Email already registered",
  "timestamp": "2026-08-26T12:00:00"
}
```

### Validation Error

Example invalid request:

```json
{
  "name": "",
  "email": "invalid-email",
  "phone": ""
}
```

**HTTP Status: `400 Bad Request`**

---

## 2. Get All Customers

Returns all customers.

### Endpoint

```http
GET /api/customers/getAllCustomers
```

### Success Response

**HTTP Status: `200 OK`**

```json
[
  {
    "id": 1,
    "name": "Prem",
    "email": "prem@example.com",
    "phone": "9876543210",
    "createdAt": "2026-08-26T12:00:00"
  }
]
```

---

# Account APIs

## 3. Create Account

Creates a new account for an existing customer.

### Endpoint

```http
POST /api/accounts
```

### Request Headers

```http
Content-Type: application/json
```

### Request Body

```json
{
  "customerId": 1,
  "accountType": "SAVINGS",
  "initialBalance": 1000.00
}
```

### Validation Rules

| Field | Rules |
|---|---|
| `customerId` | Required |
| `accountType` | Required |
| `initialBalance` | Required and must be `0.00` or greater |

### Success Response

**HTTP Status: `201 Created`**

```json
{
  "id": 1,
  "accountNumber": "ACC123456789ABC",
  "customerId": 1,
  "accountType": "SAVINGS",
  "balance": 1000.00,
  "status": "ACTIVE",
  "createAt": "2026-08-26T12:00:00"
}
```

### Customer Not Found

**HTTP Status: `404 Not Found`**

```json
{
  "status": 404,
  "message": "Customer Not found",
  "timestamp": "2026-08-26T12:00:00"
}
```

---

## 4. Get Account by Account Number

Returns an account using its account number.

### Endpoint

```http
GET /api/accounts/{accountNumber}
```

### Example

```http
GET /api/accounts/ACC123456789ABC
```

### Success Response

**HTTP Status: `200 OK`**

```json
{
  "id": 1,
  "accountNumber": "ACC123456789ABC",
  "customerId": 1,
  "accountType": "SAVINGS",
  "balance": 1000.00,
  "status": "ACTIVE",
  "createAt": "2026-08-26T12:00:00"
}
```

### Account Not Found

**HTTP Status: `404 Not Found`**

```json
{
  "status": 404,
  "message": "Account Not Found",
  "timestamp": "2026-08-26T12:00:00"
}
```

---

## 5. Get All Accounts

Returns all accounts.

### Endpoint

```http
GET /api/accounts
```

### Success Response

**HTTP Status: `200 OK`**

```json
[
  {
    "id": 1,
    "accountNumber": "ACC123456789ABC",
    "customerId": 1,
    "accountType": "SAVINGS",
    "balance": 1000.00,
    "status": "ACTIVE",
    "createAt": "2026-08-26T12:00:00"
  }
]
```

---

# Transaction APIs

## 6. Deposit

Deposits money into an account.

### Endpoint

```http
POST /api/transactions/deposit
```

### Request Headers

```http
Content-Type: application/json
```

### Request Body

```json
{
  "accountId": 1,
  "amount": 2000.00
}
```

### Validation Rules

| Field | Rules |
|---|---|
| `accountId` | Required |
| `amount` | Required and minimum value is `0.01` |

### Success Response

**HTTP Status: `201 Created`**

```json
{
  "id": 1,
  "accountId": 1,
  "transactionType": "DEPOSIT",
  "amount": 2000.00,
  "status": "SUCCESS",
  "createdAt": "2026-08-26T12:00:00"
}
```

### Account Not Found

**HTTP Status: `404 Not Found`**

```json
{
  "status": 404,
  "message": "Account not found",
  "timestamp": "2026-08-26T12:00:00"
}
```

### Invalid Amount

An amount below `0.01` is rejected.

**HTTP Status: `400 Bad Request`**

Example:

```json
{
  "accountId": 1,
  "amount": -500.00
}
```

---

## 7. Withdrawal

Withdraws money from an account.

### Endpoint

```http
POST /api/transactions/withdraw
```

### Request Headers

```http
Content-Type: application/json
```

### Request Body

```json
{
  "accountId": 1,
  "amount": 500.00
}
```

### Validation Rules

| Field | Rules |
|---|---|
| `accountId` | Required |
| `amount` | Required and minimum value is `0.01` |

### Success Response

**HTTP Status: `201 Created`**

```json
{
  "id": 2,
  "accountId": 1,
  "transactionType": "WITHDRAWAL",
  "amount": 500.00,
  "status": "SUCCESS",
  "createdAt": "2026-08-26T12:05:00"
}
```

### Account Not Found

**HTTP Status: `404 Not Found`**

```json
{
  "status": 404,
  "message": "Account Not found",
  "timestamp": "2026-08-26T12:00:00"
}
```

### Insufficient Balance

If the account balance is lower than the withdrawal amount:

**HTTP Status: `400 Bad Request`**

```json
{
  "status": 400,
  "message": "Insufficient balance",
  "timestamp": "2026-08-26T12:00:00"
}
```

### Inactive Account

If the account is not active:

**HTTP Status: `400 Bad Request`**

```json
{
  "status": 400,
  "message": "Account is status is not active",
  "timestamp": "2026-08-26T12:00:00"
}
```

---

# Transfer API

## 8. Transfer Money

Transfers money from one account to another account.

The transfer operation:

1. Validates the source and destination accounts.
2. Ensures both accounts are active.
3. Checks the source account balance.
4. Deducts money from the source account.
5. Adds money to the destination account.
6. Executes the operation inside a database transaction.

### Endpoint

```http
POST /api/transactions/transfer
```

### Request Headers

```http
Content-Type: application/json
```

### Request Body

```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 1000.00
}
```

### Success Response

**HTTP Status: `201 Created`**

```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 1000.00,
  "status": "SUCCESS"
}
```

### Source Account Not Found

**HTTP Status: `404 Not Found`**

```json
{
  "status": 404,
  "message": "Source account not found",
  "timestamp": "2026-08-26T12:00:00"
}
```

### Destination Account Not Found

**HTTP Status: `404 Not Found`**

```json
{
  "status": 404,
  "message": "Destination account not found",
  "timestamp": "2026-08-26T12:00:00"
}
```

### Insufficient Balance

**HTTP Status: `400 Bad Request`**

```json
{
  "status": 400,
  "message": "Insufficient balance",
  "timestamp": "2026-08-26T12:00:00"
}
```

### Same Account Transfer

A transfer where the source and destination are the same account is rejected.

**HTTP Status: `400 Bad Request`**

```json
{
  "status": 400,
  "message": "Source and destination accounts must be different",
  "timestamp": "2026-08-26T12:00:00"
}
```

---

# Transaction History

## 9. Get Transactions by Account

Returns transaction history for an account.

Version 2 supports:

- Pagination
- Sorting
- Transaction type filtering

### Endpoint

```http
GET /api/transactions/account/{accountId}
```

### Basic Request

```http
GET /api/transactions/account/1
```

### Success Response

**HTTP Status: `200 OK`**

```json
{
  "content": [
    {
      "id": 1,
      "accountId": 1,
      "transactionType": "DEPOSIT",
      "amount": 2000.00,
      "status": "SUCCESS",
      "createdAt": "2026-08-26T12:00:00"
    },
    {
      "id": 2,
      "accountId": 1,
      "transactionType": "WITHDRAWAL",
      "amount": 500.00,
      "status": "SUCCESS",
      "createdAt": "2026-08-26T12:05:00"
    }
  ],
  "empty": false,
  "first": true,
  "last": true,
  "number": 0,
  "numberOfElements": 2,
  "size": 20,
  "totalElements": 2,
  "totalPages": 1
}
```

---

# Pagination

Transaction history supports pagination.

## Query Parameters

| Parameter | Description | Example |
|---|---|---|
| `page` | Zero-based page number | `0` |
| `size` | Number of transactions per page | `10` |
| `sort` | Field and direction used for sorting | `createAt,desc` |

### Example

```http
GET /api/transactions/account/1?page=0&size=5
```

### Sorting

Transactions can be sorted using:

```http
GET /api/transactions/account/1?page=0&size=5&sort=createAt,desc
```

Example:

```text
sort=createAt,desc
```

means:

```text
Sort by created time
        ↓
Newest transaction first
```

### Paginated Response

```json
{
  "content": [
    {
      "id": 20,
      "accountId": 1,
      "transactionType": "DEPOSIT",
      "amount": 2000.00,
      "status": "SUCCESS",
      "createdAt": "2026-08-26T12:31:18"
    },
    {
      "id": 19,
      "accountId": 1,
      "transactionType": "WITHDRAWAL",
      "amount": 1000.00,
      "status": "SUCCESS",
      "createdAt": "2026-08-26T12:31:18"
    }
  ],
  "first": true,
  "last": false,
  "number": 0,
  "size": 5,
  "totalElements": 33,
  "totalPages": 7
}
```

---

# Transaction Filtering

Transaction history can be filtered by transaction type.

Supported transaction types:

```text
DEPOSIT
WITHDRAWAL
```

### Deposit Transactions

```http
GET /api/transactions/account/1?type=DEPOSIT
```

### Withdrawal Transactions

```http
GET /api/transactions/account/1?type=WITHDRAWAL
```

### Filtering with Pagination

```http
GET /api/transactions/account/1?type=DEPOSIT&page=0&size=5
```

### Filtering with Pagination and Sorting

```http
GET /api/transactions/account/1?type=DEPOSIT&page=0&size=5&sort=createAt,desc
```

### Example Response

```json
{
  "content": [
    {
      "id": 20,
      "accountId": 1,
      "transactionType": "DEPOSIT",
      "amount": 2000.00,
      "status": "SUCCESS",
      "createdAt": "2026-08-26T12:31:18"
    },
    {
      "id": 18,
      "accountId": 1,
      "transactionType": "DEPOSIT",
      "amount": 2000.00,
      "status": "SUCCESS",
      "createdAt": "2026-08-26T12:30:45"
    }
  ],
  "totalElements": 10,
  "totalPages": 2
}
```

---

# Idempotency

Version 2 introduces idempotency for money transfers.

Idempotency prevents the same transfer request from being processed multiple times when a client retries a request.

## Idempotency-Key Header

The client sends a unique idempotency key:

```http
Idempotency-Key: abc-123
```

### Example Request

```http
POST /api/transactions/transfer
Content-Type: application/json
Idempotency-Key: abc-123
```

Request body:

```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 1000.00
}
```

---

## First Request

The first request with the idempotency key is processed normally.

```text
Client
   |
   | Idempotency-Key: abc-123
   v
Check Idempotency Key
   |
   | Key does not exist
   v
Process Transfer
   |
   v
Store Idempotency Key
   |
   v
Return Response
```

---

## Duplicate Request

If the same idempotency key is sent again:

```text
Client
   |
   | Idempotency-Key: abc-123
   v
Check Idempotency Key
   |
   | Key already exists
   v
Do not process transfer again
   |
   v
Return existing result
```

### Example

```text
Request 1
    |
    | abc-123
    v
Transfer ₹1000
    |
    v
Success


Request 2
    |
    | abc-123
    v
Already processed
    |
    v
Do not transfer ₹1000 again
```

The same idempotency key must not result in the money movement being executed twice.

---

# Concurrency and Transaction Safety

Version 2 introduces concurrency handling because multiple requests can attempt to update the same account simultaneously.

For example:

```text
Account Balance = ₹1000

Request A → Withdraw ₹800
Request B → Withdraw ₹800
```

Without concurrency protection, both requests could read the same balance before either update is committed.

This can produce an incorrect account state.

---

## Database Transaction

Money movement operations are executed inside a database transaction.

For a transfer:

```text
Start Transaction
       |
       v
Read Source Account
       |
       v
Read Destination Account
       |
       v
Validate Accounts
       |
       v
Validate Balance
       |
       v
Debit Source Account
       |
       v
Credit Destination Account
       |
       v
Commit Transaction
```

If an exception occurs:

```text
Exception
    |
    v
Rollback Transaction
```

This prevents a transfer from partially completing.

---

# Optimistic Locking

Account updates use optimistic locking to detect concurrent modifications.

Conceptually, the account entity contains:

```java
@Version
private Long version;
```

The version value allows the persistence layer to detect whether another transaction modified the account before the current transaction attempted to update it.

### Example

```text
Request A
    |
    | Read version = 5
    v
Update Account
    |
    v
Version = 6


Request B
    |
    | Read version = 5
    v
Update Account
    |
    v
Version mismatch
    |
    v
Concurrency conflict
```

This prevents one transaction from silently overwriting another transaction's changes.

---

# Concurrent Integration Testing

Version 2 contains tests specifically designed to verify concurrent operations.

The tests use multiple threads to execute operations simultaneously.

Conceptually:

```text
                Account
                   |
        +----------+----------+
        |          |          |
        v          v          v
    Request A  Request B  Request C
        |          |          |
        +----------+----------+
                   |
                   v
          Database Transactions
                   |
                   v
          Consistent Account State
```

The test suite verifies that concurrent operations do not result in an incorrect account balance.

---

# Transaction Rollback

Transfer operations are atomic.

For example:

```text
Source Balance      = ₹5000
Destination Balance = ₹2000

Transfer = ₹1000
```

Expected result:

```text
Source Account      = ₹4000
Destination Account = ₹3000
```

If the destination account cannot be found:

```text
Source Account
      |
      | Transfer
      v
Destination Account
      |
      X
Not Found
```

The transaction is rolled back.

```text
Transfer Failure
      |
      v
Rollback
      |
      v
Original Account State
```

No partial transfer should remain in the database.

---

# Validation

The application uses Jakarta Bean Validation for request validation.

Validation includes:

- Required customer fields
- Valid email format
- Required account information
- Positive transaction amounts
- Required account IDs
- Valid transfer information

Invalid requests are rejected before business processing.

---

# Error Handling

The application uses centralized exception handling with:

```java
@RestControllerAdvice
```

The common error response is represented by:

```java
public record ApiErrorResponse(
    int status,
    String message,
    LocalDateTime timestamp
) {
}
```

---

## Common Error Response

```json
{
  "status": 400,
  "message": "Error message",
  "timestamp": "2026-08-26T12:00:00"
}
```

---

# Common Exceptions

The application handles cases including:

- Customer already exists
- Customer not found
- Account not found
- Account not active
- Insufficient balance
- Invalid request data
- Invalid transfer
- Concurrent transaction conflicts
- Validation errors

---

# HTTP Status Codes

| Status Code | Meaning |
|---|---|
| `200 OK` | Request completed successfully |
| `201 Created` | Resource or transaction created successfully |
| `400 Bad Request` | Invalid request or business rule violation |
| `404 Not Found` | Requested customer or account does not exist |
| `409 Conflict` | Concurrent update or resource conflict, when mapped by the exception handler |

---

# API Testing

The application uses automated integration testing with:

- JUnit
- Spring Boot Test
- MockMvc
- Spring Data JPA

---

## Version 1 Tests

V1 tests cover:

- Customer creation
- Duplicate customer email
- Customer validation
- Account creation
- Customer not found
- Account validation
- Deposit
- Withdrawal
- Insufficient balance
- Negative transaction amount
- Deposit with non-existing account
- Withdrawal with non-existing account

---

## Version 2 Tests

V2 introduces additional tests for backend correctness.

### Transfer Tests

- Successful transfer
- Transfer rollback
- Invalid destination account

### Pagination Tests

- Transaction pagination
- Page size validation
- Page number validation
- Transaction sorting

### Filtering Tests

- Filter deposit transactions
- Filter withdrawal transactions
- Pagination with filtering

### Concurrency Tests

- Concurrent withdrawals
- Concurrent transaction conflicts
- Database consistency

### Idempotency Tests

- Same idempotency key should not transfer twice
- Concurrent idempotency requests
- Duplicate request protection

---

# Example API Workflow

A typical banking workflow can be represented as:

```text
1. Create Customer
        |
        v
2. Create Account
        |
        v
3. Deposit Money
        |
        v
4. Withdraw Money
        |
        v
5. Transfer Money
        |
        v
6. View Transaction History
        |
        v
7. Filter Transactions
        |
        v
8. Paginate Transactions
```

---

# API Summary

## Customer APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/customers` | Create customer |
| `GET` | `/api/customers/getAllCustomers` | Get all customers |

---

## Account APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/accounts` | Create account |
| `GET` | `/api/accounts/{accountNumber}` | Get account by account number |
| `GET` | `/api/accounts` | Get all accounts |

---

## Transaction APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/transactions/deposit` | Deposit money |
| `POST` | `/api/transactions/withdraw` | Withdraw money |
| `POST` | `/api/transactions/transfer` | Transfer money |
| `GET` | `/api/transactions/account/{accountId}` | Get account transactions |

---

# Version 2 Capabilities

Version 2 extends the core banking backend with:

```text
                    Version 2
                        |
        +---------------+---------------+
        |               |               |
        v               v               v
    Transfer       Concurrency      Idempotency
        |               |               |
        v               v               v
   Atomic Money     Optimistic       Duplicate
     Movement        Locking        Protection
        |
        +---------------+---------------+
                        |
                        v
              Transaction History
                        |
                +-------+-------+
                |               |
                v               v
             Filtering      Pagination
```

The main goal of Version 2 is to move beyond basic CRUD operations and demonstrate backend correctness under concurrent and repeated requests.

---

# Version Overview

## Version 1 — Core Backend

Version 1 focuses on:

- Java 21
- Spring Boot
- REST APIs
- Customer management
- Account management
- Deposits
- Withdrawals
- Transaction history
- Validation
- Exception handling
- MySQL persistence
- Integration testing

---

## Version 2 — Backend Correctness

Version 2 adds:

- Account-to-account transfers
- Database transactions
- Transaction rollback
- Concurrency handling
- Optimistic locking
- Concurrent integration tests
- Idempotency
- Duplicate request protection
- Transaction filtering
- Pagination
- Sorting

---

## Version 3 — Production Engineering

Future production-oriented improvements can include:

- Authentication
- JWT
- Authorization
- Role-based access control
- Docker
- CI/CD
- Database migrations
- Observability
- Structured logging
- Metrics
- Performance optimization
- Deployment

---

# Project Goal

The Banking Management System started as a basic banking CRUD application and evolved into a backend system focused on correctness.

The main engineering concerns demonstrated by Version 2 are:

```text
                Banking Operation
                       |
         +-------------+-------------+
         |             |             |
         v             v             v
    Transaction    Concurrency   Idempotency
     Atomicity       Safety      Duplicate Safety
         |             |             |
         +-------------+-------------+
                       |
                       v
              Consistent Database
```

The goal is to ensure that financial operations remain atomic, consistent, and safe even when requests are concurrent or repeated.