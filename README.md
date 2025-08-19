# Bankapp

## Подготовка

### 1. Установите зависимости

Для запуска проекта вам понадобятся:

- Docker Desktop
- Включённый Kubernetes в Docker Desktop (настройка → Kubernetes → Enable Kubernetes)
- Установленный Git


### 2. Укажите ваш файл конфигурации `kubeconfig.yaml` для доступа к k8s кластеру
- возьмите ваш файл kubeconfig из C:\Users\User\\.kube\config
- удалите из него все что связано с minikube (если есть) должно остаться все что связано с docker-desktop
- замените файл jenkins\kubeconfig.yaml в проекте на ваш
- Jenkins будет использовать этот файл для доступа к Kubernetes


### 3. Смонтируйте тома для запуска Jenkins в контейнере
- откройте файл jenkins\docker-compose.yml в проекте
- отредактируйте строки
```bash
"C:/Users/Dmitry/dev/YP Java Middle/bankapp:/repo:rw"
"C:/Users/Dmitry/dev/YP Java Middle/bankapp/jenkins/kubeconfig.yaml:/root/.kube/config"
```
- в первой строке укажите путь к вашему репозиторию
- во второй строке укажите путь к файлу kubeconfig.yaml (то есть репозиторий + /jenkins/kubeconfig.yaml)


### 4. Установите Ingress Controller в кластер
Мы используем `ingress-nginx`. Установите его в кластер:

```bash

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx --namespace ingress-nginx --create-namespace --set controller.service.type=LoadBalancer
```

### 5. Запустите Jenkins

```bash
cd jenkins
docker compose up -d --build
```
- Возьмите из логов Jenkins сгенерированный пароль
- откройте Jenkins [http://localhost:8080](http://localhost:8080)
- Введите пароль
- Далее будет предложение установить плагины, игнорируйте его (просто закройте это окно), все нужные плагины уже установлены
- Далее нажимаем кнопку Start using Jenkins

### 6. Создайте Jenkins job
- Перейдите в Blue Ocean (http://localhost:8080/blue)
- Создайте сборочную линию
- Укажите Git как источник кода
- В URL репозитория укажите file:///repo
- Создайте конвеер
- Джоба должна сама запуститься
- Проконролруйте что джоба успешно выполнилась

### 7. Как запустить UI приложения
- используем страничку http://localhost:80
- в базе заведены 2 тестовый пользователя 
  - login = Thelma, password = Thelma
  - login = Louisa, password = Louisa
- если хотим завести нового пользователя используем страничку http://localhost:80/signup

## Важно
После запуска docker-compose нужно подождать секунд 30 пока все сервисы 
придут в нормальное состояние и система начнет работать стабильно