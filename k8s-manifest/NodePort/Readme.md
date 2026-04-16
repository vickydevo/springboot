This is a complete and professional technical guide for exposing your **Spring Boot** application using **NodePort**. I have standardized the formatting, ensured command consistency, and added the **Cleanup** section at the end as requested.

---

# 🍃 Spring Boot External Access: NodePort & SSH Tunneling

This guide details how to expose a Java Spring Boot application running in a Minikube cluster (hosted on an EC2 instance) and access it from a local browser.

---

## 🏗️ 1. Infrastructure Overview

The request flow bridges the gap between your local machine and the private Minikube network as follows:

**Local Browser** $\rightarrow$ **SSH Tunnel** $\rightarrow$ **EC2 Host** $\rightarrow$ **Minikube Node (NodePort)** $\rightarrow$ **Spring Boot Pod**

---

## 📄 2. Spring Boot Manifest (`spring-nodeport.yml`)

This manifest creates a 3-replica deployment and a NodePort service. Note that the `nodePort` is explicitly set to `30037`.

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
    nodePort: 30037   # External Access Port (Range: 30000-32767)
```

**Apply the manifest:**
```bash
kubectl apply -f spring-nodeport.yml
```

---

## 🔍 3. Verification & Local Testing

### Check Service Status
Confirm that the service is active and mapped correctly:
```bash
kubectl get svc spring-service-np
```
*The output should display `8081:30037/TCP`.*

### Test Internally from EC2 Host
Verify the application is reachable within the EC2 instance by hitting the Minikube IP:
```bash
# Get Minikube IP (usually 192.168.49.2)
MINIKUBE_IP=$(minikube ip)

# Curl the NodePort
curl http://$MINIKUBE_IP:30037
```
**Expected Output:** `Greetings from 'vignan' deployed JAVA app...`

---

## 🌐 4. Accessing from your Local Browser

Since the Minikube IP is on a private bridge network, you must tunnel the traffic.

### Option A: SSH Tunneling (Best Practice)
Run this from your **Local Computer terminal**:
```bash
ssh -i "your-key.pem" -L 8081:$(minikube ip):30037 ubuntu@<EC2-PUBLIC-IP>
```

### Option B: Kubectl Port-Forward
Run this on the **EC2 instance** to bind the service to all interfaces:
```bash
kubectl port-forward --address 0.0.0.0 service/spring-service-np 8081:8081
```
*Note: Ensure port 8081 is allowed in your AWS EC2 Security Group.*

---

## 🏁 5. Final Result
Open your local browser and navigate to:
**`http://localhost:8081`**

---

## 🧹 6. Cleanup

To remove all resources created in this guide and free up system memory, run the following commands:

### Delete using the Manifest File
```bash
kubectl delete -f spring-nodeport.yml
```


### Verify Cleanup
```bash
kubectl get all
```