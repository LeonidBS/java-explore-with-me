# Explore-with-me project.
#  **Общее описание** #
Выполнена в рамках дипломного проекта.
Позволяет пользователям делиться информацией об интересных событиях и находить компанию для участия в них

Приложение является многомодульным.

### Структура модулей ###

![Architecture](/resources/arch.png)

Работа раздлена на три этапа.
<br/>
## Этап 1. Сервис статистики ##


### База данных ###

![Architecture](/resources/stats.png)

Table endpoint_hits {  <br/>
id integer [primary key] <br/>
app varchar(255) [not null] <br/>
uri varchar [not null] <br/>
ip varchar(50) [not null] <br/>
fduration integer [not null] <br/>
mpa_id integer [not null] <br/>
} <br/><br/>




