Here is the complete, refined script. I have updated the `REQUESTS` variable to **20** as requested and added a small logic check to ensure the URL is reachable before starting the loop.

### `test.sh`

```bash
#!/bin/bash

# =================================================================
# Configuration
# Replace <MINIKUBE_IP> and <NODE_PORT> with your actual values
# Example: URL="http://192.168.49.2:32000"
# =================================================================
URL="http://<MINIKUBE_IP>:<NODE_PORT>"
REQUESTS=20
DELAY=0.5

echo "----------------------------------------------------"
echo "Starting Stress Test: $REQUESTS requests to $URL"
echo "----------------------------------------------------"

# Check if the endpoint is reachable before starting
if ! curl -s --head --request GET "$URL" | grep "200 OK" > /dev/null; then
    echo "⚠️  Warning: Target URL $URL seems unreachable. Check your Minikube IP/Port."
fi

# Loop to generate traffic
for ((i=1; i<=REQUESTS; i++))
do
   # Perform the request and capture the HTTP status code
   RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" $URL)
   
   echo "Request $i: Received HTTP $RESPONSE"
   
   # Small delay to mimic steady traffic
   sleep $DELAY
done

echo "----------------------------------------------------"
echo "Test Complete."
echo "Check Grafana for 'vignan_app_requests_total' metric."
echo "----------------------------------------------------"

```

---

### Quick Setup Instructions

1. **Create the file:**
```bash
nano test.sh

```


*(Paste the code above, update the `URL` line, then press `Ctrl+O`, `Enter`, and `Ctrl+X`)*
2. **Grant permission:**
```bash
chmod +x test.sh

```


3. **Run it:**
```bash
./test.sh

```



Would you like me to help you create a **Prometheus query (PromQL)** to visualize the request rate from these 20 hits in Grafana?
