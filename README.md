# RabbitMQ Microservices Demo

A microservices demo using **Spring Boot**, **RabbitMQ**, and **MySQL** — with three services: Frontend, Producer, and Consumer.

---

## Architecture

```
Frontend (8081) ──▶ Producer (8080) ──▶ RabbitMQ (5672) ──▶ Consumer (8082)
                         │                                          │
                   MySQL (webapp)                           MySQL (webapp2)
```

**Flow:**
1. User registers via **Frontend** → sends REST request to **Producer**
2. Producer saves user to `webapp` DB and publishes a message to RabbitMQ
3. Consumer receives the message and saves it to `webapp2` DB
4. User views processed messages at the **Consumer** page

---

## Tech Stack

| Layer      | Technology                  |
|------------|-----------------------------|
| Frontend   | Spring Boot + Thymeleaf     |
| Producer   | Spring Boot (REST API)      |
| Consumer   | Spring Boot (Queue Listener)|
| Messaging  | RabbitMQ                    |
| Database   | MySQL (2 separate DBs)      |
| Build      | Maven                       |
| Deployment | Docker / Docker Compose     |

---

## Quick Start (Docker)

```bash
git clone https://github.com/YOUR_USERNAME/rabbitmq-microservices-demo.git
cd rabbitmq-microservices-demo
docker-compose up --build
```

| Service             | URL                                    |
|---------------------|----------------------------------------|
| Frontend (UI)       | http://localhost:8081                  |
| Producer (API)      | http://localhost:8080                  |
| Consumer (Messages) | http://localhost:8082                  |
| RabbitMQ Dashboard  | http://localhost:15672 (guest / guest) |

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

**3. Run all three services** (separate terminals)
```bash
cd frontend  && mvn spring-boot:run   # Port 8081
cd producer  && mvn spring-boot:run   # Port 8080
cd consumer  && mvn spring-boot:run   # Port 8082
```

---

## Project Structure

```
rabbitmq-app/
├── frontend/                         # UI Service (Port 8081)
│   ├── src/main/java/iti/gov/frontend/
│   │   ├── controller/               # FrontendController.java
│   │   ├── entity/                   # User.java
│   │   └── FrontendApplication.java
│   ├── src/main/resources/
│   │   ├── templates/                # register.html
│   │   └── application.properties
│   ├── Dockerfile
│   └── pom.xml
│
├── producer/                         # REST API Service (Port 8080)
│   ├── src/main/java/iti/gov/producer/
│   │   ├── config/                   # RabbitMQConfig.java
│   │   ├── controller/               # RegistrationController.java
│   │   ├── dto/                      # UserRegisteredEvent.java
│   │   ├── entity/                   # User.java
│   │   ├── repository/               # UserRepository.java
│   │   ├── service/                  # UserService.java
│   │   └── ProducerApplication.java
│   ├── src/main/resources/
│   │   ├── templates/                # users.html
│   │   └── application.properties
│   ├── Dockerfile
│   └── pom.xml
│
├── consumer/                         # Queue Listener Service (Port 8082)
│   ├── src/main/java/iti/gov/consumer/
│   │   ├── config/                   # RabbitMQConfig.java
│   │   ├── controller/               # MessageController.java
│   │   ├── dto/                      # UserRegisteredEvent.java
│   │   ├── entity/                   # Message.java
│   │   ├── repository/               # MessageRepository.java
│   │   ├── service/                  # MessageService.java, UserRegistrationListener.java
│   │   └── ConsumerApplication.java
│   ├── src/main/resources/
│   │   ├── templates/                # messages.html
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
| Method | Endpoint    | Description                          |
|--------|-------------|--------------------------------------|
| GET    | `/`         | Registration form                    |
| POST   | `/register` | Register user (forwards to Producer) |

**Producer — Port 8080**
| Method | Endpoint             | Description               |
|--------|----------------------|---------------------------|
| POST   | `/producer/register` | Save user & publish event |
| GET    | `/users`             | List all registered users |

**Consumer — Port 8082**
| Method | Endpoint | Description          |
|--------|----------|----------------------|
| GET    | `/`      | View messages (HTML) |

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
