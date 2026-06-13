# RabbitMQ Microservices Demo

A microservices demo using **Spring Boot**, **RabbitMQ**, **MySQL**, **Eureka Service Discovery**, and **API Gateway**.

---

## Architecture

```
                        ┌─────────────────────────────────────┐
                        │         Eureka Server :8761         │
                        │         (Service Registry)          │
                        └──────────────┬──────────────────────┘
                                       │
                 ┌─────────────────────┼──────────────────────┐
                 ↓                     ↓                       ↓
         Frontend :8081         Producer :8082          Consumer :8083
                 └─────────────────────┼──────────────────────┘
                                       │
                        ┌──────────────┴──────────────────────┐
                        │        API Gateway :8080            │
                        └──────────────┬──────────────────────┘
                                       ↑
                                    Client
```

**Flow:**
1. Client hits **API Gateway** on port `8080`
2. Gateway forwards to the correct service (`/frontend/**`, `/producer/**`, `/consumer/**`)
3. User registers via **Frontend** → Feign calls **Producer** directly (Eureka-resolved)
4. Producer saves user to `webapp` DB and publishes event to **RabbitMQ**
5. Consumer receives the event and saves it to `webapp2` DB

---

## Tech Stack

| Layer            | Technology                        |
|------------------|-----------------------------------|
| Frontend         | Spring Boot + Thymeleaf           |
| Producer         | Spring Boot (REST API)            |
| Consumer         | Spring Boot (Queue Listener)      |
| Messaging        | RabbitMQ                          |
| Database         | MySQL (2 separate DBs)            |
| Service Registry | Eureka Server (Spring Cloud)      |
| API Gateway      | Spring Cloud Gateway (WebFlux)    |
| Service Calls    | OpenFeign (Eureka-resolved)       |
| Build            | Maven                             |
| Deployment       | Docker / Docker Compose           |

---

## Quick Start (Docker)

```bash
git clone https://github.com/YOUR_USERNAME/rabbitmq-microservices-demo.git
cd rabbitmq-microservices-demo

# 1 — build all jars first
cd eureka-server && mvn clean package -DskipTests && cd ..
cd api-gateway   && mvn clean package -DskipTests && cd ..
cd producer      && mvn clean package -DskipTests && cd ..
cd consumer      && mvn clean package -DskipTests && cd ..
cd frontend      && mvn clean package -DskipTests && cd ..

# 2 — start everything
docker-compose up --build
```

| Service              | URL                                      |
|----------------------|------------------------------------------|
| **API Gateway**      | http://localhost:8080                    |
| Frontend (via GW)    | http://localhost:8080/frontend/          |
| Producer (via GW)    | http://localhost:8080/producer/register  |
| Consumer (via GW)    | http://localhost:8080/consumer/          |
| Eureka Dashboard     | http://localhost:8761                    |
| RabbitMQ Dashboard   | http://localhost:15672 (guest / guest)   |
| Frontend (direct)    | http://localhost:8081                    |
| Producer (direct)    | http://localhost:8082                    |
| Consumer (direct)    | http://localhost:8083                    |

---

## Running Locally (Without Docker)

**1. Create databases**
```sql
CREATE DATABASE webapp;
CREATE DATABASE webapp2;
```

**2. Start RabbitMQ**
```bash
rabbitmq-server
```

**3. Start services in this order** (separate terminals)
```bash
cd eureka-server && mvn spring-boot:run   # Port 8761
cd producer      && mvn spring-boot:run   # Port 8082
cd consumer      && mvn spring-boot:run   # Port 8083
cd frontend      && mvn spring-boot:run   # Port 8081
cd api-gateway   && mvn spring-boot:run   # Port 8080
```

---

## Project Structure

