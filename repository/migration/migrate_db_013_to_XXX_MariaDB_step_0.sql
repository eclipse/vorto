/*
 This procedure is non-destructive.

 It performs the following operations:

    1) refactor_namespace_ownership_reference
    Modifies the namespace table by replacing the tenant foreign key with a foreign
    key owner_user_id pointing directly to the user who owns the namespace.

    2) create_privileges
    Creates a new table where typical read/write/admin privileges are represented as constants,
    with a unique ID expressed as a 64 bit unsigned integer suitable for bitwise operations,
    and the name of the privilege as varchar for readability.

    Privileges will be used both by namespace-related roles, and repository-related roles.

    3) create_namespace_roles
    Creates a new table where user roles related to namespaces are represented as constants,
    with a unique ID expressed as a 64 bit unsigned integer suitable for bitwise operations,
    a privileges field representing the sum of permissions this role allows, and the name of the
    namespace role as varchar for readability.
    These roles match all of the previously designed roles (plus "none"), save for sysadmin -
    as the latter is not bound to a specific namespace but to the repository (see next step).

    4) create_repository_roles
    Creates a new table where user roles related to the repository in general are represented as
    constants, with a unique ID expressed as a 64 bit unsigned integer suitable for bitwise
    operations, a privileges field representing the sum of permissions this role allows,
    and the name of the repository role as varchar for readability.
    There are only three repository roles available at the time of writing: "none" (e.g. for
    anonymous users), user and sysadmin.

    5) create_user_namespace_roles
    Creates a table expressing the relationship between users and namespaces, by composite
    user-namespace id, and a role field expressing the sum of roles a given user has on a given
    namespace. The available roles reference the namespace_roles table.

    6) pre_populate_user_namespace_roles
    Pre-populates the table by filling in the user-namespace ids for each user-namespace association
    present in the user_role table.

    7) populate_user_namespace_roles
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

    8) create_user_repository_roles
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

        a) There will still be some redundancy between the namespace.owner_user_id field, which denotes
            ownership and therefore implies the role formerly known as TENANT_ADMIN (now namespace_admin),
            and the actual namespace_admin role for that user in the namespace_user_roles table.
        b) When deleting a user account where the user's namespace(s) has other users with the
            namespace_admin role (which is the pre-condition to delete the account), the ownership of
            the namespace will be "orphaned", as the application may not be able to choose a next
            administrator among users with the appropriate permission.
            However, since the ownership does not differ practically from having the namespace_admin
            role (and there could be multiple other users with that role on the orphaned
            namespace), this should pose no issue.
        c) Some role names will change for harmonization, e.g. the TENANT_ADMIN role becomes
            namespace_admin, etc.
 */

# 1) refactor_namespace_ownership_reference
drop procedure if exists refactor_namespace_ownership_reference;

create procedure refactor_namespace_ownership_reference()
begin
    declare user_in_namespace_count tinyint;
    # Checks if the namespace table already has a owner_user_id field
    set user_in_namespace_count = (
        select count(*)
        from information_schema.COLUMNS
        where TABLE_NAME = 'namespace'
          and COLUMN_NAME = 'owner_user_id'
    );
    /*
        Assuming the namespace table does not already have a user field, this does two things:

            1) Creates a user field in the namespace table with reference to the primary key in the user table
            2) Updates the values of user in namespace to match the user id referenced in tenant_user,
            through a nested inner join

        Note:
            The owner_id of the tenant table is unfortunately missing data, so it is not usable
            hence, the nexted inner join). Likely the historical reason is that the owner_id field
            has been abandoned back then, in favor of the intermediary table tenant_user, which we are
            ultimately going to remove again.
     */
    if (user_in_namespace_count = 0) then
        alter table namespace
            ADD COLUMN owner_user_id bigint;
        alter table namespace
            add constraint user foreign key (owner_user_id) references user (id);
        update namespace n
            inner join tenant t on t.id = n.tenant_id
            inner join tenant_user tu on t.id = tu.tenant_id
        set n.owner_user_id = tu.user_id;

    end if;
