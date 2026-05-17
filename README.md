# Java RestApi Logs Monitor
Java REST API with Kibana & Grafana - Log Tracing System

A complete observability solution for Java REST APIs that provides centralized log management with Kibana and metrics visualization with Grafana. This system allows you to trace, monitor, and analyze your application logs and performance metrics in real-time.
## 📋Table of Contents

### Architecture Overview
Prerequisites
Quick Start
Project Structure
Configuration Guide
Running the Application
Log Tracing with Kibana
Metrics Visualization with Grafana
API Endpoints
Docker Setup
Troubleshooting
Contributing

## 🏗 Architecture Overview

┌─────────────────┐     ┌──────────────┐     ┌─────────────┐     ┌──────────┐
│  Spring Boot    │────▶│   Logstash   │────▶│ Elasticsearch│────▶│  Kibana  │
│  REST API       │     │   (TCP:5044) │     │  (Port:9200) │     │(Port:5601)│
└─────────────────┘     └──────────────┘     └─────────────┘     └──────────┘
│                                                              
│ (Exposes /actuator/prometheus)                               
▼                                                              
┌─────────────────┐     ┌──────────────┐     ┌──────────┐
│   Prometheus    │────▶│   Grafana    │     │   Alert  │
│  (Port:9090)    │     │  (Port:3000) │     │ Manager │
└─────────────────┘     └──────────────┘     └──────────┘
▲                                              
│ (Scrapes every 15s)                          
└──────────────────────────────────────────────┘

## 🔧 Prerequisites
Java 17 or later
Maven 3.6+
Docker and Docker Compose (for ELK stack and Prometheus/Grafana)
8GB RAM minimum (16GB recommended for ELK stack)