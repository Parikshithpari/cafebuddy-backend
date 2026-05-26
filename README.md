# CafeBuddy Backend

Spring Boot REST API for CafeBuddy — Excel-based cafe import + JWT user accounts.

## Import into Spring Tool Suite (STS)

1. **File → Import → Maven → Existing Maven Projects**
2. Browse to the unzipped `cafebuddy-backend` folder
3. Click **Finish** — STS auto-resolves dependencies
4. Right-click the project → **Run As → Spring Boot App**
5. Server starts at `http://localhost:8080`

---

## API Reference

### Auth

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | Public | Create account, returns JWT |
| POST | `/api/auth/login` | Public | Login, returns JWT |

**Register**
```json
POST /api/auth/register
{
  "fullName": "Jane Doe",
  "email": "jane@example.com",
  "password": "secret123"
}
```

**Login**
```json
POST /api/auth/login
{
  "email": "jane@example.com",
  "password": "secret123"
}
```

Both return:
```json
{
  "token": "eyJhbGci...",
  "type": "Bearer",
  "id": 1,
  "fullName": "Jane Doe",
  "email": "jane@example.com",
  "role": "USER"
}
```

---

### Cafes

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/cafes` | Public | List all cafes |
| GET | `/api/cafes?q=brooklyn` | Public | Search cafes |
| GET | `/api/cafes/{id}` | Public | Single cafe |
| POST | `/api/cafes/upload` | Required | Upload Excel file |
| DELETE | `/api/cafes/{id}` | Required | Delete cafe |

**Upload Excel:**
```bash
curl -X POST http://localhost:8080/api/cafes/upload \
     -H "Authorization: Bearer <token>" \
     -F "file=@cafes-sample.xlsx"
```

**Response:**
```json
{
  "totalRows": 6,
  "imported": 6,
  "skipped": 0,
  "errors": [],
  "cafes": [
    { "id": 1, "name": "Verve Coffee", "area": "Abbot Kinney, LA", ... }
  ]
}
```

---

### Users

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/users/me` | Required | Own profile |
| GET | `/api/users` | ADMIN only | All users |
| GET | `/api/users/{id}` | ADMIN only | Single user |

---

## Excel Format

| Column | Required | Allowed values | Notes |
|--------|----------|----------------|-------|
| `name` | Yes | any text | Also: "cafe", "cafe name" |
| `area` | Yes | any text | Also: "location", "neighbourhood" |
| `address` | No | any text | |
| `lat` | No | decimal | Also: "latitude" |
| `lng` | No | decimal | Also: "longitude", "long" |
| `mood` | No | Quiet / Heads-down / Mixed / Chatty | Defaults to Mixed |
| `wifi` | No | Fast / Decent / Spotty | Defaults to Decent |
| `outlets` | No | Plenty / Some / Few | Defaults to Some |

A sample file `cafes-sample.xlsx` is included.

---

## Switch to PostgreSQL (Production)

In `application.properties`, replace the H2 block with:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cafebuddy
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```
