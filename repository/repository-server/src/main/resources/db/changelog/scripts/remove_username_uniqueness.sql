# this searches for, and builds a DDL update with, the username uniqueness constraint in the user
# table (here referenced as index)
# once the update is built, it is executed as a prepared statement with no variables
set @theFullUpdate = (select concat('alter table user drop index ', constraint_name) as theUpdate from information_schema.KEY_COLUMN_USAGE where TABLE_NAME ='user' and COLUMN_NAME = 'username');
prepare statement from @theFullUpdate;
execute statement;
deallocate prepare statement;