```
rabbitmq-app/
├── eureka-server/                        # Service Registry (Port 8761)
│   ├── src/main/java/iti/gov/eureka_server/
│   │   └── EurekaServerApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── Dockerfile
│   └── pom.xml
│
├── api-gateway/                          # API Gateway (Port 8080)
│   ├── src/main/java/iti/gov/api_gateway/
│   │   ├── ApiGatewayApplication.java
│   │   └── GatewayConfig.java
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/                             # UI Service (Port 8081)
│   ├── src/main/java/iti/gov/frontend/
│   │   ├── client/                       # ProducerClient.java (Feign)
│   │   ├── controller/                   # FrontendController.java
│   │   ├── entity/                       # User.java
│   │   └── FrontendApplication.java
│   ├── src/main/resources/
│   │   ├── templates/                    # register.html
│   │   └── application.properties
│   ├── Dockerfile
│   └── pom.xml
│
├── producer/                             # REST API Service (Port 8082)
│   ├── src/main/java/iti/gov/producer/
│   │   ├── config/                       # RabbitMQConfig.java
│   │   ├── controller/                   # RegistrationController.java
│   │   ├── dto/                          # UserRegisteredEvent.java
│   │   ├── entity/                       # User.java
│   │   ├── repository/                   # UserRepository.java
│   │   ├── service/                      # UserService.java
│   │   └── ProducerApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── Dockerfile
│   └── pom.xml
│
├── consumer/                             # Queue Listener Service (Port 8083)
│   ├── src/main/java/iti/gov/consumer/
│   │   ├── config/                       # RabbitMQConfig.java
│   │   ├── controller/                   # MessageController.java
│   │   ├── dto/                          # UserRegisteredEvent.java
│   │   ├── entity/                       # Message.java
│   │   ├── repository/                   # MessageRepository.java
│   │   ├── service/                      # MessageService.java, UserRegistrationListener.java
│   │   └── ConsumerApplication.java
│   ├── src/main/resources/
│   │   ├── templates/                    # messages.html
│   │   └── application.properties
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml
├── .gitignore
└── README.md
```

---

## API Endpoints

**Frontend — Port 8081**
| Method | Endpoint              | Description                          |
|--------|-----------------------|--------------------------------------|
| GET    | `/frontend/`          | Registration form                    |
| POST   | `/frontend/register`  | Register user (forwards to Producer) |

**Producer — Port 8082**
| Method | Endpoint                  | Description               |
|--------|---------------------------|---------------------------|
| POST   | `/producer/register`      | Save user & publish event |
| GET    | `/users`                  | List all registered users |

**Consumer — Port 8083**
| Method | Endpoint | Description          |
|--------|----------|----------------------|
| GET    | `/`      | View messages (HTML) |

---

## RabbitMQ Configuration

| Component     | Name                |
|---------------|---------------------|
| Exchange      | `Direct-Exchange`   |
| Exchange Type | Direct              |
| Queue         | `user-registration` |
| Routing Key   | `register`          |

```
Producer ──▶ Direct-Exchange ──[register]──▶ user-registration queue ──▶ Consumer
```

---

## Screenshots


### 1. Exchange & Queue Binding — RabbitMQ Management
`Direct-Exchange` configured as a direct exchange and bound to the `user-registration` queue using routing key `register`.

<img width="828" height="673" alt="image" src="https://github.com/user-attachments/assets/95aa3a81-ea86-46b2-8396-0bd2d5a7852d" />

---

### 2. User Registration Form
The registration form served by the Frontend at `http://localhost:8080/frontend/`.

<img width="677" height="584" alt="image" src="https://github.com/user-attachments/assets/82171246-382b-4922-aa1b-23810422538b" />

---

### 3. Successful Registration
Confirmation screen after a user is successfully registered and the event is published to RabbitMQ.

<img width="678" height="229" alt="image" src="https://github.com/user-attachments/assets/54d28039-34b5-42f5-a6d6-f7d6b882766e" />

---

### 4. Queue with 1 Message Ready — RabbitMQ Management
The `user-registration` queue showing 1 message ready, confirming the Producer successfully published the event.

<img width="789" height="291" alt="image" src="https://github.com/user-attachments/assets/a12407a3-263c-45ba-ad93-9905d75d1c14" />

---

### 5. Consumer Receiving the Message — CLI
The Consumer service logs showing the message was received from the queue and processed by `UserRegistrationListener`.

<img width="1217" height="472" alt="image" src="https://github.com/user-attachments/assets/050d84cc-4a8f-489a-84cd-dda224f6db25" />

---

## Useful Commands

```bash
docker-compose up --build          # Build and start all services
docker-compose up -d               # Start in background
docker-compose down                # Stop all services
docker-compose down -v             # Stop and remove volumes
docker-compose logs -f             # Follow all logs
docker-compose logs -f producer    # Logs for a specific service
docker system prune -a             # Clear Docker cache
```

---

## License

Educational use only.
