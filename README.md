# SecureAuth: Login Authentication System

A modern desktop login system built with **Java Swing** and **MySQL**. Users can register, log in, view a personal dashboard and change their password, while administrators can manage all accounts. Passwords are protected with **BCrypt** hashing.

## Overview

SecureAuth is a mini project that shows how a Java desktop application connects to a database and handles user accounts safely. It has a colourful custom interface, role-based access (user and admin) and a clean package structure that can be reused as the login module of a larger application.

## Features

- Registration with validation (username, email, password and confirm password)
- Secure login with BCrypt password verification
- Show password option on login and register screens
- Custom window with gradient design, minimize and close buttons, and drag to move
- Dashboard showing role, email, last login time and total registered users
- Change password page that checks the current password first
- Role-based access: `user` and `admin`
- Admin panel to view all users and delete accounts
- Last login tracking
- Database settings kept in a config file, outside the source code and Git

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17+ | Programming language |
| Java Swing | Graphical user interface |
| FlatLaf | Modern look and feel |
| MySQL 8 | Database |
| JDBC (MySQL Connector/J) | Database connection |
| jBCrypt | Password hashing |
| Maven | Build and dependencies |

## Project Structure

```
src/main
├── java
│   ├── Main.java                 # Entry point
│   ├── db/DBConnection.java      # Database connection
│   ├── dao/UserDAO.java          # Database operations
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
    ├── db.properties.example     # Config template
    └── db.properties             # Your real settings (ignored by Git)
```

## Getting Started

### Prerequisites
- JDK 17 or newer
- MySQL Server 8.x and MySQL Workbench
- IntelliJ IDEA

### 1. Clone the repository
```bash
git clone https://github.com/tanmayudaiwal2511/Java-Swing-login-system-with-MySQL.git
```

### 2. Create the database
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

### 3. Configure the connection
Copy `src/main/resources/db.properties.example` to `db.properties` and enter your MySQL password:

```properties
db.url=jdbc:mysql://localhost:3306/auth_db
db.user=root
db.password=your_mysql_password
```

### 4. Run
Open the project in IntelliJ IDEA, let Maven download the dependencies and run `Main.java`.

## Creating an Admin

Register a normal account, then run:

```sql
UPDATE users SET role = 'admin' WHERE username = 'your_username';
```

Log in again and the **Manage Users** option appears in the sidebar.

## Security

- Passwords are hashed with BCrypt and a random salt; plain text is never stored
- All queries use prepared statements to prevent SQL injection
- Login errors use a general message so they do not reveal which field was wrong
- Database credentials are loaded from `db.properties`, which is excluded from Git

## Future Scope

- Email OTP-based two-factor authentication
- Forgot password with email reset code
- Account lockout after repeated failed logins
- Admin activity log and role management screen
- Profile photo and dark mode

## Author

**Tanmay Udaiwal**
Department of Computer Engineering, Poornima Institute of Engineering & Technology, Jaipur

GitHub: [@tanmayudaiwal2511](https://github.com/tanmayudaiwal2511)
