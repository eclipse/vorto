/*
 This procedure is non-destructive.

 It performs the following operations:

    1) create_privileges
    Creates a new table where typical read/write/admin privileges are represented as constants,
    with a unique ID expressed as a 64 bit unsigned integer suitable for bitwise operations,
    and the name of the privilege as varchar for readability.

    Privileges will be used both by namespace-related roles, and repository-related roles.

    2) create_namespace_roles
    Creates a new table where user roles related to namespaces are represented as constants,
    with a unique ID expressed as a 64 bit unsigned integer suitable for bitwise operations,
    a privileges field representing the sum of permissions this role allows, and the name of the
    namespace role as varchar for readability.
    These roles match all of the previously designed roles, save for sysadmin -
    as the latter is not bound to a specific namespace but to the repository (see next step).

    3) create_repository_roles
    Creates a new table where user roles related to the repository in general are represented as
    constants, with a unique ID expressed as a 64 bit unsigned integer suitable for bitwise
    operations, a privileges field representing the sum of permissions this role allows,
    and the name of the repository role as varchar for readability.
    There are only two repository roles available at the time of writing: user and sysadmin.

    4) create_user_namespace_roles
    Creates a table expressing the relationship between users and namespaces, by composite
    user-namespace id, and a role field expressing the sum of roles a given user has on a given
    namespace. The available roles reference the namespace_roles table.

    5) pre_populate_user_namespace_roles
    Pre-populates the table by filling in the user-namespace ids for each user-namespace association
    present in the user_role table.

    6) populate_user_namespace_roles
    Populates the actual roles by iterating over the user_role table and computing the role number
    for each user-namespace association.
    This is, by far, the most complex operation as the user_role table expresses the roles in
    multiple records, while the user_namespace_roles will only have one record per user-namespace
    association, hence an explicit cursor is required.
    Once this procedure has finalized, one can run the debug_user_namespace_roles procedure
    optionally, in order to create a debug table that displays user-namespace role relationships in
    human-readable form.
    The debug table itself is populated on-demand only, and serves no othe purpose than verifying
    data consistency after running the procedure.

    7) create_user_repository_roles
    Creates a table expressing the roles users have on the repository. The standard user role is
    implied at this time, and does not require a record (so this table will NOT be populated with
    a row per every repository user).
    The current functionality of this table will be to list users with the sysadmin repository role.
    Therefore, it will be populated by collecting records from the user_role table where the user
    has the SYS_ADMIN role.
    At application level, checks for sysadmin role will query the table AND the role, not just the
    presence of a record for a given user, so in the future, if there are other repository roles,
    the business logic will remain as is.
    The population of the table data is in the same procedure.

    Notes:

        a) Some role names will change for harmonization, e.g. the TENANT_ADMIN role becomes
            namespace_admin, etc.
 */

# 1) create_privileges
drop procedure if exists create_privileges;

create procedure create_privileges()
begin
    declare privileges tinyint;
    set privileges = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'privileges'
    );
    if (privileges = 0) then
        # creates the table
        create table privileges
        (
            # privilege value is a power of 2
            privilege bigint      not null primary key unique check ( privilege & (privilege - 1) = 0 ),
            name      varchar(64) not null unique
        );
        # populates with "harmonized" values
        insert into privileges values (1, 'read');
        insert into privileges values (2, 'write');
        insert into privileges values (4, 'admin');
    end if;
end;

call create_privileges();

# 2) create_namespace_roles
drop procedure if exists create_namespace_roles;

create procedure create_namespace_roles()
begin
    declare namespace_roles tinyint;
    set namespace_roles = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'namespace_roles'
    );
    if (namespace_roles = 0) then
        create table namespace_roles
        (
            # role is a power of 2
            role       bigint      not null primary key unique check ( role & (role - 1) = 0 ),
            name       varchar(64) not null unique,
            privileges bigint      not null default 0
        );
        # populates with "harmonized" values
        # model_view has read privilege
        insert into namespace_roles values (1, 'model_viewer', 1);
        # model_write etc. have read/write privileges, aka 1 + 2 == 3
        insert into namespace_roles values (2, 'model_creator', 3);
        insert into namespace_roles values (4, 'model_promoter', 3);
        insert into namespace_roles values (8, 'model_reviewer', 3);
        insert into namespace_roles values (16, 'model_publisher', 3);
        # namespace_admin has read/write and admin privileges, aka 1 + 2 + 4 == 7
        insert into namespace_roles values (32, 'namespace_admin', 7);
    end if;
end;

call create_namespace_roles();

# 3) create_repository_roles
drop procedure if exists create_repository_roles;

create procedure create_repository_roles()
begin
    declare repository_roles tinyint;
    set repository_roles = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'repository_roles'
    );
    if (repository_roles = 0) then
        create table repository_roles
        (
            # role is a power of 2
            role       bigint      not null primary key unique check ( role & (role - 1) = 0 ),
            name       varchar(64) not null unique,
            privileges bigint      not null default 0
        );
        # populates with "harmonized" values
        # repository_user has no special privileges, and their repository role is implied normally,
        # i.e. not even persisted in the user_repository_roles table
        insert into repository_roles values (0, 'repository_user', 0);
        # sysadmin has read/write/admin privileges - for now this is a bit over-generalized, but
        # things might be fine-tuned once other repository-scoped roles emerge
        insert into repository_roles values (1, 'sysadmin', 7);
    end if;
