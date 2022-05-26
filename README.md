# Task management system

![Example 1](doc/example1.gif?raw=true)
![Example 2](doc/example2.gif?raw=true)

В проекте реализуется **Система управления задачами**. Функционал схож с функционалом Jira.

#### Функционал:
    - Создание, редактирование, просмотр задач;
    - Изменение статуса и приоритета задач;
    - Камментирование задач;
    - Интерфейс просмтора всех задач с сортировкой по проиоритету и статусу.

#### Особенности:
    - Деплой docker-compose;
    - Backend REST API;
    - Frontend SPA на VueJs;
    - Поддержка роутинга в SPA с помощью Vue Router.

#### Стэк:
    - Backend: Spring Boot, Spring MVC, Spring Data, Spring Security;
    - Frontend: VueJs, Vue Router, Vuex;
    - Дополнительно: Docker, Docker compose, Nginx (Для frontend`а), PostgreSQL (Основная БД).

#### Deploy:
```
docker-compose build
docker-compose up -d
```

> Credentials пользователей для теста:
> * `jack@white.ru:password` - Jack White
> * `john@doe.ru:password` - John Doe
> * `vincent@vega.ru:password` - Vincent Vega
> * `jack@black.ru` - Jack Black
