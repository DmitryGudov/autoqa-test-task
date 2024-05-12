# Процедура запуска автотестов

## Шаг 1

Открыть терминал с помощью сочетания клавиш `Ctrl + Alt + T` и перейти в директорию, куда будет клонироваться проект.

## Шаг 2

Необходимо склонировать удаленный репозиторий на свой ПК с помощью следующей команды:

```
git clone git@github.com:DmitryGudov/autoqa-test-task.git
```

## Шаг 3

Открыть склонированный проект в **IntelliJ IDEA**.

## Шаг 4

Перед запуском автотестов необходимо включить офисный VPN (если они будут запускаться **НЕ** из офиса), т.к. подключение
осуществляется к регресс-стенду.

## Шаг 5

Убедиться, что путь на приватный ключ в `DBConnectionManager.java` совпадает с вашим. В случае несовпадения - указать
актуальный.
> Ссылка на строчку в проекте:
https://github.com/DmitryGudov/autoqa-test-task/blob/cdd2239c53e22d4b6587317eee6df5dfeca2205f/src/test/java/managers/DBConnectionManager.java#L21C1

## Шаг 6

Запустить в терминале проекта команду для запуска автотестов и генерации отчета

```
mvn clean test && mvn allure:report
```

## Шаг 7

После завершения прогона перейти в директорию
`.../autoqa-test-task/target/site/allure-maven-plugin`
и открыть в браузере `Chrome` файл `index.html`.


# Отчет
![Снимок экрана от 2024-05-12 15-30-27](https://github.com/DmitryGudov/autoqa-test-task/assets/124876096/2e4e4c43-9433-4299-8a36-50e4292a8607)
![Снимок экрана от 2024-05-12 15-30-47](https://github.com/DmitryGudov/autoqa-test-task/assets/124876096/f04a8e90-d6e9-4dec-ba06-91093b11185e)
![Снимок экрана от 2024-05-12 15-30-56](https://github.com/DmitryGudov/autoqa-test-task/assets/124876096/049613ca-eed2-4b80-9686-5f4082477be6)

