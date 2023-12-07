Учебный проект c использованием Spring Webflux Security
================================


## Технологии, использованные в учебном проекте:
- Spring Boot 3
- Spring Security (JWT)
- Spring WebFlux
- Spring Data R2DBC
- MapStruct
- PostgreSQL
- Flyway

## История:
Когда пользователь пытается зарегистрироваться, то в этом случае путь является открытым “/api/v1/auth/register”. В случае логина пользователя, мы пытаемся получить аутентификацию на основании username и пароля в классе SecurityService, используя метод authenticate (пользователь есть в БД, является активным, пароли совпадают).
Если все условия проходят, то вызываем метод генерации токена generateToken в классе SecurityService. Он использует секретный ключ, который у нас имеется. Далее подписываем наш токен с помощью алгоритма PBKDF2 и подписи. Токен улетает пользователю.
Пользовательский токен проходит фильтр безопасности в WebSecurityConfig в методе securityWebFilterChain , где мы вытаскиваем из нашего запроса токен, а сам токен подлежит корректности и проверки времени жизни самого токена (в JwtHandler).
После подтверждения корректности создается объект UserAuthenticaionBearer и уходит наружу. Таким образом, получаем доступ к нашей системе даже если endpoint будет закрыт.

# cURL запросов:

## 1. Регистрация пользователя
```bash
curl --location 'http://localhost:8083/api/v1/auth/register' \
--header 'Content-Type: application/json' \
--data '{
    "username": "alexP",
    "password": "test",
    "first_name": "Alex",
    "last_name": "P"
}'
```

Пример ответа:
```json
{
  "user_id": 5,
  "username": "alexP",
  "role": "USER",
  "first_name": "Alex",
  "last_name": "P",
  "enabled": true,
  "created_at": "2023-12-07T21:05:24.078438",
  "updated_at": "2023-12-07T21:05:24.078454"
}
```

## 2. Аутентификация пользователя
```bash
curl --location 'http://localhost:8083/api/v1/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "username": "AlexP",
    "password": "P"
  }'
```

Пример ответа
```json
{
  "user_id": 5,
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1Iiwicm9sZSI6IlVTRVIiLCJpc3MiOiJhbGV4cGFyb2ciLCJleHAiOjE3MDE5NzU5NjUsImlhdCI6MTcwMTk3MjM2NSwianRpIjoiNWI3NThkYzQtNjUyMS00MjkyLTliZjAtNThhMDkyZTI4YWUwIiwidXNlcm5hbWUiOiJhbGV4UCJ9.S7jb83w10jOxx7BfrjsotQRRzpA4oEKb3HUi9uIj3Es",
  "issued_at": "2023-12-07T18:06:05.277+00:00",
  "expires_at": "2023-12-07T19:06:05.277+00:00"
}
```

## 3. Получение данных пользователя с использованием токена, полученного в предыдущем запросе

```bash
curl --location 'http://localhost:8083/api/v1/auth/info' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1Iiwicm9sZSI6IlVTRVIiLCJpc3MiOiJhbGV4cGFyb2ciLCJleHAiOjE3MDE5NzU5NjUsImlhdCI6MTcwMTk3MjM2NSwianRpIjoiNWI3NThkYzQtNjUyMS00MjkyLTliZjAtNThhMDkyZTI4YWUwIiwidXNlcm5hbWUiOiJhbGV4UCJ9.S7jb83w10jOxx7BfrjsotQRRzpA4oEKb3HUi9uIj3Es'
```

Пример ответа
```json
{
  "user_id": 5,
  "username": "alexP",
  "role": "USER",
  "first_name": "Alex",
  "last_name": "P",
  "enabled": true,
  "created_at": "2023-12-07T21:05:24.078438",
  "updated_at": "2023-12-07T21:05:24.078454"
}
```
