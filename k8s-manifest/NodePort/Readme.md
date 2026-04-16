This is a solid technical breakdown. You've correctly identified that while **NodePort** is active, you can't reach it on the Minikube IP via the standard app port (8081); you must use the assigned NodePort (30037).

Here is your updated, professional `README.md` specifically tailored to your **Spring Boot** application.

---

# 🍃 Spring Boot External Access: NodePort & SSH Tunneling

This guide details how to expose a Java Spring Boot application running in a Minikube cluster (on an EC2 instance) and access it from a local browser.

---

## 🏗️ 1. Infrastructure Overview

The request flow follows this path to bridge the gap between your local machine and the private Minikube network:

**Local Browser** $\rightarrow$ **SSH Tunnel** $\rightarrow$ **EC2 Host** $\rightarrow$ **Minikube Node (NodePort)** $\rightarrow$ **Spring Boot Pod**

---

## 📄 2. Spring Boot Manifest (`spring-nodeport.yml`)

Apply this configuration to create both the 3-replica deployment and the NodePort service.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: springboot-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: spring
  template:
    metadata:
      labels:
        app: spring
    spec:
      containers:
      - name: springboot
        image: vignan91/springboot:1.0
        ports:
        - containerPort: 8081
---
apiVersion: v1
kind: Service
metadata:
  name: spring-service-np
spec:
  type: NodePort
  selector:
    app: spring
  ports:
  - protocol: TCP
    port: 8081        # Service Internal Port
    targetPort: 8081  # Pod Container Port
    nodePort: 30037   # External Access Port (30000-32767)

```

**Apply the manifest:**

```bash
kubectl apply -f spring-nodeport.yml

```

---

## 🔍 3. Verification & Local Testing

### Check Service Status

```bash
kubectl get svc spring-svc-np

```

*Confirm the output shows `8081:30037/TCP`.*

### Test internally from EC2 Host

Find your Minikube IP and curl the NodePort:

```bash
minikube ip  # e.g., 192.168.49.2
curl http://192.168.49.2:30037

```

**Success Output:** `Greetings from 'vignan' deployed JAVA app...`

---

## 🌐 4. Accessing from your Local Browser

Since the Minikube IP (`192.168.49.2`) is private to the EC2 instance, use an **SSH Tunnel** to map your local machine to the cluster.

### Option A: Direct Tunnel to NodePort (Recommended)

Run this command from your **Local Terminal** (not the EC2 shell):

```bash
ssh -i "your-key.pem" -L 8081:192.168.49.2:30037 ubuntu@<EC2-PUBLIC-IP>

```

### Option B: Using Kubectl Port-Forward

If you prefer using Kubernetes native tools, run this on the **EC2 instance**:

```bash
kubectl port-forward --address 0.0.0.0 service/spring-svc-np 8081:8081

```

*Note: Ensure port 8081 is open in your AWS Security Group.*

---

## 🏁 5. Final Result

Open your local browser and navigate to:
**`http://localhost:8081`**



---
