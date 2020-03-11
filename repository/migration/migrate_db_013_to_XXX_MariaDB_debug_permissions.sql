# the following is optional and can be run to ensure that the user_permissions table is populated
# correctly after running migrate_db_013_to_XXX_MariaDB_step_0.sql

# creates the debug table
drop procedure if exists create_permissions_debug_table;
create procedure create_permissions_debug_table()
begin
drop table if exists debug_permissions;
create table debug_permissions (
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

call create_permissions_debug_table();

# populates the debug table
drop procedure if exists debug_permissions;
create procedure debug_permissions()
begin
    declare done tinyint default false;
    declare c_user_id bigint;
    declare c_username varchar(255);
    declare c_namespace_id bigint;
    declare c_namespace_name varchar(255);
    declare c_permission bigint;
    declare c_view boolean;
    declare c_create boolean;
    declare c_promote boolean;
    declare c_review boolean;
    declare c_publish boolean;
    declare c_admin boolean;
    declare thecursor cursor for select user_id, u.username, namespace_id, n.name, permission
    from user_permissions
             inner join user u on user_id = u.id
             inner join namespace n on n.id = namespace_id;
    declare continue handler for not found set done = true;
    open thecursor;

    insert_loop: loop
        fetch thecursor into c_user_id, c_username, c_namespace_id, c_namespace_name, c_permission;
        # break condition
        if done then
            leave insert_loop;
        end if;
        set c_view = (c_permission & 1 = 1);
        set c_create = (c_permission & 2 = 2);
        set c_promote = (c_permission & 4 = 4);
        set c_review = (c_permission & 8 = 8);
        set c_publish = (c_permission & 16 = 16);
        set c_admin = (c_permission & 32 = 32);
        insert into debug_permissions values(c_user_id, c_username, c_namespace_id, c_namespace_name, c_view, c_create, c_promote, c_review, c_publish, c_admin);
    end loop;
    close thecursor;
end;

call debug_permissions();