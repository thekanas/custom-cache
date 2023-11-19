# Task
1. Создать любой gradle проект
2. Проект должен быть совместим с java 17
3. Придерживаться GitFlow: master -> develop -> feature/fix
4. Создать реализацию кэша, используя алгоритмы LRU и LFU
5. Создать в приложении слои service и dao (service будет вызывать слой dao, слой dao будет временная замена database). В этих сервисах реализовать CRUD операции для работы с entity. Работу организовать через интерфейсы.
6. Результат работы dao должен синхронизироваться с кешем через proxy (или кастомная аннотация, или АОП/aspectj). При работе с entity оперируем id. Алгоритм работы с кешем:  
GET - ищем в кеше и если там данных нет, то достаем объект из dao, сохраняем в кеш и возвращаем  
POST - сохраняем в dao и потом сохраняем в кеше  
DELETE - удаляем из dao и потом удаляем из кеша  
PUT - обновление/вставка в dao и потом обновление/вставка в кеше
7. Алгоритм и максимальный размер коллекции должны читаться из файла resources/application.yml
8. Создать entity, в нем должно быть поле id и еще минимум 4 поля
9. Service работает с dto
10. Объекты (dto), которые принимает service, должны валидироваться. В т.ч. добавить regex валидацию
11. Кеши должны быть покрыты unit tests
12. Должен содержать javadoc и описанный README.md
13. Использовать lombok
14. Реализовать метод для получения информации в формате xml

Доп. задание:  
*** Самописный JsonParser подтягивать как библиотеку и парсировать json через него  
***В самописный JsonParser добавить возможность работы с xml 
___
Используемый стек
---
В проекте использован следующий стек технологий:
1. Java 17
2. Gradle
3. PostgreSQL
4. JDBS
5. Lombok
6. Mapstruct
7. Aspectj
8. Junit
9. Mockito
10. Liquibase
___

* Создан gradle проект совместимый с java 17. Работа велась по GitFlow.
* В проекте реализованы LRU и LFU алгоритмы кеширования.
* Слои сервиса и дао реализуют CRUD операции.
* Результат работы dao синхронизируется с кешем через proxy (АОП:aspectj).
* Алгоритм и максимальный размер коллекции читаются из файла resources/application.yml
* Объекты dto, которые принимает service, проверяются валидатором.
* Тестами покрыты кеши, сервис
* Написан javaDoc.
* В папку libs добавлен самописный json parser.
* Демонстрацию работы проекта можно посмотреть запустив метод main класса Runner.java.

Инструкция по запуску
-

* В файле src/main/resources/application.yml указать необходимые данные для подключения к базе данных.
* Создать базу данных (custom_cache по умолчанию).
* Запустить метод main класса Runner.java. 


