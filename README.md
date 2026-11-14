 # SecureAuth: Login Authentication System

A modern desktop login system built with **Java Swing** and **MySQL**. Users can register, log in, view a personal dashboard and change their password, while administrators can manage all accounts. Passwords are protected with **BCrypt** hashing.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue)
![Maven](https://img.shields.io/badge/Build-Maven-red)
![License](https://img.shields.io/badge/License-MIT-green)

---

## Table of Contents
- [Features](#features)
- [Screenshots](#screenshots)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Security](#security)
- [Database Schema](#database-schema)
- [Troubleshooting](#troubleshooting)
- [Future Scope](#future-scope)
- [Author](#author)

---

## Features

- **Registration** with validation (username length, email format, password length, password confirmation)
- **Secure login** with BCrypt password verification
- **Show password** option on the login and registration screens
- **Custom window design**: gradient panels, rounded buttons, hover effects, draggable window with minimize and close buttons
- **Dashboard** showing role, email, last login time and total registered users
- **Change password** page that verifies the current password first
- **Role-based access**: `user` and `admin` roles
- **Admin panel** to view all users and delete accounts (an admin cannot delete their own account)
- **Last login tracking** saved in the database
- **Database settings in a config file**, kept out of the source code and out of Git

## Screenshots

> Put your images in a `docs/screenshots/` folder with these names.

| Login | Register |
|---|---|
| ![Login](docs/screenshots/login.png) | ![Register](docs/screenshots/register.png) |

| Dashboard | Settings |
|---|---|
| ![Dashboard](docs/screenshots/dashboard.png) | ![Settings](docs/screenshots/settings.png) |

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17+ | Main programming language |
| Java Swing / AWT | Graphical user interface |
| FlatLaf 3.5.4 | Modern look and feel |
| MySQL 8 | Database |
| JDBC (MySQL Connector/J 8.4.0) | Java to MySQL connection |
| jBCrypt 0.4 | Password hashing |
| Maven | Build and dependency management |
| IntelliJ IDEA | Development environment |

## Project Structure

```
src/main
├── java
│   ├── Main.java                 # Entry point
│   ├── db/DBConnection.java      # Reads db.properties, opens connections
│   ├── dao/UserDAO.java          # All database operations
│   ├── model/User.java           # User data class
│   ├── util/Theme.java           # Colours, fonts, UI helpers
│   └── ui/
│       ├── AuthFrame.java        # Shared base for login and register
│       ├── LoginFrame.java
│       ├── RegisterFrame.java
│       ├── DashboardFrame.java
│       ├── TitleBar.java         # Custom minimize / close bar
│       ├── GradientPanel.java
│       └── ModernButton.java
└── resources
    ├── db.properties.example     # Template (copy to db.properties)
    └── db.properties             # Your real settings (ignored by Git)
```

## Getting Started

### Prerequisites
- JDK 17 or newer
- MySQL Server 8.x and MySQL Workbench
- IntelliJ IDEA (Maven support is built in)

### 1. Clone the repository
```bash
git clone https://github.com/tanmayudaiwal2511/Java-Swing-login-system-with-MySQL.git
cd Java-Swing-login-system-with-MySQL
```

### 2. Create the database
Run this in MySQL Workbench:

```sql
CREATE DATABASE auth_db;
USE auth_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(100) UNIQUE,
    role VARCHAR(10) NOT NULL DEFAULT 'user',
    last_login TIMESTAMP NULL
);
```

### 3. Configure the database connection
Copy the example file and enter your own MySQL password:

```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

On Windows, you can simply copy and rename the file in IntelliJ or File Explorer.

```properties
db.url=jdbc:mysql://localhost:3306/auth_db
db.user=root
db.password=your_mysql_password
```

`db.properties` is listed in `.gitignore`, so your password is never uploaded.

### 4. Open and run
1. Open the project in IntelliJ IDEA and let Maven download the dependencies.
2. Run `Main.java` (`Shift + F10`).

## Usage

1. Click **Sign up**, create an account and then log in.
2. Use **Home** to see your account summary and **Settings** to change your password.
3. To create an admin, register a normal account and then run:

```sql
UPDATE users SET role = 'admin' WHERE username = 'your_username';
```

Log in again and a **Manage Users** option appears in the sidebar.

## Security

- Passwords are hashed with **BCrypt** (with a random salt); plain-text passwords are never stored.
- All SQL queries use **prepared statements** to prevent SQL injection.
- Login errors use a general message ("Invalid username or password") so no hint is given about which part was wrong.
- Database credentials are loaded from `db.properties` and excluded from version control.
- For real deployments, use a dedicated MySQL user with limited rights instead of `root`.

## Database Schema

| Field | Type | Notes |
|---|---|---|
| id | INT | Primary key, auto increment |
| username | VARCHAR(50) | Unique, not null |
| password_hash | VARCHAR(100) | BCrypt hash |
| created_at | TIMESTAMP | Set automatically |
| email | VARCHAR(100) | Unique |
| role | VARCHAR(10) | `user` (default) or `admin` |
| last_login | TIMESTAMP | Updated on each login |

## Troubleshooting

| Problem | Fix |
|---|---|
| `Cannot load db.properties` | Make sure the file is in `src/main/resources`, then rebuild the project |
| `Access denied for user 'root'` | Check the password in `db.properties` |
| `Unknown database 'auth_db'` | Run the SQL script from step 2 |
| `Communications link failure` | Start the MySQL server and check port 3306 |
| `Unknown column 'email'` or `'role'` | Your table is missing columns; recreate it with the script above |
| Red errors on `BCrypt` or `FlatLaf` imports | Reload Maven changes in IntelliJ |

## Future Scope

- Email OTP-based two-factor authentication
- Forgot password with email reset code
- Account lockout after repeated failed logins
- Activity log of logins for admins
- Admin screen to change user roles and search users
- Profile photo and dark mode
- Windows installer (`.exe`) using `jpackage`

## Author

**Tanmay Udaiwal**
Department of Computer Engineering, Poornima Institute of Engineering & Technology, Jaipur

GitHub: [@tanmayudaiwal2511](https://github.com/tanmayudaiwal2511)
