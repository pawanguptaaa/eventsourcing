# Spring Boot Event Sourcing with Redis

A simple e-commerce shopping cart application built using the Event Sourcing pattern with Spring Boot and Redis. This project demonstrates how to use an append-only log of events (stored in Redis) as the single source of truth for an application's state.

## 🚀 Key Concepts

* **Event Sourcing:** Instead of storing the current state of the shopping cart, this application stores a sequence of events (`ADD_ITEM`, `REMOVE_ITEM`) that led to the current state.
* **Redis as an Event Store:** Redis's `LIST` data structure is used to maintain an immutable, chronological log of events for each user's shopping cart. The `RPUSH` command appends new events, and `LRANGE` is used to retrieve the full history for state reconstruction.
* **State Reconstruction:** The application rebuilds the current state of the cart on demand by "replaying" all the events in the log. This provides a complete audit trail and allows for advanced features like temporal queries (e.g., "what was the cart's state last week?").

## 🛠️ Technology Stack

* **Backend:** Spring Boot (Java 17)
* **Database:** Redis (as the event store)
* **Dependencies:**
    * `Spring Web` for creating the REST API.
    * `Spring Data Redis` for seamless interaction with Redis.
* **Build Tool:** Maven

## 📋 Prerequisites

Before you run this application, make sure you have the following installed:

* **Java Development Kit (JDK) 17 or higher:** [Download from Oracle](https://www.oracle.com/java/technologies/downloads/) or use a package manager like Homebrew.
* **Apache Maven:** Typically bundled with modern IDEs like IntelliJ or VS Code.
* **Redis Server:** You can run Redis locally using Docker or Homebrew.

### Running Redis with Docker (Recommended)

If you have Docker installed, you can start a Redis instance with a single command:

```bash
docker run --name my-redis -p 6379:6379 -d redis
