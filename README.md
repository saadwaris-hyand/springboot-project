# Ticket Validator App

This is a full-stack web application for validating different types of IT service tickets. It consists of:
- A **Spring Boot** backend that performs business logic and validation.
- A **React** frontend (with Material UI) for user input, result display, and interaction.

---

## 📁 Project Structure

```
Springboot Project/
├── ticket-validator-backend/         # Spring Boot backend
│   └── src/                          # Backend source code
└── ticket-validator-react/
    └── json-ticket-validator/
        └── src/                      # React frontend source code
```

---

## ✅ Prerequisites

To run this project locally, make sure you have the following installed:

### 🔙 Backend (Spring Boot with Gradle)
- **Java** – Version 17+
- **Gradle** – Version 7.5+ (or use the Gradle Wrapper `./gradlew`)
- **Spring Boot** – Version 3.x

### 🎨 Frontend (React)
- **Node.js** – Version 18+
- **npm** – Version 9+ (comes with Node)
- **Vite** – Version 4+ (used under the hood via `npm run dev`)

---

## 🚀 Getting Started

In case it is a new machine and you dont have any prerequisites, there is an extensive setup guide at the last of this documentation which guides you through setting up the project from scratch on a new laptop. Follow these steps to get everything installed and running.

---

### Backend (Spring Boot)

1. Navigate to the backend folder:

   ```bash
   cd "ticket-validator-backend"
   ```

