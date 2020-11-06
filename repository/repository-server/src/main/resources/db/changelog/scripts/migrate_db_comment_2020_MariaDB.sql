alter table comment change content content text char set utf8;
alter table comment drop column firstname;
alter table comment drop column lastname;
