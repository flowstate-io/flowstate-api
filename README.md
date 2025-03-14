# Flowstate API

## Overview

Flowstate's API is built using SpringBoot and PostgreSQL. The service includes

- User Registration
- Login
- Email Verification using OTP (One-Time Passcode)
- Refresh Token Handling
- Logout Functionality

## Features

- **User Registration:** Register users with a username, email, and password.
- **User Login:** Authenticate users and generate JWT tokens for secure access.
- **JWT Authentication:** Protect endpoints by requiring valid JWT tokens.
- **Email Verification:** Verify user emails using an OTP sent to the registered email address.
- **Resend Verification Email:** Allows users to request a new email verification link if needed.
- **Refresh Token:** Use refresh tokens to obtain new access tokens without re-authenticating.
- **Logout:** Revoke refresh tokens to invalidate the session on logout.

## Prerequisites

- Java 21 or higher.
- Docker
- Maven
- OpenSSL

## Generating an RSA Key Pair with OpenSSL

To generate a new RSA key pair, use the following commands. Ensure that the keys are stored securely.

### Change directory to where the keys should be stored:
```
cd src/main/resources/jwt
```
### Generate private key:
```
openssl genpkey -algorithm RSA -out app.key -outform PEM
```
### Generate public key:
```
openssl rsa -pubout -in app.key -out app.pub
```

## Running Locally
### Database Setup
The database will be created and configured automatically and during application start-up using spring-docker-compose dependency.

### Steps to Run
**Ensure Docker is running before starting the application**
#### 1. Clone the Repository
```
git clone https://github.com/flowstate-io/flowstate-api.git
cd flowstate-api
```
#### 2. Run the application
```
mvn clean install
mvn spring-boot:run
```

## API Endpoints

### Authentication
