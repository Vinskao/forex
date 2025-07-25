## MongoDB Setup
```bash
docker run -d --name mongo-dev -p 27017:27017 mongo:latest
```

## Test
### 1. Insert Data

Uncommemted out this line in `src\main\resources\application.yml` so you can see the result in 10 seconds.

```yml
cron: "0/10 * * * * *"  # run every 10 secs
```

```bash
# Check with mongosh
mongosh
use forex
db.forex_rates.find().sort({date: -1}).limit(5)
```

### 2. Read Data
```bash
curl -X POST "http://localhost:8080/api/forex/usd-ntd-history" \
     -H "Content-Type: application/json" \
     -d '{"currency":"USD/NTD","startDate":"2025/05/31","endDate":"2025/06/03"}'
```