2. Run the Spring Boot application (make sure Java 17+ and Gradle are installed):

   ```bash
   ./gradlew bootRun
   ```

   The backend will start at: [http://localhost:8080](http://localhost:8080)

---

### Frontend (React + Vite + Material UI)

1. Navigate to the frontend folder:

   ```bash
   cd "ticket-validator-react/json-ticket-validator"
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Start the development server:

   ```bash
   npm run dev
   ```

   The frontend will be running at: [http://localhost:5173](http://localhost:5173)

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
| Field     | Rule                                                                 |
|-----------|----------------------------------------------------------------------|
| `category`| Must be one of: `Network`, `Database`, `UI`, `Access Issue`          |
| `impact`  | Required and must not be blank                                       |

---

### 🔸 Additional for `CHANGE_REQUEST`:
| Field                   | Rule                                      |
|-------------------------|-------------------------------------------|
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


## 🛠 Technologies Used

- **Backend:** Java 17, Spring Boot,
- **Frontend:** React, Material UI (MUI)

---

## ✅ Features

- Validates multiple ticket types (`INCIDENT`, `CHANGE_REQUEST`, `MAINTENANCE`)
- Real-time form validation
- Display of raw JSON response
- History of past validations
- Responsive UI with MUI components

---

## 🔧 First Time Setup

This section guides you through setting up the project from scratch on a new laptop. Follow these steps to get everything installed and running.

### Prerequisites Installation

#### 1. Install Git

**Windows:**
1. Download Git from [git-scm.com](https://git-scm.com/download/win)
2. Run the installer and follow the prompts
3. Open Command Prompt or PowerShell and verify installation:
   ```bash
   git --version
   ```

**macOS:**
1. Install Homebrew if not installed:
   ```bash
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   ```
2. Install Git:
   ```bash
   brew install git
   ```
3. Verify installation:
   ```bash
   git --version
   ```

**Linux (Ubuntu/Debian):**
1. Update package lists:
   ```bash
   sudo apt update
   ```
2. Install Git:
   ```bash
   sudo apt install git
   ```
3. Verify installation:
   ```bash
   git --version
   ```

#### 2. Install Java 17+

**Windows:**
1. Download Java 17 JDK from [Oracle](https://www.oracle.com/java/technologies/downloads/#java17) or [OpenJDK](https://adoptium.net/)
2. Run the installer and follow the prompts
3. Set JAVA_HOME environment variable:
   - Right-click "This PC" > Properties > Advanced system settings > Environment Variables
   - Add new system variable: JAVA_HOME = C:\Program Files\Java\jdk-17 (adjust path as needed)
   - Add to PATH: %JAVA_HOME%\bin
4. Verify installation:
   ```bash
   java -version
   ```

**macOS:**
1. Install using Homebrew:
   ```bash
   brew install openjdk@17
   ```
2. Follow brew's instructions to create a symlink
3. Verify installation:
   ```bash
   java -version
   ```

**Linux (Ubuntu/Debian):**
1. Add the repository:
   ```bash
   sudo apt install -y software-properties-common
   sudo add-apt-repository ppa:linuxuprising/java
   sudo apt update
   ```
2. Install Java 17:
   ```bash
   sudo apt install oracle-java17-installer
   ```
   Or OpenJDK:
   ```bash
   sudo apt install openjdk-17-jdk
   ```
3. Verify installation:
   ```bash
   java -version
   ```

#### 3. Install Node.js and npm

**Windows:**
1. Download the LTS version installer from [nodejs.org](https://nodejs.org/)
2. Run the installer and follow the prompts (ensure npm is included)
3. Verify installation:
   ```bash
   node -v
   npm -v
   ```

**macOS:**
1. Install using Homebrew:
   ```bash
   brew install node
   ```
2. Verify installation:
   ```bash
   node -v
   npm -v
   ```

**Linux (Ubuntu/Debian):**
1. Install using Node Version Manager (recommended):
   ```bash
   curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash
   ```
2. Restart terminal or source profile:
   ```bash
   source ~/.bashrc
   ```
3. Install LTS version:
   ```bash
   nvm install --lts
   ```
4. Verify installation:
   ```bash
   node -v
   npm -v
   ```

### Project Setup

#### 1. Clone the Repository

1. Open terminal or command prompt
2. Navigate to where you want the project:
   ```bash
   cd /path/to/your/projects/folder
   ```
3. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ticket-validator.git
   ```
   (Replace the URL with your actual repository URL)
4. Navigate into the project folder:
   ```bash
   cd ticket-validator
   ```

#### 2. Set Up and Run Backend

1. Navigate to the backend directory:
   ```bash
   cd ticket-validator-backend
   ```

2. If you're on Windows and Gradle is not installed, you can use the Gradle wrapper:
   ```bash
   gradlew.bat bootRun
   ```
   
   If you're on macOS/Linux:
   ```bash
   ./gradlew bootRun
   ```

   If you encounter permission issues on macOS/Linux:
   ```bash
   chmod +x ./gradlew
   ./gradlew bootRun
   ```

3. The backend should start and be available at [http://localhost:8080](http://localhost:8080)

#### 3. Set Up and Run Frontend

1. Open a new terminal (keep the backend running)
2. Navigate to the frontend directory:
   ```bash
   cd ticket-validator-react/json-ticket-validator
   ```

3. Install frontend dependencies:
   ```bash
   npm install
   ```
   
   This may take a few minutes to complete.

4. Start the development server:
   ```bash
   npm run dev
   ```

5. The frontend should start and be available at [http://localhost:5173](http://localhost:5173)

### Troubleshooting Common Issues

#### Backend Issues

- **Java not found**: Ensure JAVA_HOME is set correctly and points to Java 17+
- **Port already in use**: If port 8080 is already used, you can change it in `src/main/resources/application.properties` by adding `server.port=8081`
- **Gradle errors**: Try cleaning the Gradle cache with `./gradlew clean` before running again

#### Frontend Issues

- **Node modules errors**: Delete the `node_modules` folder and run `npm install` again
- **Port already in use**: Press 'y' when prompted to use an alternative port, or change it in `vite.config.js`
- **Dependency conflicts**: Run `npm install --force` if you encounter package conflicts

#### Connection Issues

- Ensure both backend and frontend are running simultaneously
- Check that your firewall isn't blocking the required ports
- Verify the API base URL in frontend config matches the backend URL

---