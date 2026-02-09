# 🛒 Mini E-Commerce Microservices Project

A beginner-to-intermediate **microservices-based e-commerce backend** built using **Spring Boot**, **Eureka Service Discovery**, **MySQL**, and **Docker Compose**.

This project demonstrates real-world backend concepts such as service discovery, inter-service communication, database per service, and containerized deployment.

---

## 🚀 Features

### ✅ Product Service

* Create, Read, Update, Delete products
* Manage product stock
* Decrease stock atomically when orders are placed
* Uses its own MySQL database

### ✅ Order Service

* Place orders for products
* Communicates with Product Service using **Feign Client**
* Validates stock before order creation
* Automatically marks orders as `CREATED` or `FAILED`
* Uses its own MySQL database

### ✅ Service Discovery

* **Eureka Server** for service registration and discovery
* Product and Order services register dynamically

### ✅ Database

* **MySQL** with persistent storage
* One database per microservice (best practice)
* Data persists across container restarts

### ✅ Dockerized Setup

* All services run using **Docker Compose**
* Single command to start the entire system

---

## 🧱 Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Cloud Netflix Eureka
* OpenFeign
* MySQL 8
* Docker & Docker Compose
* Lombok
* Maven

---

## 📂 Project Structure

```
mini-ecommerce/
│
├── eureka-server/
│   └── Eureka Service Discovery
│
├── product-service/
│   └── Product CRUD + Stock Management
│
├── order-service/
│   └── Order Management (Feign → Product Service)
│
├── docker-compose.yml
└── README.md
```

---

## ⚙️ Architecture Overview

```
        ┌──────────────┐
        │  Eureka      │
        │  Server      │
        │  (8761)      │
        └──────┬───────┘
               │
   ┌───────────┴───────────┐
   │                       │
┌──▼─────────┐      ┌──────▼────────┐
│ Product    │      │ Order          │
│ Service    │◄────►│ Service        │
│ (8081)     │ Feign│ (8082)         │
└──┬─────────┘      └──────┬────────┘
   │                        │
┌──▼─────────┐      ┌──────▼────────┐
│ MySQL      │      │ MySQL          │
│ productdb  │      │ orderdb        │
└────────────┘      └───────────────┘
```

---

## ▶️ How to Run the Project

### 1️⃣ Prerequisites

* Java 17
* Maven
* Docker Desktop (running)

---

### 2️⃣ Build JARs (one time)

```bash
cd product-service
mvn -DskipTests clean package

cd ../order-service
mvn -DskipTests clean package

cd ../eureka-server
mvn -DskipTests clean package
```

---

### 3️⃣ Start All Services (Docker Compose)

From the root folder:

```bash
docker compose up --build
```

---

### 4️⃣ Verify Services

* **Eureka Dashboard**
  [http://localhost:8761](http://localhost:8761)
  → PRODUCT-SERVICE and ORDER-SERVICE should be **UP**

* **Product Service**
  [http://localhost:8081/products](http://localhost:8081/products)

* **Order Service**
  [http://localhost:8082/orders](http://localhost:8082/orders)

---

## 🧪 API Testing (Postman)

### ➕ Create Product

```
POST /products
```

```json
{
  "name": "Mouse",
  "price": 500,
  "stock": 10
}
```

---

### 📦 Place Order

```
POST /orders
```

```json
{
  "productId": 1,
  "quantity": 2
}
```

* If stock is sufficient → `CREATED`
* If stock is insufficient → `FAILED`
* Product stock is reduced automatically

---

## 🗄️ Database Access (MySQL Workbench)

### Product Database

* Host: `localhost`
* Port: `3307`
* DB: `productdb`
* User: `root`
* Password: `root`

### Order Database

* Host: `localhost`
* Port: `3308`
* DB: `orderdb`
* User: `root`
* Password: `root`

---

## 🧠 Key Concepts Demonstrated

* Microservices architecture
* Service discovery with Eureka
* Feign client communication
* Database per service pattern
* Atomic stock update
* Dockerized backend
* Persistent data using volumes

---

## 🧩 Future Enhancements (Optional)

* API Gateway
* Resilience4j (Circuit Breaker)
* Swagger / OpenAPI
* JWT Authentication
* Frontend (React / Angular)

---

## 👨‍💻 Author

**Mahesh Medaboina**
Java Backend Developer (Spring Boot & Microservices)
