# SQLcmd - консольный клиент для выполнения CRUD-запросов в PostgreSQL
***(учебный проект на курсе [Juja](https://juja.com.ua/))***
***
##### Для работы с проектом необходимо:
1. JDK 8
2. IntelliJ IDEA
3. PostgreSQL server
##### Для прохождения тестов необходимо:
1. Создать пустую базу данных
2. В классе Config укзать в соответствующих полях имя созданной базы данных, логин и пароль.
##### Реализованы следующие консольные команды:
* **сonnect**

        Описание: Команда для подключения к соответствующей БД

        Формат ввода: connect|database|username|password
            database - имя БД
            username - имя пользователя БД
            password - пароль пользователя БД

        Формат вывода: текстовое сообщение с результатом выполнения операции

* **tables**

        Описание: Команда выводит список всех таблиц

        Формат ввода: tables

        Формат вывода: Перечень всех таблиц существующих в базе данных

* **clear**

        Описание: Команда очищает все данные из указанной таблицы

        Формат ввода: clear|tableName
            tableName - имя очищаемой таблицы

        Формат вывода: текстовое сообщение с результатом выполнения операции
* **drop**

        Описание: Команда удаляет заданную таблицу

        Формат ввода: drop|tableName
            tableName - имя удаляемой таблицы

        Формат вывода: текстовое сообщение с результатом выполнения операции
* **create**

        Описание: Команда создает новую таблицу с заданными полями

        Формат ввода: create|tableName|column1|type1|column2|type2|...|columnN|typeN
            tableName - имя таблицы
            column1   - имя первого столбца записи
            type1     - тип данных первого столбца записи
            column2   - имя второго столбца записи
            type2     - тип данных второго столбца записи
            columnN   - имя n-го столбца записи
            typeN     - тип данных n-го столбца записи

        Формат вывода: текстовое сообщение с результатом выполнения операции
* **find**

        Описание: Команда для печати таблицы в консоль

        Формат ввода: find|tableName
            tableName - имя удаляемой таблицы

        Формат вывода: Отформатированная печать таблицы в консоль с разметкой
* **insert**

        Описание: Команда для вставки одной строки в заданную таблицу

        Формат ввода: insert|tableName|column1|value1|column2|value2|...|columnN|valueN
            tableName  - имя таблицы
            column1    - имя первого столбца записи
            value1     - значение первого столбца записи
            column2    - имя второго столбца записи
            value2     - значение второго столбца записи
            columnN    - имя n-го столбца записи
            valueN     - значение n-го столбца записи

        Формат вывода: текстовое сообщение с результатом выполнения операции
* **update**

        Описание: Команда обновит запись в указанной таблице, установив значение column1 = value1, ..., columnN = valueN для которых соблюдается условие column = value

        Формат ввода: update|tableName|column|value|column1|value1|column2|value2|...|columnN|valueN
            tableName  - имя таблицы
            column     - имя проверяемого столбца
            value      - значение проверяемого столбца
            column1    - имя первого обновляемого столбца
            value1     - значение первого обновляемого столбца
            column2    - имя второго обновляемого столбца
            value2     - значение второго обновляемого столбца
            columnN    - имя n-го обновляемого столбца
            valueN     - значение n-го обновляемого столбца

        Формат вывода: печать обновленной таблицы в консоль
* **delete**

        Описание: Команда удаляет одну или несколько записей в указанной таблице для которых соблюдается условие column = value

        Формат ввода: delete|tableName|column|value
            tableName  - имя таблицы
            column     - имя проверяемого столбца
            value      - значение проверяемого столбца

        Формат вывода: печать обновленной таблицы в консоль
* **help**

        Описание: Команда выводит в консоль список всех доступных команд и описания к ним

        Формат ввода: help

        Формат вывода: Перечень всех команд и описания к ним

* **exit**

        Описание: Команда для отключения от БД и выход из приложения

        Формат ввода: exit

        Формат вывода: текстовое сообщение с результатом выполнения операции

