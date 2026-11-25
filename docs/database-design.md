# MySQL Database Design

## Overview

The database is designed for **MySQL Server 8.0** using `utf8mb4` / `utf8mb4_unicode_ci` for full Unicode support, `InnoDB` for ACID transactions and foreign key integrity, and normalized tables covering:
1. **Security & Admin**: `roles`, `admin_users`, `admin_user_roles`
2. **Core Job-Work Catalog**: `service_categories`, `services`, `machines`, `machine_images`, `projects`, `project_images`
3. **Lead & Quotation Management**: `customers`, `enquiries`, `quotation_requests`, `quotation_attachments`, `contact_messages`
4. **Site Configuration**: `website_settings`
5. **Future E-Commerce Expansion**: `product_categories`, `products`, `product_images`

---

## Entity-Relationship Diagram

```mermaid
erDiagram
    roles ||--o{ admin_user_roles : assigned
    admin_users ||--o{ admin_user_roles : has

    service_categories ||--o{ services : categorizes
    machines ||--o{ machine_images : has_photos
    projects ||--o{ project_images : has_photos
    services ||--o{ projects : relates_to

    customers ||--o{ enquiries : submits
    customers ||--o{ quotation_requests : requests
    services ||--o{ quotation_requests : for_service
    quotation_requests ||--o{ quotation_attachments : includes

    product_categories ||--o{ products : categorizes
    products ||--o{ product_images : has_photos

    roles {
        bigint id PK
        varchar name UK
        varchar description
    }
    admin_users {
        bigint id PK
        varchar username UK
        varchar email UK
        varchar password_hash
        varchar full_name
        boolean active
        timestamp last_login_at
    }
    service_categories {
        bigint id PK
        varchar name
        varchar slug UK
        text description
        int display_order
        boolean active
    }
    services {
        bigint id PK
        bigint category_id FK
        varchar name
        varchar slug UK
        varchar short_description
        text full_description
        text capabilities
        text specifications
        varchar primary_image_url
        boolean featured
        boolean published
        int display_order
    }
    machines {
        bigint id PK
        varchar name
        varchar slug UK
        varchar machine_type
        varchar model
        text specifications
        text capabilities
        text additional_details
        varchar primary_image_url
        boolean active
        int display_order
    }
    projects {
        bigint id PK
        bigint service_id FK
        varchar title
        varchar slug UK
        text description
        varchar machining_type
        varchar material
        varchar industry
        varchar primary_image_url
        boolean featured
        boolean published
    }
    customers {
        bigint id PK
        varchar full_name
        varchar company_name
        varchar phone
        varchar email
        text address
    }
    enquiries {
        bigint id PK
        bigint customer_id FK
        varchar name
        varchar company_name
        varchar phone
        varchar email
        varchar subject
        text message
        varchar source_page
        varchar status
        text internal_notes
    }
    quotation_requests {
        bigint id PK
        varchar reference_code UK
        bigint customer_id FK
        bigint service_id FK
        varchar customer_name
        varchar company_name
        varchar phone
        varchar email
        varchar required_service_name
        text component_description
        varchar quantity
        varchar material
        text technical_requirements
        varchar status
        text internal_notes
    }
    quotation_attachments {
        bigint id PK
        bigint quotation_request_id FK
        varchar original_filename
        varchar stored_filename
        varchar content_type
        bigint file_size_bytes
        varchar storage_path
    }
    contact_messages {
        bigint id PK
        varchar name
        varchar phone
        varchar email
        varchar subject
        text message
        boolean is_read
    }
    website_settings {
        bigint id PK
        varchar setting_key UK
        text setting_value
        varchar setting_group
    }
    product_categories {
        bigint id PK
        varchar name
        varchar slug UK
        text description
        boolean active
    }
    products {
        bigint id PK
        bigint category_id FK
        varchar sku UK
        varchar name
        varchar slug UK
        text description
        text specifications
        decimal base_price
        int stock_quantity
        boolean active
        boolean published
    }
```

---

## Key Design Decisions

1. **Audit Timestamps**: Every core table includes `created_at` and `updated_at` managed automatically by MySQL defaults and JPA `@CreationTimestamp` / `@UpdateTimestamp`.
2. **Status Enums**: `enquiries.status` and `quotation_requests.status` use standardized uppercase values (`NEW`, `PENDING`, `CONTACTED`, `QUOTATION_SENT`, `ACCEPTED`, `REJECTED`, `COMPLETED`) with indexes for fast admin filtering.
3. **Safe File Storage**: `quotation_attachments` stores a UUID-based `stored_filename` and relative `storage_path` outside the webroot, while keeping `original_filename` for display in the admin dashboard.
4. **Non-Destructive Future Expansion**: `product_categories`, `products`, and `product_images` are created cleanly in MySQL now, with `products.published = FALSE` by default and an admin toggle in `website_settings` (`ecommerce_enabled = false`).
