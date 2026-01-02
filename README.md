This updated `README.md` integrates the Docker build process into the workflow, ensuring a logical progression from local setup to containerization.

---

# Spring Boot Hello - Deployment Guide

This repository contains a Spring Boot application. Follow the steps below to build and deploy the application on an **Ubuntu 22.04/24.04 LTS** server using either a native process or **Docker**.

## 1. Pre-requisites

Ensure your system is up to date and the necessary tools are installed.

### Install Core Tools

```bash
sudo apt update
sudo apt install git maven openjdk-17-jdk -y

```

### Install Docker (Optional for Containerization)

```bash
sudo apt install docker.io -y
sudo usermod -aG docker $USER && newgrp docker

```

> **Jenkins Note:** If integrating with Jenkins, set the Git executable path to `/usr/bin/git` in the Global Tool Configuration.

---

## 2. Clone and Build

### Clone the Repository

```bash
git clone https://github.com/vickydevo/springboot-hello.git
cd springboot-hello

```

### Build the JAR file

Use Maven to compile the code and package it into an executable JAR file.

```bash
mvn clean install

```

---

## 3. Deployment Options

### Option A: Running as a Native Process

Use this method to run the app directly on the Ubuntu OS.

**Start in Background:**

```bash
cd target
nohup java -jar gs-spring-boot-0.1.0.jar > app_output.log 2>&1 &

```

**Management Commands:**
| Action | Command |
| --- | --- |
| **Check if running** | `ps aux | grep java` |
| **View logs** | `tail -f app_output.log` |
| **Stop Process** | `pkill -f gs-spring-boot` |

### Option B: Running with Docker (Recommended)

Use this method to containerize the application for consistent deployments.

**Build the Image:**

```bash
docker build -t springboothello:v1 -f Dockerfile-with-ARG-ENV . --build-arg version=0.1.0

```

**Run the Container:**

```bash
docker run -d -p 8081:8081 --name springboot-app springboothello:v1

```

---

## 4. Accessing the Application

Once started, the application is accessible at the following endpoints (Replace `<IP>` with your server IP):

* **Home Page:** `http://<IP>:8081`

---

## 5. Troubleshooting & Network

### Firewall Configuration

If you cannot access the URLs, ensure your **AWS Security Group** or local firewall (UFW) has an **Inbound Rule** configured:

* **Type:** Custom TCP
* **Port Range:** `8081`
* **Source:** `0.0.0.0/0` (or your specific IP)

### Docker Logs

If using Docker, check container health with:

```bash
docker logs -f springboot-app

```

---
