# the following is optional and can be run to ensure that the debug_user_namespace_privileges table is populated
# correctly after running migrate_db_from_013_MariaDB_preparation.sql

# creates the debug table
drop procedure if exists create_debug_user_namespace_privileges_table;
create procedure create_debug_user_namespace_privileges_table()
begin
drop table if exists debug_user_namespace_privileges;
create table debug_user_namespace_privileges (
    user_id bigint,
    username varchar(255),
    namespace_id bigint,
    namespace_name varchar(255),
    p_view boolean default false,
    p_create boolean default false,
    p_promote boolean default false,
    p_review boolean default false,
    p_publish boolean default false,
    p_admin boolean default false
);

end;

call create_debug_user_namespace_privileges_table();

# populates the debug table
drop procedure if exists debug_user_namespace_privileges;
create procedure debug_user_namespace_privileges()
begin
    declare done tinyint default false;
    declare c_user_id bigint;
    declare c_username varchar(255);
    declare c_namespace_id bigint;
    declare c_namespace_name varchar(255);
    declare c_role bigint;
    declare c_view boolean;
    declare c_create boolean;
    declare c_promote boolean;
    declare c_review boolean;
    declare c_publish boolean;
    declare c_admin boolean;
    declare thecursor cursor for select user_id, u.username, namespace_id, n.name, role
    from user_namespace_roles
             inner join user u on user_id = u.id
             inner join namespace n on n.id = namespace_id;
    declare continue handler for not found set done = true;
    open thecursor;

    insert_loop: loop
        fetch thecursor into c_user_id, c_username, c_namespace_id, c_namespace_name, c_role;
        # break condition
        if done then
            leave insert_loop;
        end if;
        set c_view = (c_role & 1 = 1);
        set c_create = (c_role & 2 = 2);
        set c_promote = (c_role & 4 = 4);
        set c_review = (c_role & 8 = 8);
        set c_publish = (c_role & 16 = 16);
        set c_admin = (c_role & 32 = 32);
        insert into debug_user_namespace_privileges values(c_user_id, c_username, c_namespace_id, c_namespace_name, c_view, c_create, c_promote, c_review, c_publish, c_admin);
    end loop;
    close thecursor;
end;

call debug_user_namespace_privileges();