---

# Quiz App

A full-stack Quiz application built with **Spring Boot** (backend), **React + Vite** (frontend), and **MariaDB** (database). The application is fully Dockerized using **Docker Compose**.

---

## Features

* Create, Read, Update, Delete quizzes and questions
* REST API backend with Spring Boot
* Frontend with React + Vite
* CORS enabled for cross-origin frontend access
* Database persistence using MariaDB

---

## Project Structure

```
Quiz-App/
├── backend/          # Spring Boot project
│   ├── Dockerfile
│   └── target/QuizApp-1-0.0.1-SNAPSHOT.jar
├── frontend/         # React + Vite project
│   ├── Dockerfile
│   └── package.json
├── docker-compose.yml
└── README.md
```

---

## Prerequisites

* Docker >= 24
* Docker Compose >= 2.20
* (Optional) Postman or a browser for testing APIs

---

## Docker Setup

The project uses **three services**:

1. **db** → MariaDB database
2. **backend** → Spring Boot API
3. **frontend** → React + Vite application served via Nginx

---

## Step 1: Clone the project

```bash
git clone <your-repo-url>
cd Quiz-App
```

---

## Step 2: Docker Compose Build & Up

Run the following command from the root project folder:

```bash
docker-compose up --build
```

* This will build **backend** and **frontend** images and start all three services.
* The first time may take a few minutes.

---

## Step 3: Access the services

| Service            | URL                                                                            | Notes                                                                  |
| ------------------ | ------------------------------------------------------------------------------ | ---------------------------------------------------------------------- |
| Frontend           | [http://localhost:5173](http://localhost:5173)                                 | React app served via Nginx                                             |
| Backend API        | [http://localhost:8081/quiz/getallquiz](http://localhost:8081/quiz/getallquiz) | Spring Boot REST API; mapped from container port 8080 → host 8081      |
| Database (MariaDB) | 127.0.0.1:3307                                                                 | Use MySQL client/Postman; root password = `root`, database = `quiz-db` |

---

## Step 4: React Frontend API Configuration

* Frontend communicates with backend via environment variable:

```env
# frontend/.env
VITE_API_URL=http://localhost:8081
```

* In code:

```js
fetch(`${import.meta.env.VITE_API_URL}/quiz/getallquiz`)
```

> This ensures the frontend works correctly when accessed from your browser outside Docker.

---

## Step 5: Stopping the project

```bash
docker-compose down
```

* Stops and removes containers
* Use `docker-compose down -v` to also remove volumes (database data)

---

## Notes

* **Database Credentials** in `docker-compose.yml`:

```yaml
environment:
  MYSQL_ROOT_PASSWORD: root
  MYSQL_DATABASE: quiz-db
```

* Spring Boot `application.properties` for Docker:

```properties
spring.datasource.url=jdbc:mariadb://db:3306/quiz-db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
server.address=0.0.0.0
server.port=8080
```

* **Networking**: Docker Compose automatically creates a network.

  * Containers use service names (`db`, `backend`) to communicate internally.
  * Browser/Postman must use `localhost` + mapped ports (frontend 5173, backend 8081).

---

## Recommended Workflow

1. Make changes to React frontend → rebuild container if needed.
2. Make changes to Spring Boot backend → rebuild `backend` container.
3. Database persists in Docker volume `db_data`, so data is preserved between container restarts.

---

