# Riya Industries — Precision Machining & Lathe Job-Work Platform

A production-ready industrial machining and lathe job-work web platform built with **Java 17**, **Spring Boot**, **Thymeleaf**, **Spring Security**, and **MySQL 8**.

Designed with a high-contrast industrial aesthetic (black, white, metallic silver, charcoal, and industrial grey) to attract real manufacturing customers, manage job-work enquiries and quotation requests with drawing/photo uploads, and support future expansion into own-brand product manufacturing and e-commerce.

---

## Key Capabilities

### Public Website
- **Industrial Showcase**: Hero section, service category strip, machining showcase, capabilities, and why-choose-us highlights.
- **Services & Machinery**: Dynamic service catalog and workshop machinery specifications managed from the Admin Dashboard.
- **Project Gallery**: Completed component gallery with category/material filtering.
- **Job-Work Quotation & Enquiry System**: Customers can submit component details, quantities, materials, technical requirements, and attach engineering drawings or reference photos.
- **Direct Contact**: Click-to-call, WhatsApp enquiry integration, email contact, contact form, and Google Maps embed.

### Admin Dashboard (`/admin`)
- Secure login with BCrypt password hashing and role-based access control (`ADMIN`).
- Dashboard overview metrics (total/new enquiries, pending quotations, completed projects, services, machines).
- Full CRUD management for Services, Categories, Machinery, Completed Projects, Gallery, Enquiries, Quotations (with status workflow & internal notes), Contact Info, Website Settings, and Future Products.

### Future E-Commerce Ready
- Database schema and modular domain architecture prepared for `product_categories`, `products`, `product_images`, inventory, and future cart/order modules without activating unnecessary shopping flows today.

---

## Technology Stack

- **Backend**: Java 17 (LTS), Spring Boot 3.x, Spring MVC, Spring Data JPA (Hibernate), Spring Security, Maven
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap 5, Thymeleaf, GSAP (ScrollTrigger), Three.js (lightweight 3D accents)
- **Database**: MySQL Server 8.0, MySQL Workbench, MySQL Connector/J
- **Deployment Target**: Oracle Cloud Infrastructure (OCI) Always Free ARM VM + MySQL HeatWave Always Free / MySQL 8 + Nginx + Let's Encrypt HTTPS

---

## Repository Structure

```text
Riya_Industries_web/
├── database/
│   ├── schema.sql              # Full normalized MySQL 8 DDL
│   ├── seed.sql                # Clearly labeled demonstration data
│   └── README.md               # MySQL Workbench import & setup guide
├── docs/
│   ├── architecture.md         # System architecture, sitemap, wireframe, hosting, roadmap
│   ├── database-design.md      # ER diagram & table specifications
│   ├── deployment-guide.md     # Free hosting & HTTPS deployment guide
│   └── user-guide.md           # Non-technical owner guide for daily operations
├── src/
│   ├── main/
│   │   ├── java/com/riyaindustries/
│   │   │   ├── config/         # Web, security, upload, and MVC configuration
│   │   │   ├── controller/     # Public, API, and Admin controllers
│   │   │   ├── dto/            # Request/response & form DTOs
│   │   │   ├── entity/         # JPA entities
│   │   │   ├── exception/      # Global exception handling
│   │   │   ├── repository/     # Spring Data JPA repositories
│   │   │   ├── security/       # Spring Security & UserDetailsService
│   │   │   ├── service/        # Business logic & file storage services
│   │   │   └── util/           # Slug, file validation, and helper utilities
│   │   └── resources/
│   │       ├── static/         # CSS, JS, and local images
│   │       ├── templates/      # Thymeleaf public & admin templates
│   │       └── application.yml # Environment-driven Spring Boot config
│   └── test/                   # Unit & integration tests
├── .env.example                # Environment variable template (never commit .env)
├── .gitignore
├── Dockerfile
├── pom.xml
└── README.md
```

---

## Quick Start (Local Development)

1. **Create MySQL Database**: Import `database/schema.sql` and optionally `database/seed.sql` in MySQL Workbench (see [database/README.md](database/README.md)).
2. **Configure Environment**: Copy `.env.example` to `.env` (or set system environment variables `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `ADMIN_INIT_PASSWORD`).
3. **Run Application**:
   ```bash
   mvn spring-boot:run
   ```
4. **Access**:
   - Public Website: `http://localhost:8080`
   - Admin Dashboard: `http://localhost:8080/admin`
