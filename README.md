# Spring MVC + Angular Team Directory

A full-stack employee directory with a REST API built on Spring MVC and a standalone Angular UI. The backend uses Spring Boot **4.1.1** and Java **21**; the frontend uses Angular **22.2.3**.

> AngularJS (1.x) is discontinued and does not receive security updates. This demo uses the current Angular framework instead.

## Requirements

- Java 21+
- Node.js 24.15+ and npm

## Run locally

Start the Spring MVC API in one terminal:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Start Angular in a second terminal:

```powershell
cd frontend
npm start
```

Open <http://localhost:4200>. The Angular development-server proxy forwards `/api` requests to Spring Boot on port 8080. The API can also be browsed directly at <http://localhost:8080/api/employees>.

## REST API

All request and response bodies use JSON. Create and update requests have this shape:

```json
{
  "firstName": "Alex",
  "lastName": "Morgan",
  "email": "alex.morgan@example.com",
  "department": "Engineering",
  "salary": 85000
}
```

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/employees` | List employees |
| `GET` | `/api/employees/{id}` | Get one employee |
| `POST` | `/api/employees` | Create an employee (`201 Created`) |
| `PUT` | `/api/employees/{id}` | Update an employee |
| `DELETE` | `/api/employees/{id}` | Delete an employee (`204 No Content`) |

The example stores data in memory and starts with three sample employees; changes are reset when the backend restarts. Request validation rejects missing or malformed fields.

## Run checks

```powershell
cd backend
.\mvnw.cmd test
```

```powershell
cd frontend
npm run build
```

## License

Copyright (C) 2026 [Debajyoti Nath](https://github.com/debajyoti-nath-eng).

This repository is provided for viewing and cloning only. No permission is
granted to use, reproduce, modify, distribute, or create derivative works
from its contents. See [LICENSE](LICENSE) for the full terms.

Third-party libraries and tools are subject to their respective licenses.
