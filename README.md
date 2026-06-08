# RabbitMQ Microservices Demo

A microservices demo using **Spring Boot**, **RabbitMQ**, and **MySQL** — featuring a Producer and Consumer service communicating via a message queue.

---

## Architecture

```
Producer (8080) ──▶ RabbitMQ (5672) ──▶ Consumer (8082)
     │                                        │
  MySQL (webapp)                       MySQL (webapp2)
```

1. User registers on the Producer
2. Producer saves the user to `webapp` DB and publishes a message to RabbitMQ
3. Consumer receives the message, saves it to `webapp2` DB, and displays it

---

## Tech Stack

| Layer       | Technology              |
|-------------|-------------------------|
| Backend     | Spring Boot (Java 17)   |
| Messaging   | RabbitMQ                |
| Database    | MySQL                   |
| Templates   | Thymeleaf               |
| Build       | Maven                   |
| Deployment  | Docker / Docker Compose |

---

## Quick Start (Docker)

```bash
git clone https://github.com/YOUR_USERNAME/rabbitmq-microservices-demo.git
cd rabbitmq-microservices-demo
docker-compose up --build
```

| Service              | URL                                      |
|----------------------|------------------------------------------|
| Producer (Register)  | http://localhost:8080                    |
| Consumer (Messages)  | http://localhost:8082                    |
| RabbitMQ Management  | http://localhost:15672 (guest / guest)   |

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

**3. Run services**
```bash
cd producer && mvn spring-boot:run
cd consumer && mvn spring-boot:run
```

---

## Project Structure

```
rabbitmq-app/
├── producer/
│   ├── src/main/java/iti/gov/producer/
│   │   ├── config/         # RabbitMQConfig.java
│   │   ├── controller/     # RegistrationController.java
│   │   ├── dto/            # UserRegisteredEvent.java
│   │   ├── entity/         # User.java
│   │   ├── repository/     # UserRepository.java
│   │   ├── service/        # UserService.java
│   │   └── ProducerApplication.java
│   ├── src/main/resources/
│   │   ├── templates/      # register.html, users.html
│   │   └── application.properties
│   ├── Dockerfile
│   └── pom.xml
├── consumer/
│   ├── src/main/java/iti/gov/consumer/
│   │   ├── config/         # RabbitMQConfig.java
│   │   ├── controller/     # MessageController.java
│   │   ├── dto/            # UserRegisteredEvent.java
│   │   ├── entity/         # Message.java
│   │   ├── repository/     # MessageRepository.java
│   │   ├── service/        # MessageService.java, UserRegistrationListener.java
│   │   └── ConsumerApplication.java
│   ├── src/main/resources/
│   │   ├── templates/      # messages.html
│   │   └── application.properties
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml
├── .gitignore
└── README.md
```


## Useful Commands

```bash
docker-compose up --build          # Build and start
docker-compose down                # Stop all containers
docker-compose logs -f             # Follow logs
docker system prune -a             # Clear Docker cache
docker-compose down -v             # Remove volumes
```

---

## API Endpoints

**Producer**
- `GET /` — Registration form
- `POST /register` — Submit registration
- `GET /users` — List all users

**Consumer**
- `GET /` — View all messages
- `GET /api/messages` — JSON message list

---

## License

Educational use only.
