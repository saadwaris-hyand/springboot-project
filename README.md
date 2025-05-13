# Ticket Validator App

This is a full-stack web application for validating different types of IT service tickets. It consists of:
- A Spring Boot backend that performs business logic and validation.
- A React frontend (with Material UI) for user input, result display, and interaction.

---

## ✅ Features

1. Validate Single Ticket
   - Accepts a single JSON object representing a ticket.
   - Returns detailed validation results.
2. Validate Bulk Tickets
   - Accepts a list of JSON ticket objects.
   - Processes each independently and returns a list of validation results.
3. View Ticket History
   - Returns a list of all tickets submitted during the session and their validation status.

---

## ✅ Prerequisites

To run this project locally, make sure you have the following installed:

### 🔙 Backend (Spring Boot with Gradle)
- Java – Version 17+
- Gradle – Version 7.5+ (or use the Gradle Wrapper `./gradlew`)
- Spring Boot – Version 3.x

### 🎨 Frontend (React)
- Node.js – Version 18+
- npm – Version 9+ (comes with Node)
- Vite – Version 4+ (used under the hood via `npm run dev`)

---

# 📁 Project Structure

Springboot Project/
├── ticket-validator-backend/         # Spring Boot backend
│   └── src/                          # Backend source code
└── ticket-validator-react/
    └── json-ticket-validator/
        └── src/                      # React frontend source code

---



## 🚀 Getting Started

### Backend (Spring Boot)

1. Navigate to the backend folder:

   cd "ticket-validator-backend"
   

2. Run the Spring Boot application (make sure Java 17+ and Gradle are installed):

   ./gradlew bootRun

   The backend will start at: [http://localhost:8080]

---

### Frontend (React + Vite + Material UI)

1. Navigate to the frontend folder:

   cd "ticket-validator-react/json-ticket-validator"

2. Install dependencies:

   npm install

3. Start the development server:

   npm run dev

   The frontend will be running at: [http://localhost:5173]

---

## 📡 API Endpoints (Backend)

Base URL: `http://localhost:8080`

| Method | Endpoint            | Description                               |
|--------|---------------------|-------------------------------------------|
| POST   | `/validate`         | Validates a single ticket                 |
| POST   | `/validate/bulk`    | Validates a list of tickets               |
| GET    | `/tickets`          | Retrieves previously validated tickets    |

> All POST endpoints accept and return JSON.

---


## 🧪 Testing

### Backend Tests

Tests cover:

- Controller layer (`TicketValidationControllerTest`)
- Service layer (`TicketServiceTest`)
- DTO validation logic (`TicketDTOTest`)

Run backend tests:

./gradlew test

---

## 🛠 Technologies Used

- **Backend:** Java 17, Spring Boot
- **Frontend:** React, Material UI (MUI)

---

## 🧪 Sample Inputs & Validation Rules

### ✅ Sample Input – Valid INCIDENT Ticket

```json
{
  "type": "INCIDENT",
  "system": "CRM-System",
  "created_date": "2023-04-10",
  "priority": "HIGH",
  "description": "Login issue for multiple users",
  "responsible": "admin.user",
  "category": "Access Issue",
  "impact": "System unavailable"
}
```

---

### ❌ Sample Input – Invalid CHANGE_REQUEST Ticket

```json
{
  "type": "CHANGE_REQUEST",
  "system": "",
  "created_date": "2026-01-01",
  "priority": "URGENT",
  "description": "",
  "responsible": "",
  "planned_execution_date": "2024-12-01",
  "approver": ""
}
```

---

## ✅ Validation Rules

### 🔹 Common (All Ticket Types)
| Field          | Rule                                                                   |
|----------------|------------------------------------------------------------------------|
| `system`       | Must not be empty                                                      |
| `created_date` | Must not be in the future                                              |
| `priority`     | Must be one of: `LOW`, `MEDIUM`, `HIGH`, `CRITICAL` (case-insensitive) |
| `description`  | Must not be empty                                                      |
| `responsible`  | Must not be empty                                                      |

---

### 🔸 Additional for `INCIDENT`:
| Field     | Rule                                                                  |
|-----------|-----------------------------------------------------------------------|
| `category`| Must be one of: `Network`, `Database`, `UI`, `Access Issue`           |
| `impact`  | Required and must not be blank                                        |

---

### 🔸 Additional for `CHANGE_REQUEST`:
| Field                   | Rule                                      |
|--------------------------|------------------------------------------|
| `planned_execution_date`| Must be in the future                     |
| `approver`              | Required and must not be blank            |

---

### 🔸 Additional for `MAINTENANCE`:
| Field                     | Rule                                               |
|---------------------------|----------------------------------------------------|
| `maintenance_window_start`| Must be before `maintenance_window_end`            |
|                           | Must be in the future                              |
| `maintenance_window_end`  | Must be in the future                              |
| `affected_components`     | Must not be null or empty                          |
