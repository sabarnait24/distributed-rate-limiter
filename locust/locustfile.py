from locust import HttpUser, task, between
from queue import Queue
import random
import time
from collections import defaultdict, deque

user_ids = Queue()

for i in range(101, 106):
    user_ids.put(f"user{i}")


class RateLimitUser(HttpUser):

    wait_time = between(0.2, 0.5)

    def on_start(self):
        self.api_key = user_ids.get()
        self.request_times = defaultdict(deque)

    def record_request(self, method, endpoint):
        key = f"{method}:{endpoint}"
        now = time.time()

        timestamps = self.request_times[key]
        timestamps.append(now)

        # Keep only requests from the last 60 seconds
        while timestamps and timestamps[0] < now - 60:
            timestamps.popleft()

        print(
            f"{self.api_key} -> {method} {endpoint} "
            f"rate={len(timestamps)} req/min"
        )

    @task(1)
    def get_order(self):
        endpoint = "/api/orders/123"

        self.record_request("GET", endpoint)

        self.client.get(
            endpoint,
            headers={"X-API-Key": self.api_key}
        )


    @task(1)
    def get_profile(self):
        endpoint = f"/api/users/{self.api_key}/profile"

        self.record_request("GET", endpoint)

        self.client.get(
            endpoint,
            headers={"X-API-Key": self.api_key}
        )


    @task(1)
    def create_order(self):
        endpoint = "/api/orders"

        self.record_request("POST", endpoint)

        self.client.post(
            endpoint,
            headers={"X-API-Key": self.api_key},
            json={
                "userId": self.api_key,
                "productId": random.randint(100, 999),
                "quantity": random.randint(1, 10)
            }
        )