-- wipes out the "constant" tables --
delete
from privileges;
delete
from namespace_roles;
delete
from repository_roles;

-- inserts "constant" data that would not change at runtime, yet is required by other tables --
insert into privileges
values (0, 'none'),
       (1, 'read'),
       (2, 'write'),
       (4, 'admin');
insert into repository_roles
values (0, 'repository_user', 0),
       (1, 'sysadmin', 7);
insert into namespace_roles
values (0, 'none', 0),
       (1, 'model_viewer', 1),
       (2, 'model_creator', 3),
       (4, 'model_promoter', 3),
       (8, 'model_reviewer', 3),
       (16, 'model_publisher', 3),
       (32, 'namespace_admin', 7);