end;

call refactor_namespace_ownership_reference();

# 2) create_privileges
drop procedure if exists create_privileges;

create procedure create_privileges()
begin
    declare privileges tinyint;
    # Checks if the namespace table already has a owner_user_id field
    set privileges = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'privileges'
    );
    if (privileges = 0) then
        # creates the table
        create table privileges
        (
            # privilege value is a power of 2 or 0 for none
            privilege bigint      not null primary key unique check ( privilege & (privilege - 1) = 0 ),
            name      varchar(64) not null unique
        );
        # populates with "harmonized" values
        insert into privileges values (0, 'none');
        insert into privileges values (1, 'read');
        insert into privileges values (2, 'write');
        insert into privileges values (4, 'admin');
    end if;
end;

call create_privileges();

# 3) create_namespace_roles
drop procedure if exists create_namespace_roles;

create procedure create_namespace_roles()
begin
    declare namespace_roles tinyint;
    # Checks if the user_permissions table already has a owner_user_id field
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
        insert into namespace_roles values (0, 'none', 0);
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

# 4) create_repository_roles
drop procedure if exists create_repository_roles;

create procedure create_repository_roles()
begin
    declare repository_roles tinyint;
    # Checks if the user_permissions table already has a owner_user_id field
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

# 5) create_user_namespace_roles
drop procedure if exists create_user_namespace_roles;

create procedure create_user_namespace_roles()
begin
    declare user_namespace_roles tinyint;
    # Checks if the user_permissions table already has a owner_user_id field
    set user_namespace_roles = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'user_namespace_roles'
    );
    if (user_namespace_roles = 0) then
        # creates the table
        create table user_namespace_roles
        (
            user_id      bigint not null,
            namespace_id bigint not null,
            role         bigint not null default 0,
            primary key (user_id, namespace_id),
            foreign key (user_id) references user (id),
            foreign key (namespace_id) references namespace (id)
        );
    end if;
end;

call create_user_namespace_roles();

# 6) pre_populate_user_namespace_roles
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

# 7) populate_user_namespace_roles - optionally, run the debug_user_namespace_roles script after
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
                               set role = role + 1
                               where user_id = c_user_id
                                 and namespace_id = c_namespace_id;
            when ('MODEL_CREATOR') then update user_namespace_roles
                                        set role = role + 2
                                        where user_id = c_user_id
                                          and namespace_id = c_namespace_id;
            when ('MODEL_PROMOTER') then update user_namespace_roles
                                         set role = role + 4
                                         where user_id = c_user_id
                                           and namespace_id = c_namespace_id;
            when ('MODEL_REVIEWER') then update user_namespace_roles
                                         set role = role + 8
                                         where user_id = c_user_id
                                           and namespace_id = c_namespace_id;
            when ('MODEL_PUBLISHER') then update user_namespace_roles
                                          set role = role + 16
                                          where user_id = c_user_id
                                            and namespace_id = c_namespace_id;
            when ('TENANT_ADMIN') then update user_namespace_roles
                                       set role = role + 32
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

# 8) create_user_repository_roles
drop procedure if exists create_user_repository_roles;

create procedure create_user_repository_roles()
begin
    declare user_repository_roles tinyint;
    declare sysadmin_role int;
    # Checks if the namespace table already has a owner_user_id field
    set user_repository_roles = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'user_repository_roles'
    );
    set sysadmin_role = 7;
    if (user_repository_roles = 0) then
        create table user_repository_roles
        (
            user_id bigint primary key not null,
            role    bigint             not null default 0,
            foreign key (user_id) references user (id)
        );
        insert into user_repository_roles
        select user_id, 7
        from tenant_user
                 inner join user_role ur on tenant_user.id = ur.tenant_user_id
        where role = 'SYS_ADMIN';
    end if;
end;