# Banking Management System API

## Base URL

```text
http://localhost:8080
```

This document describes the REST APIs implemented in Version 1 of the Banking Management System.

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

If the request contains invalid data:

**HTTP Status: `400 Bad Request`**

Example:

```json
{
  "name": "",
  "email": "invalid-email",
  "phone": ""
}
```

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
| `initialBalance` | Required and minimum value is `0.00` |

### Success Response

**HTTP Status: `201 Created`**

```json
{
  "id": 1,
  "accountNumber": "ACCXXXXXXXXXXXX",
  "customerId": 1,
  "accountType": "SAVINGS",
  "balance": 1000.00,
  "status": "ACTIVE",
  "createAt": "2026-08-26T12:00:00"
}
```

### Customer Not Found

If the specified customer does not exist:

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
  "message": "Account Not  found",
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

**HTTP Status: `404 Not Found`**

```json
{
  "status": 404,
  "message": "Account is status is not active",
  "timestamp": "2026-08-26T12:00:00"
}
```

---

## 8. Get Transactions by Account

Returns all transactions associated with an account.

### Endpoint

```http
GET /api/transactions/account/{accountId}
```

### Example

```http
GET /api/transactions/account/1
```

### Success Response

**HTTP Status: `200 OK`**

```json
[
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
]
```

### Account Not Found

If the account does not exist:

**HTTP Status: `404 Not Found`**

```json
{
  "status": 404,
  "message": "Account not found",
  "timestamp": "2026-08-26T12:00:00"
}
```

---

# HTTP Status Codes

| Status Code | Meaning |
|---|---|
| `200 OK` | Request completed successfully |
| `201 Created` | Resource or transaction created successfully |
| `400 Bad Request` | Invalid request or business rule violation |
| `404 Not Found` | Requested customer or account does not exist |

---

# Error Response

The application uses a common error response structure:

```json
{
  "status": 400,
  "message": "Error message",
  "timestamp": "2026-08-26T12:00:00"
}
```

The response is represented by the `ApiErrorResponse` record:

```java
public record ApiErrorResponse(
    int status,
    String message,
    LocalDateTime timestamp
) {
}
```

---

# API Testing

The APIs can be tested manually using Postman.

Automated API integration tests are implemented using:

- JUnit
- Spring Boot Test
- MockMvc

The V1 tests cover:

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