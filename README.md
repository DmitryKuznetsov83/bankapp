# Intershop

## Как собирать приложение
- Используем maven wrapper
- Стандартно clean + install

## Как запускать приложение
- запускаем команду `docker-compose up`
- в контейнере поднимаются:
  - account-service
  - bank-db
  - bank_keycloak 
  - blocker-service
  - cash-service
  - config-server
  - discovery-server
  - exchange-generator-service
  - exchange-service
  - front
  - gateway-server
  - notification-service
  - transfer-service

## Как запустить UI приложения
- используем страничку http://localhost:8081
- в базе заведены 2 тестовый пользователя 
  - login = Thelma, password = Thelma
  - login = Louisa, password = Louisa
- если хотим завести нового пользователя используем страничку http://localhost:8081/signup

## Важно
После запуска docker-compose нужно подождать секунд 30 пока все сервисы 
придут в нормальное состояние и система начнет работать стабильно