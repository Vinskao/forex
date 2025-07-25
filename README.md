## MongoDB
```bash
docker run -d --name mongo-dev -p 27017:27017 mongo:latest
```


## Test
### 1. Insert Data

### 2. Read Data
```bash
curl -G "http://localhost:8080/api/forex/usd-ntd-history" \
     --data-urlencode "currency=usd" \
     --data-urlencode "startDate=2025-07-01" \
     --data-urlencode "endDate=2025-07-20"
```