 `README.md` specifically optimized for your **Ubuntu Server** deployment. added the requested comment for Prometheus, and organized it for better readability.

---

# Spring Boot Hello - Deployment Guide

This repository contains a Spring Boot application. Follow the steps below to build and deploy the application on an **Ubuntu 22.04/24.04 LTS** server.

## 1. Pre-requisites

Ensure your system is up to date and the necessary tools are installed.

### Install Git & Maven

```bash
sudo apt update
sudo apt install git maven -y

```

### Install Java 17 (Ubuntu)

Spring Boot 2.6.4 and your configuration require Java 17.

```bash
# Install OpenJDK 17
sudo apt install openjdk-17-jdk -y

# Verify the version
java -version

# If multiple versions exist, set Java 17 as default:
sudo update-alternatives --config java

```

> **Jenkins Note:** If integrating with Jenkins, set the Git executable path to `/usr/bin/git` in the Global Tool Configuration.

---

## 2. Clone the Repository

```bash
git clone https://github.com/vickydevo/springboot-hello.git
cd springboot-hello

```

---

## 3. Build the Application

Use Maven to compile the code and package it into an executable JAR file.

```bash
mvn clean install

```

---

## 4. Deploy & Manage the Application

### Start the Application (Foreground)

Use this for testing to see logs in real-time:

```bash
cd target
java -jar gs-spring-boot-0.1.0.jar

```

### Start the Application (Background)

Use `nohup` to keep the application running after you close the terminal session:

```bash
nohup java -jar gs-spring-boot-0.1.0.jar > app_output.log 2>&1 &

```

### Useful Management Commands

| Action | Command |
| --- | --- |
| **Check if running** | `ps aux | grep java` |
| **View logs** | `tail -f app_output.log` |
| **Stop by Job ID** | `jobs` then `kill %1` |
| **Stop by Process ID** | `kill -9 <PID>` |

---

## 5. Accessing the Application

Once started, the application is accessible at the following endpoints:

* **Home Page:** `http://<YOUR_PUBLIC_IP>:8081`
* **Actuator Health:** `http://<YOUR_PUBLIC_IP>:8081/actuator/health`
* **Prometheus Metrics:** `http://<YOUR_PUBLIC_IP>:8081/actuator/prometheus`  Look for your custom chaos metrics: chaos_errors_seconds_count and chaos_leak_seconds_count
*  http://54.87.142.250:8081/chaos/leak
<img width="623" height="213" alt="Image" src="https://github.com/user-attachments/assets/f04c628e-174f-4e21-821f-ba2840a58a4c" />
* http://54.87.142.250:8081/chaos/error
<img width="986" height="392" alt="Image" src="https://github.com/user-attachments/assets/91f5c6c2-cc2e-411c-b441-2102f4549d71" />


> **Note:** This endpoint exposes internal application metrics (JVM memory, CPU, request counts) in a format that a **Prometheus Server** can scrape for monitoring and dashboarding (e.g., in Grafana).



---

### Network Troubleshooting

If you cannot access the URLs above, ensure your **AWS Security Group** has an **Inbound Rule** configured:

* **Type:** Custom TCP
* **Port Range:** `8081`
* **Source:** `0.0.0.0/0` (or your specific IP)

Would you like me to help you create a **Systemd Service** file so that your application automatically restarts if the Ubuntu server reboots?
