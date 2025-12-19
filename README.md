# LMS Demo (Spring Boot)

Простая система управления онлайн-курсами (Learning Management System) на базе Spring Boot.

## Технологии

- Java 17
- Spring Boot 3 (Web, Data JPA, Security, Validation)
- PostgreSQL
- Hibernate
- Lombok
- MapStruct
- JWT (io.jsonwebtoken)

## Запуск (локально)

1. Создать базу данных PostgreSQL:

```sql
CREATE DATABASE lms_db;
CREATE USER lms_user WITH PASSWORD 'lms_password';
GRANT ALL PRIVILEGES ON DATABASE lms_db TO lms_user;
```

2. Проверить настройки в `src/main/resources/application.yml` (URL, логин/пароль).

3. Собрать и запустить:

```bash
mvn spring-boot:run
```

При первом запуске будут созданы роли `ADMIN`, `TEACHER`, `STUDENT` и админ-пользователь:

- username: `admin`
- password: `admin123`

## Запуск через Docker / Docker Compose

Требуется установленный Docker и Docker Compose.

1. Собрать и запустить сервисы:

```bash
docker-compose up --build
```

2. В составе поднимаются:

- `db` — PostgreSQL (`lms_db` / `lms_user` / `lms_password`)
- `app` — Spring Boot приложение (порт `8080` на хосте)

3. После старта API доступны по `http://localhost:8080/api/v1/...`, а учётка `admin/admin123` работает так же, как при локальном запуске.

## Аутентификация

### Логин

`POST /api/v1/auth/login`

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Ответ:

```json
{
  "token": "JWT_TOKEN",
  "refreshToken": "REFRESH_TOKEN",
  "expiresIn": 180
}
```

Токен передаётся в заголовке:

`Authorization: Bearer JWT_TOKEN`

### Обновление токена

`POST /api/v1/auth/refresh`

```json
{
  "refreshToken": "REFRESH_TOKEN"
}
```

### Безопасность паролей

Пароль должен соответствовать требованиям:
- Минимум 8 символов
- Хотя бы одна заглавная буква
- Хотя бы одна строчная буква
- Хотя бы одна цифра
- Хотя бы один специальный символ (!@#$%^&*()_+-=[]{}|;:,.<>?)

Пароли шифруются с помощью BCrypt.

### Автоматический выход

- **Время жизни JWT токена**: 3 минуты
- **Время жизни refresh токена**: 15 минут
- **Автоматический выход при бездействии**: 3 минуты бездействия пользователя
- **Автоматическое обновление токена**: каждые 2.5 минуты при активности

### Регистрация студента

`POST /api/v1/auth/register`

```json
{
  "username": "student1",
  "email": "student1@example.com",
  "password": "secret123"
}
```

## Примеры REST API

Все эндпоинты имеют префикс `/api/v1`.

### Курсы

- `GET /api/v1/courses` — список курсов (пагинация через параметры `page`, `size`, `sort`).
- `GET /api/v1/courses/{id}` — получить курс.
- `POST /api/v1/courses` — создать курс (TEACHER/ADMIN).
- `PUT /api/v1/courses/{id}` — обновить курс.
- `DELETE /api/v1/courses/{id}` — удалить курс.

Тело запроса при создании/обновлении:

```json
{
  "title": "Java для начинающих",
  "description": "Базовый курс по Java",
  "price": 1000.0,
  "teacherId": 1,
  "categoryId": 1
}
```

### Запись на курс

- `GET /api/v1/enrollments/student/{studentId}` — курсы студента.
- `GET /api/v1/enrollments/course/{courseId}` — студенты курса.
- `POST /api/v1/enrollments` — записаться на курс.
- `DELETE /api/v1/enrollments/{id}` — отписаться.

Пример тела для записи:

```json
{
  "studentId": 2,
  "courseId": 1
}
```

### Отзывы

- `GET /api/v1/reviews/course/{courseId}` — отзывы по курсу.
- `GET /api/v1/reviews/student/{studentId}` — отзывы студента.
- `POST /api/v1/reviews` — добавить отзыв.

```json
{
  "rating": 5,
  "comment": "Отличный курс!",
  "studentId": 2,
  "courseId": 1
}
```

## Роли и доступ

- `ADMIN` — полный доступ ко всем сущностям.
- `TEACHER` — создание и управление своими курсами, уроками и материалами.
- `STUDENT` — просмотр курсов, запись на курсы, добавление отзывов.

Безопасность реализована через Spring Security + JWT, пароли хешируются с помощью BCrypt.

## Веб-интерфейс

Проект включает HTML-страницы для каждой роли:

- `/login.html` - страница входа
- `/register.html` - страница регистрации
- `/admin.html` - панель администратора
- `/teacher.html` - панель преподавателя
- `/student.html` - панель студента

Все страницы автоматически отслеживают активность пользователя и выполняют автоматический выход при бездействии (3 минуты).

## Swagger документация

Проект включает Swagger/OpenAPI документацию для всех REST API эндпоинтов.

### Доступ к документации

После запуска приложения документация доступна по адресам:

- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html` или `http://localhost:8080/swagger-ui.html` (если настроено)
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Использование

1. Открой Swagger UI в браузере
2. Для тестирования защищённых эндпоинтов:
   - Сначала выполни `/api/v1/auth/login` для получения JWT токена
   - Нажми кнопку **"Authorize"** в правом верхнем углу
   - Вставь токен в формате: `Bearer YOUR_JWT_TOKEN`
   - Теперь можно тестировать все защищённые эндпоинты

### Документированные разделы

- **Аутентификация** - вход, регистрация, обновление токенов
- **Пользователи** - управление пользователями (ADMIN)
- **Категории** - управление категориями курсов (ADMIN)
- **Курсы** - управление курсами (TEACHER/ADMIN)
- **Записи на курсы** - запись студентов на курсы
- И другие разделы API


