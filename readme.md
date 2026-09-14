# 🚀 Distributed Rate Limiter with Spring Boot, Redis & Kubernetes

A distributed rate limiter built with Spring Boot and Redis, deployed on Kubernetes using Helm and Minikube. The application supports endpoint- and user-level rate limiting, with Prometheus and Grafana for monitoring and Locust for load testing.

---

## 📋 Prerequisites

Ensure the following tools and runtime versions are installed on your system:

* **Java Development Kit (JDK):** Java 17 or 21
* **Build Tool:** Maven 3.8+ / Gradle 8+
* **Container Runtime:** Docker Desktop / Docker Engine 24.0+
* **Local Kubernetes:** Minikube v1.30+
* **Package Manager & CLI:** `kubectl`, `helm` 3.x
* **Load Testing:** Python 3.10+ (with virtual environment support)

---

## 🛠️ Step-by-Step Setup & Execution

### 1. Run Host Infrastructure & Monitoring Stack

```powershell
# Create dedicated Docker network for monitoring
docker network create monitoring

# Run Redis container on host
docker run -d --name redis-server -p 6379:6379 redis

# Run Grafana container
docker run -d --name grafana --network monitoring -p 3000:3000 grafana/grafana

# Create prometheus.yml to scrape configuration
New-Item -Path .\prometheus.yml -ItemType File
# (Make sure to add your target endpoints to prometheus.yml before running the next command)

# Run Prometheus container with custom config mount
docker run -d --name prometheus -p 9090:9090 --network monitoring -v \${PWD}/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
```

### 2. Deploy Application to Minikube

```powershell
# Start local Kubernetes cluster
minikube start 

# Build the application Docker image
docker build -t rate-limiter-app:1.0.4 .

# Cache the image inside Minikube
minikube image load rate-limiter-app:1.0.4

# Setup K8s Namespace
kubectl create namespace rate-limiter

# Deploy using Helm chart
helm install rate-limiter-app . -n rate-limiter
```

### 3. Expose Traffic & Scraping Endpoints

```powershell
# Route traffic and get the exposed Minikube service URL
minikube service rate-limiter-app -n rate-limiter --url

# Port-forward the service to allow host Prometheus to scrape application metrics
kubectl port-forward -n rate-limiter svc/rate-limiter-app 8080:8080
```

### 4. Execute Load Testing

```powershell
# Run Locust load test script
locust -f .\locustfile.py  

# Load testing configuration targets:
# - Users: 5
# - Ramp-up rate: 5 users/sec
# - Host: [Insert your Minikube service URL here]
```

---

## 📈 Observability & Test Visuals

### Locust Load Testing
![Locust Dashboard](./images/locust-dashboard.png)

### Grafana Monitoring Metrics
![Grafana Dashboard](./images/grafana-dashboard.png)
