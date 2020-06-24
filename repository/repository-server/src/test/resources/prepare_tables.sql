-- Wipes out the tables
-- The delete statements must be executed in this order, due to foreign key constraints.
-- Once the tenant, tenant_user and user_role tables are deleted, this should be adapted accordingly.

delete
from privileges;
delete
from namespace_roles;
delete
from repository_roles;
delete
from user_namespace_roles;
delete
from user_repository_roles;
delete
from namespace;
delete
from user;

-- inserts "constant" data that would not change at runtime, yet is required by other tables --
insert into privileges
values (1, 'readonly'),
       (2, 'readwrite'),
       (4, 'admin');
insert into repository_roles
values (0, 'repository_user', 0),
       (1, 'sysadmin', 7);
insert into namespace_roles
values (1, 'model_viewer', 1),
       (2, 'model_creator', 3),
       (4, 'model_promoter', 3),
       (8, 'model_reviewer', 3),
       (16, 'model_publisher', 3),
       (32, 'namespace_admin', 7);

-- Creates users - this cannot be performed after other scripts or code create users, as it
-- uses a fixed id for the users created, thus allowing to make one of them sysadmin
insert into user (id, ack_of_terms_and_cond_timestamp, date_created, last_updated, username,
                  is_technical_user, authentication_provider_id, subject)
values (1, now(), now(), now(), 'userSysadmin', 0, 'GITHUB', 'none'),
       (2, now(), now(), now(), 'userSysadmin2', 0, 'GITHUB', 'none'),
       (3, now(), now(), now(), 'userModelCreator', 0, 'GITHUB', 'none'),
       (4, now(), now(), now(), 'userModelCreator2', 0, 'GITHUB', 'none'),
       (5, now(), now(), now(), 'userModelCreator3', 0, 'GITHUB', 'none'),
       (6, now(), now(), now(), 'userModelViewer', 0, 'GITHUB', 'none');
-- makes the sysadmin user sysadmin
insert into user_repository_roles
values (1, 7);
