# Spring Boot Application with ELK Stack Integration

A complete Spring Boot REST API application with centralized logging using ELK Stack (Elasticsearch, Logstash, Kibana) running in Docker containers.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [1. Clone the Repository](#1-clone-the-repository)
    - [2. Start ELK Stack](#2-start-elk-stack)
    - [3. Run Spring Boot Application](#3-run-spring-boot-application)
    - [4. Access Kibana Dashboard](#4-access-kibana-dashboard)
- [Configuration](#configuration)
- [Testing](#testing)
- [Log Flow](#log-flow)
- [Troubleshooting](#troubleshooting)
- [Project Structure](#project-structure)
- [Useful Commands](#useful-commands)
- [Contributing](#contributing)
- [License](#license)

## Overview

This project demonstrates how to integrate a Spring Boot application with the ELK Stack for centralized logging and log analysis. All logs from the Spring Boot application are automatically shipped to Elasticsearch via Logstash and visualized in Kibana.

## Architecture
┌─────────────────┐ ┌──────────────┐ ┌─────────────────┐ ┌─────────┐
│ Spring Boot │────▶│ Logstash │────▶│ Elasticsearch │────▶│ Kibana │
│ Application │ TCP │ Port 5000 │ │ Port 9200 │ │ Port 5601│
└─────────────────┘ └──────────────┘ └─────────────────┘ └─────────┘
│ │ │ │
│ │ │ │
▼ ▼ ▼ ▼
Console Logs JSON Format Indexed Data Visualization

text

## Prerequisites

- **Docker** and **Docker Compose** (for ELK Stack)
- **Java 11 or 17**
- **Gradle** (or use the wrapper)
- **Your favorite IDE** (IntelliJ, VS Code, Eclipse)

## Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| Spring Boot | 2.7+ / 3.x | REST API Application |
| Elasticsearch | 8.11.0 | Log storage and indexing |
| Logstash | 8.11.0 | Log processing and forwarding |
| Kibana | 8.11.0 | Log visualization |
| Logstash Logback Encoder | 8.1 | JSON log formatting |
| Gradle | 7.x / 8.x | Build tool |
| Docker | 20.10+ | Containerization |

## Getting Started

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd <project-directory>
2. Start ELK Stack

Start all ELK services using Docker Compose:

bash
# Start all containers in detached mode
docker-compose up -d

# Verify all containers are running
docker-compose ps

# View logs from all services
docker-compose logs -f
Expected output:

text
NAME           STATUS          PORTS
elasticsearch  Up             0.0.0.0:9200->9200/tcp
logstash       Up             0.0.0.0:5000->5000/tcp
kibana         Up             0.0.0.0:5601->5601/tcp
3. Run Spring Boot Application

bash
# Using Gradle wrapper
./gradlew bootRun

# Or build and run JAR
./gradlew build
java -jar build/libs/your-app-name.jar
4. Access Kibana Dashboard

Open your browser and navigate to: http://localhost:5601
Go to Menu (☰) → Stack Management → Data Views
Click Create data view
Enter:

Name: springboot-logs
Index pattern: springboot-logs-*
Click Next step → Select @timestamp as time field → Create data view
Go to Menu → Discover to view your logs


