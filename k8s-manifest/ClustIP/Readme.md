I have refined and completed the documentation, including the missing "Restart Deployment" section and the correct syntax for rollouts.

---

# 🚀 Testing Kubernetes ClusterIP Services

In Kubernetes, a **ClusterIP** service is the default service type. It provides a stable IP address and DNS name that is only reachable **within the cluster network**.

This guide demonstrates how to verify connectivity using two different internal methods: **Pod-to-Service (DNS)** and **Node-to-Service (Direct IP)**.

---

## 1. Prerequisites: Deployment & Service

First, ensure your application and service are applied to the cluster.

### `springboot-deployment.yaml`
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
```

### `spring-service.yaml`
```yaml
apiVersion: v1
kind: Service
metadata:
   name: spring-service
spec:
   type: ClusterIP
   selector:
      app: spring
   ports:
      - protocol: TCP
        port: 8081
        targetPort: 8081
```

### Apply Configurations
```bash
kubectl apply -f springboot-deployment.yaml
kubectl apply -f spring-service.yaml
```

---

## 2. Verification
Before testing, ensure your resources are running correctly.

```bash
# Check Pod status (ensure they are 'Running')
kubectl get pods

# Check Service details and note the ClusterIP
kubectl get svc spring-service

# View detailed service configuration and active endpoints
kubectl describe svc spring-service
```

---

## 3. Testing Method A: via Pod (DNS Resolution)
This is the most common way to test internal communication. Since Pods use **CoreDNS**, you can use the Service Name instead of an IP address.

1. **Identify a running Pod:**
   ```bash
   kubectl get pods
   ```

2. **Exec into the Pod:**
   ```bash
   kubectl exec -it <pod-name> -- bash
   ```

3. **Curl the Service Name:**
   Inside the pod terminal, run:
   ```bash
   curl http://spring-service:8081
   ```
   *Note: This works because Kubernetes DNS resolves `spring-service` to the ClusterIP.*

---

## 4. Testing Method B: via Minikube Node (Direct IP)
The Minikube Node is part of the cluster network, so it can reach the **ClusterIP** directly. However, the Node itself cannot resolve the Kubernetes DNS name.

1. **Find the ClusterIP:**
   ```bash
   kubectl get svc spring-service
   ```

2. **SSH into the Minikube Node:**
   ```bash
   minikube ssh
   ```

3. **Curl the ClusterIP:**
   Inside the Minikube shell, run:
   ```bash
   curl http://<YOUR_CLUSTER_IP>:8081
   ```

**Expected Output:**
`JAVA application deployed on EC2 Instance By AA 09-04. Server IP: 10...`

---

## 5. Maintenance & Deployment Management

### Checking Service Endpoints
To see the list of actual Pod IPs the service is currently targeting:
```bash
kubectl get endpoints spring-service
```

### Restarting a Deployment
**Why do we do this?** * To pull a fresh image if you are using the `:latest` tag and the image was updated in the registry.
* To refresh application configurations or environment variables without changing the YAML file.
* To clear stuck processes or memory leaks by replacing all pods.

**The Command:**
```bash
kubectl rollout restart deployment springboot-deployment
```

### Monitoring the Rollout
To check the status of the restart/update:
```bash
kubectl rollout status deployment springboot-deployment
```

### Manual Resiliency Test (Deleting a Pod)
To see how Kubernetes automatically recovers, delete a single pod; a new one will be created instantly to match the replica count.
```bash
kubectl delete pod <pod-name>
```

---

## 6. Cleanup
Once you have finished testing, remove the resources to free up cluster capacity.

### Delete by Filename
If you still have the YAML files locally, this is the cleanest way to remove all resources defined within them:
```bash
kubectl delete -f springboot-deployment.yaml
kubectl delete -f spring-service.yaml
```