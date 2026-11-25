# MySQL Database Setup & Workbench Guide

## 1. Prerequisites
- **MySQL Server 8.0+** installed and running on `localhost:3306`
- **MySQL Workbench 8.0 CE** installed

---

## 2. Import Schema & Seed Data in MySQL Workbench

1. Open **MySQL Workbench** and connect to your local MySQL Server instance (`Local instance MySQL80`).
2. Go to **File -> Open SQL Script...** and select `database/schema.sql`.
3. Click the **Lightning Bolt** icon (`Execute`) to create the `riya_industries` database and all tables.
4. *(Optional demonstration data)* Open `database/seed.sql` and execute it to load clearly labeled `[DEMO]` services, machines, projects, and default settings.
5. Refresh the **Schemas** panel on the left to verify `riya_industries` and its 17 tables are present.

---

## 3. Connect Spring Boot Using Environment Variables

Never hardcode your MySQL password in `application.yml` or commit it to GitHub.

Set these environment variables before starting Spring Boot (or configure them in IntelliJ IDEA / VS Code Run Configuration):

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/riya_industries?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
$env:ADMIN_INIT_USERNAME="admin"
$env:ADMIN_INIT_PASSWORD="ChangeThisStrongPassword!"
```

On first startup, the application automatically ensures the initial admin account exists in `admin_users` using `ADMIN_INIT_PASSWORD` hashed with **BCrypt**.
