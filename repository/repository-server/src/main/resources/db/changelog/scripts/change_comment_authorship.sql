alter table comment add column author_id bigint;
alter table comment add constraint foreign key (author_id) references user (id);
update comment set author_id = (select id from user where comment.author = username);
alter table comment drop author;