end;

call create_repository_roles();

# 4) create_user_namespace_roles
drop procedure if exists create_user_namespace_roles;

create procedure create_user_namespace_roles()
begin
    declare user_namespace_roles tinyint;
    set user_namespace_roles = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'user_namespace_roles'
    );
    if (user_namespace_roles = 0) then
        # creates the table
        create table user_namespace_roles
        (
            user_id      bigint not null,
            namespace_id bigint not null,
            roles        bigint not null default 0,
            primary key (user_id, namespace_id),
            foreign key (user_id) references user (id),
            foreign key (namespace_id) references namespace (id)
        );
    end if;
end;

call create_user_namespace_roles();

# 5) pre_populate_user_namespace_roles
drop procedure if exists pre_populate_user_namespace_roles;

create procedure pre_populate_user_namespace_roles()
begin
    # this defines the limit to the query to user_role, in order to ensure the whole
    # table is traversed and not the first n (e.g. 500) records
    declare max_limit bigint;
    select count(id) from user_role into max_limit;
    insert into user_namespace_roles (user_id, namespace_id)
    select tu.user_id, n.id
    from user_role
             inner join tenant_user tu on tenant_user_id = tu.id
             inner join tenant t on tu.tenant_id = t.id
             inner join user u on tu.user_id = u.id
             inner join namespace n on tu.tenant_id = n.tenant_id
         # ignoring SYS_ADMIN roles
    where role != 'SYS_ADMIN'
    limit max_limit
    on duplicate key update user_id = user_namespace_roles.user_id;

end;

call pre_populate_user_namespace_roles();

# 6) populate_user_namespace_roles - optionally, run the debug_user_namespace_roles script after
# this one, in order to verify the data has been populated correctly
drop procedure if exists populate_user_namespace_roles;

# this one requires an explicit cursor, as it iterates over all user_role records and
# updates same user_permission records multiple times
create procedure populate_user_namespace_roles()
begin
    declare done tinyint default false;
    declare c_user_id bigint;
    declare c_namespace_id bigint;
    # the old-notation role from user_role
    declare c_old_role varchar(255);
    # the new permission which might be overwritten by each iteration for same namespace/user records
    # (which means it need to be operated upon to sum)
    declare thecursor cursor for select u.id, n.id, user_role.role
                                 from user_role
                                          inner join tenant_user tu on tu.id = tenant_user_id
                                          inner join tenant t on tu.tenant_id = t.id
                                          inner join namespace n on t.id = n.tenant_id
                                          inner join user u on tu.user_id = u.id
                                          inner join user_namespace_roles un
                                                     on un.namespace_id = n.id and un.user_id = u.id
                                 limit 2000;
    declare continue handler for not found set done = true;

    open thecursor;

    read_loop:
    loop
        # fetches each record one by one
        fetch thecursor into c_user_id, c_namespace_id, c_old_role;
        # break condition
        if done then
            leave read_loop;
        end if;

        /*
        Converts old varchar single role entries into numeric roles,
        summing with the existing value which default to 0 if none present.
        Here, the conversion e.g. from 'USER' to 1 is hard-coded, as there
        is no point querying the permissions table.
        */

        case (c_old_role)

            when ('USER') then update user_namespace_roles
                               set roles = roles + 1
                               where user_id = c_user_id
                                 and namespace_id = c_namespace_id;
            when ('MODEL_CREATOR') then update user_namespace_roles
                                        set roles = roles + 2
                                        where user_id = c_user_id
                                          and namespace_id = c_namespace_id;
            when ('MODEL_PROMOTER') then update user_namespace_roles
                                         set roles = roles + 4
                                         where user_id = c_user_id
                                           and namespace_id = c_namespace_id;
            when ('MODEL_REVIEWER') then update user_namespace_roles
                                         set roles = roles + 8
                                         where user_id = c_user_id
                                           and namespace_id = c_namespace_id;
            when ('MODEL_PUBLISHER') then update user_namespace_roles
                                          set roles = roles + 16
                                          where user_id = c_user_id
                                            and namespace_id = c_namespace_id;
            when ('TENANT_ADMIN') then update user_namespace_roles
                                       set roles = roles + 32
                                       where user_id = c_user_id
                                         and namespace_id = c_namespace_id;
            # no handling for different cases such as 'SYS_ADMIN' here
            else begin
            end;

            end case;

    end loop;

    close thecursor;
end;

call populate_user_namespace_roles();

# 7) create_user_repository_roles
drop procedure if exists create_user_repository_roles;

create procedure create_user_repository_roles()
begin
    declare user_repository_roles tinyint;
    declare sysadmin_role int;
    set user_repository_roles = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'user_repository_roles'
    );
    set sysadmin_role = 7;
    if (user_repository_roles = 0) then
        create table user_repository_roles
        (
            user_id bigint primary key not null,
            roles   bigint             not null default 0,
            foreign key (user_id) references user (id)
        );
        insert into user_repository_roles
        select user_id, 7
        from tenant_user
                 inner join user_role ur on tenant_user.id = ur.tenant_user_id
        where role = 'SYS_ADMIN';
    end if;
end;

create procedure add_workspace_id_and_populate()
begin
    alter table namespace add column workspace_id varchar(255) not null default 'undefined';
    update namespace set workspace_id = (select tenant_id from tenant where id = namespace.tenant_id);
end;