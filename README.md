```markdown
# Spring Boot Application with ELK Stack & Grafana

## Access URLs

After starting the application and ELK stack, you can access the following services:

| Service | URL | Default Credentials |
|---------|-----|---------------------|
| Spring Boot Test API | http://localhost:8080/api/test/logs | No authentication |
| Kibana Dashboard | http://localhost:5601 | No authentication (default setup) |
| Grafana Dashboard | http://localhost:3000 | Username: `admin` / Password: `admin` |

## How to Setup

### Step 1: Start ELK Stack with Grafana

Run the following command in your project root where `docker-compose.yml` is located:

```bash
docker-compose up -d
```

This starts Elasticsearch, Logstash, Kibana, and Grafana containers.

### Step 2: Run Spring Boot Application

```bash
./gradlew bootRun
```

Or use:

```bash
java -jar build/libs/your-app-name.jar
```

### Step 3: Configure Kibana (First Time Only)

1. Open browser and go to: http://localhost:5601
2. Click Menu (☰) → Stack Management → Data Views
3. Click **Create data view**
4. Enter:
    - Name: `springboot-logs`
    - Index pattern: `springboot-logs-*`
5. Click Next step
6. Select `@timestamp` as time field
7. Click Create data view
8. Go to Menu → Discover to view logs

### Step 4: Configure Grafana (First Time Only)

1. Open browser and go to: http://localhost:3000
2. Login with:
    - Username: `admin`
    - Password: `admin`
3. Change password when prompted (or skip)
4. Add Elasticsearch data source:
    - Click Menu (☰) → Connections → Data sources
    - Click **Add data source**
    - Search and select **Elasticsearch**
    - Configure:
        - Name: `Spring Boot Logs`
        - URL: `http://elasticsearch:9200`
        - Index name: `springboot-logs-*`
        - Time field name: `@timestamp`
    - Click **Save & test**
5. View logs:
    - Click Menu → Explore
    - Select `Spring Boot Logs` data source
    - Click **Run query**

## Generate Test Logs

To generate sample logs and verify everything is working, call the test endpoint:

```bash
curl http://localhost:8080/api/test/logs
```

Or open in browser: http://localhost:8080/api/test/logs

## View Your Logs

- **In Kibana:** http://localhost:5601 → Menu → Discover
- **In Grafana:** http://localhost:3000 → Menu → Explore

## Troubleshooting

### Port Already in Use
If you see "address already in use" error, change the port in `docker-compose.yml` from 5000:5000 to 5001:5000 (or any other free port).

### Grafana Shows Plugin Error
Remove the line `GF_INSTALL_PLUGINS` from the Grafana environment section in `docker-compose.yml`. Elasticsearch data source is built-in and doesn't need a plugin.

### No Logs Appearing
- Wait 10-20 seconds for logs to propagate
- Check if Spring Boot is running: http://localhost:8080/api/test/logs
- Check Docker containers: `docker-compose ps`

## Local vs Cloud Deployment

**Local Setup:**
- Use `localhost` for all URLs
- Ensure Docker is installed locally

**Cloud Setup:**
- Replace `localhost` with your server's IP address or domain
- Open required ports (5601, 3000, 8080, 5000, 9200) in firewall
- Update Elasticsearch URL in Grafana to use internal Docker hostname: `http://elasticsearch:9200`

## Stopping Everything

```bash
docker-compose down
```

To also remove all data:

```bash
docker-compose down -v
```

## Requirements

- Docker and Docker Compose
- Java 11 or 17
- Minimum 4GB RAM for Docker
```