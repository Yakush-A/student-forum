# Student Forum

Проект представляет собой веб-форум для студентов, вдохновлённый идеей
StackOverflow. Пользователи могут создавать публикации, обсуждать их
в комментариях и структурировать контент с помощью категорий и тегов.

## Возможности

- регистрация и аутентификация пользователей
- создание, редактирование и удаление публикаций
- комментарии к публикациям
- система категорий и тегов
- базовая система рекомендаций для продвижения статей

## Технологии

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Gradle

## Модель данных

В проекте используются следующие основные сущности:

- **User** — пользователь системы
- **Post** — публикация
- **Comment** — комментарий к публикации
- **Category** — категория постов
- **Tag** — тег для классификации постов
- **Role** - роли пользователей

Примеры связей:

- `User → Post` — OneToMany (автор постов)
- `Post → Comment` — OneToMany
- `Post ↔ Tag` — ManyToMany
- `Post → Category` — ManyToOne

## Качество кода

### Checkstyle

В проекте используется конфигурация **Sun Checks** с небольшими модификациями.

### SonarQube Cloud

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Yakush-A_student-forum&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Yakush-A_student-forum)

## Сборка и запуск

### Сборка проекта

```bash
./gradlew clean build