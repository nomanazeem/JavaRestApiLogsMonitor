plugins {
    java
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "JavaRestApiLogsMonitor"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // ✅ ADD THIS - Validation dependency
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Logstash encoder for JSON formatting
    implementation("net.logstash.logback:logstash-logback-encoder:8.1")

    // Distributed tracing (optional but recommended)
    //implementation("io.micrometer:micrometer-tracing-bridge-brave")
    //implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    // Metrics
    //implementation("io.micrometer:micrometer-registry-prometheus")

    // For better JSON handling
    //implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    //implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}