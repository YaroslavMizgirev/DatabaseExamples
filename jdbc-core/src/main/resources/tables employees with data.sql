create table employees (id int4 primary key, firstname varchar(50), lastname varchar(75), department varchar(20), salary numeric(15,2));

alter table employees modify column department varchar(50);

insert into employees (id, lastname, firstname, department, salary) values
    (1, "Дементьев", "Варлам", "Кинезитерапевт", 35922.77),
    (2, "Мышкин", "Карл", "Фермер", 54922.77),
    (3, "Веселов", "Мечеслав", "Полицейский", 39936.34),
    (4, "Фомин", "Митрофан", "Судебный пристав", 13764.94),
    (5, "Носков", "Владимир", "Табаковод", 26675.34),
    (6, "Васильев", "Феликс", "Дерматолог", 12420.93),
    (7, "Логинов", "Абрам", "Борт-радист", 7010.43),
    (8, "Филиппов", "Мартын", "Шахтёр", 7274.51),
    (9, "Кузьмин", "Герман", "Стоматолог", 6515.56),
    (10, "Зуев", "Альфред", "Журналист", 6316.91);

select * from employees;
