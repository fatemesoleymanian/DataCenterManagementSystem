
# Data Center Management System (DCIM + CRM)

**پروژه تکمیل چالش فنی استخدامی شرکت فناوران دوراندیش اشراق نوین**

![Ashraq Logo](https://example.com/ashraq-logo.png) <!-- اگر لوگو دارید جایگزین کنید -->

## توضیح پروژه

این پروژه یک سامانه جامع مدیریت مراکز داده (DCIM) همراه با ماژول مدیریت مشتریان و خدمات (CRM) است که دقیقاً بر اساس چالش‌های فنی مطرح‌شده در PDF استخدامی شرکت **فناوران دوراندیش اشراق نوین** پیاده‌سازی شده است.

هدف پروژه:
- پیاده‌سازی کامل چالش اول: مدیریت فیزیکی مراکز داده، رک‌ها، تجهیزات، یونیت‌ها و اتصالات پورت‌ها با رعایت قوانین دقیق (مانند اتصال پورت ۱ سرورها از طریق پچ‌پنل به سوئیچ)
- پیاده‌سازی کامل چالش دوم: مدیریت مشتریان، اجاره/تمدید سرور اختصاصی، صدور فاکتور خرید/تمدید، پرداخت فاکتور، سیستم تیکتینگ با نقش‌های متفاوت (مشتری/پشتیبان/ادمین)

## ویژگی‌های کلیدی پیاده‌سازی‌شده

### چالش اول - مدیریت مراکز داده (DCIM)

- درختواره ۴ سطحی موقعیت جغرافیایی (کشور ← استان ← شهر ← محله)
- تعریف مراکز داده، ردیف رک، رک (۴۲ یونیت استاندارد)
- تعریف تجهیزات (سرور، سوئیچ، روتر، پچ‌پنل و ...) با تعداد یونیت و پورت
- الصاق تجهیزات به رک با چک دقیق عدم هم‌پوشانی یونیت‌ها (overlap prevention)
- اتصال یک‌به‌یک پورت‌ها با واسط پچ‌پنل (قانون اجباری: پورت ۱ سرورها فقط به پچ‌پنل)
- لاگ فعالیت تمام عملیات با `@LogActivity`
- کنترل دسترسی با Spring Security (ADMIN, SUPPORT)

### چالش دوم - مدیریت مشتریان و خدمات (CRM)

- ثبت مشتری (حقیقی/حقوقی) با نقش CUSTOMER
- اجاره سرور اختصاصی (ایجاد CustomerService + فاکتور BUY اتوماتیک)
- تمدید سرویس (به‌روزرسانی expiryDate + فاکتور RENEW اتوماتیک)
- پرداخت فاکتور (تغییر isPaid به true)
- سیستم تیکتینگ کامل:
  - ایجاد تیکت توسط مشتری
  - اضافه پیام توسط مشتری (فقط تیکت خودش) یا پشتیبان
  - تغییر وضعیت توسط پشتیبان (OPEN → PENDING → CLOSED)
  - محدودیت دسترسی: مشتری فقط تیکت خودش را می‌بیند
- لاگ فعالیت تمام عملیات

### ویژگی‌های فنی پروژه

- Java 21 + Spring Boot 3.x
- Spring Data JPA + Hibernate
- PostgreSQL / H2 (برای توسعه)
- Spring Security با JWT (Bearer Auth)
- Lombok + MapStruct (اختیاری برای mapper)
- Validation با Jakarta Bean Validation
- Swagger / OpenAPI برای مستندسازی API
- Global Exception Handler
- Audit fields با `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy`, `@LastModifiedBy`
- Logging فعالیت‌ها با انوتیشن سفارشی `@LogActivity`

## ساختار پروژه

```
src/main/java/com/example/DataCenterManagementSystem
├── config
│   ├── SecurityConfig.java
│   └── LoggingConfig.java (برای @LogActivity)
├── controller
│   ├── infrastructure (DCIM)
│   └── crm
├── dto
│   ├── infrastructure
│   └── crm
├── entity
│   ├── dcim
│   └── crm
├── repository
│   ├── dcim
│   └── crm
├── service
│   ├── infrastructure
│   └── crm
├── exception
├── mapper (اختیاری)
└── DataCenterManagementSystemApplication.java
```

## پیش‌نیازها

- Java 21
- Maven
- PostgreSQL (یا H2 برای تست سریع)
- IDE با پشتیبانی Lombok (IntelliJ / Eclipse)

## راه‌اندازی پروژه

1. **Clone پروژه**
   ```bash
   git clone <repository-url>
   cd DataCenterManagementSystem
   ```

2. **تنظیمات application.properties / application.yml**
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/dcim_db
       username: postgres
       password: your_password
     jpa:
       hibernate:
         ddl-auto: update
       show-sql: true
   ```

3. **اجرا**
   ```bash
   mvn spring-boot:run
   ```

   یا از IDE مستقیم Run کنید.

4. **Swagger UI**
   بعد از اجرا به آدرس زیر بروید:
   ```
   http://localhost:8080/api/swagger-ui/index.html
   ```

5. **داده اولیه**
    - پروژه شامل `@Bean CommandLineRunner` برای seed کردن داده‌های نمونه است (۵ دیتاسنتر با ساختار خواسته‌شده در PDF، کاربران ادمین/پشتیبان/مشتری، چند سرویس و تیکت نمونه).
    - اگر نمی‌خواهید seed خودکار انجام شود، کامنت کنید.

## APIهای اصلی (نمونه)

| Method | Endpoint                              | توضیح                              | Role مورد نیاز     |
|--------|---------------------------------------|------------------------------------|---------------------|
| POST   | /api/locations                        | ایجاد موقعیت جغرافیایی           | ADMIN              |
| POST   | /api/datacenters                      | ایجاد مرکز داده                   | ADMIN              |
| POST   | /api/racks                            | ایجاد رک                          | ADMIN              |
| POST   | /api/equipments                       | الصاق تجهیزات به رک               | ADMIN              |
| POST   | /api/ports/connect                    | اتصال پورت‌ها                     | ADMIN              |
| POST   | /api/users/register                   | ثبت مشتری جدید                    | PUBLIC             |
| POST   | /api/customer-services                | اجاره سرور (با فاکتور BUY)       | ADMIN              |
| PUT    | /api/customer-services/{id}/renew     | تمدید سرویس (با فاکتور RENEW)    | ADMIN / CUSTOMER   |
| PUT    | /api/invoices/{id}/pay                | پرداخت فاکتور                     | ADMIN / SUPPORT    |
| POST   | /api/tickets                          | ایجاد تیکت توسط مشتری             | CUSTOMER           |
| POST   | /api/tickets/{id}/messages            | اضافه پیام به تیکت                | CUSTOMER / SUPPORT |
| GET    | /api/activity-logs                    | مشاهده لاگ فعالیت‌ها               | ADMIN / SUPPORT    |

## نکات مهم برای مصاحبه

- تمام قوانین PDF (اتصال پورت ۱ سرور فقط به پچ‌پنل، یک‌به‌یک بودن اتصالات، عدم هم‌پوشانی یونیت‌ها، درختواره موقعیت، لاگ فعالیت، کنترل دسترسی) رعایت شده.
- فاکتور BUY هنگام ایجاد سرویس و RENEW هنگام تمدید به صورت اتوماتیک ایجاد می‌شود.
- پروژه از audit fields (createdAt/updatedAt/createdBy/lastModifiedBy) پشتیبانی می‌کند.
- آماده برای گسترش (مثلاً اضافه کردن قیمت پایه سرور، محاسبه اتوماتیک amount، notification برای انقضا سرویس و ...)

## تکنولوژی‌های استفاده‌شده

- Java 21
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA
- Lombok
- Swagger OpenAPI
- PostgreSQL / H2


تهیه و توسعه: فاطمه سلیمانیان  
تاریخ: اسفند ۱۴۰۴ (فوریه ۲۰۲۶)

