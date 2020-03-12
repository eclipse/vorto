/*
 This procedure is non-destructive.

 It performs the following operations:

    1) refactor_namespace_ownership_reference
    Modifies the namespace table by replacing the tenant foreign key with a foreign
    key owner_user_id pointing directly to the user who owns the namespace.

    2) create_permissions
    Creates a new table where currently supported permissions by namespace are expressed as constants,
    with a unique ID expressed as a 64 bit unsigned integer for bitwise operations, and the name
    of the permission as varchar for readability.

    The term "permission" replaces the term "role" as more appropriated to the context (which also
    leaves room for creating actual roles, standard or user-defined, in the far future).
    Some permission names change cosmetically to reflect the move away from "tenancy" and to
    harmonize naming conventions.

    3) create_user_permissions
    Creates and a new user_permissions table (to ultimately replace user_role), with the following
    DDL:

    | Compound ID                                           | Permissions as bigint
    | user_id (FK user.id) + namespace_id (FK namespace.id) | A sum of the user's permissions on
    |                                                       | the given namespace


    This should decrease the number of write operations when either deleting a user or
    a namespace, as well as read a user's permissions on a namespace in one single record.
    SYS_ADMIN users will be removed from the table and managed in a separate table (see #3), since
    that is not a permission tied to a specific namespace, but rather a system-wide role.

    4) pre_populate_user_permissions
    Pre-populates the user_permissions table with user-namespace associations without computing the
    permission yet.
    This is necessary as the iteration of user_role multiple records per user/namespace association
    will require a cursor and its own standalone procedure.

    5) populate_user_permissions
    Fetches and aggreates records from the user_role table to compute the specific permission for
    each user/namespace association.

    6) create_and_populate_sysadmins
    Creates a sysadmins table by fetching records containing the 'SYS_ADMIN' role in the user_role
    table.
    The table will only have one column expressing the user_id as both primary key and foreign key
    from the user table, since it does not express a user/namespace relationship but rather a list
    of privileged users who can administrate the system and operate with top privileges in every
    area.

    Notes:

        a) There will still be some redundancy between the namespace.owner_user_id field, which denotes
            ownership and therefore implies the role formerly known as TENANT_ADMIN (now namespace_admin),
            and the actual namespace_admin role for that user in the user_permissions table.
        b) When deleting a user account where the user's namespace(s) has other users with the
            namespace_admin role (which is the pre-condition to delete the account), the ownership of
            the namespace will be "orphaned", as the application may not be able to choose a next
            administrator among users with the appropriate permission.
            However, since the ownership does not differ practically from having the namespace_admin
            permission (and there could be multiple other user with that permission on the orphaned
            namespace), this should pose no issue.
        c) As hinted above, the TENANT_ADMIN permission becomes namespace_admin (cosmetics)
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

# 2) create_permissions
drop procedure if exists create_permissions;

create procedure create_permissions()
begin
    declare permissions tinyint;
    # Checks if the namespace table already has a owner_user_id field
    set permissions = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'permissions'
    );
    if (permissions = 0) then
        # creates the table
        create table permissions
        (
            # permission value is a power of 2 or 0 for none
            permission bigint      not null primary key unique check ( permission & (permission - 1) = 0 ),
            name       varchar(64) not null unique
        );
        # populates with "harmonized" values
        insert into permissions values (0, 'none');
        insert into permissions values (1, 'model_view');
        insert into permissions values (2, 'model_create');
        insert into permissions values (4, 'model_promote');
        insert into permissions values (8, 'model_review');
        insert into permissions values (16, 'model_publish');
        insert into permissions values (32, 'namespace_admin');
    end if;
end;

call create_permissions();

# 3) create_user_permissions
drop procedure if exists create_user_permissions;

create procedure create_user_permissions()
begin
    declare user_permissions tinyint;
    # Checks if the user_permissions table already has a owner_user_id field
    set user_permissions = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'user_permissions'
    );
    if (user_permissions = 0) then
        # creates the table
        create table user_permissions
        (
            user_id      bigint not null,
            namespace_id bigint not null,
            permissions  bigint not null default 0,
            primary key (user_id, namespace_id),
            foreign key (user_id) references user (id),
            foreign key (namespace_id) references namespace (id)
        );
    end if;
end;

call create_user_permissions();

# 4) pre_populate_user_permissions
drop procedure if exists pre_populate_user_permissions;

create procedure pre_populate_user_permissions()
begin
    # this defines the limit to the query to user_role, in order to ensure the whole
    # table is traversed and not the first n (e.g. 500) records
    declare max_limit bigint;
    select count(id) from user_role into max_limit;
    insert into user_permissions (user_id, namespace_id)
    select tu.user_id, n.id
    from user_role
             inner join tenant_user tu on tenant_user_id = tu.id
             inner join tenant t on tu.tenant_id = t.id
             inner join user u on tu.user_id = u.id
             inner join namespace n on tu.tenant_id = n.tenant_id
    # ignoring SYS_ADMIN roles
    where role != 'SYS_ADMIN'
    limit max_limit
    on duplicate key update user_id = user_permissions.user_id;

end;

call pre_populate_user_permissions();

# 5) populate_user_permissions - optionally, run the debug_permissions script after this one in
# order to ensure the data is populated correctly
drop procedure if exists populate_user_permissions;

# this one requires an explicit cursor, as it iterates over all user_role records and
# updates same user_permission records multiple times
create procedure populate_user_permissions()
begin
    declare done tinyint default false;
    declare c_user_id bigint;
    declare c_namespace_id bigint;
    # the old-notation role from user_role
    declare c_role varchar(255);
    # the new permission which might be overwritten by each iteration for same namespace/user records
    # (which means it need to be operated upon to sum)
    declare c_permissions bigint;
    declare thecursor cursor for select u.id, n.id, role, permission
                                 from user_role
                                          inner join tenant_user tu on tu.id = tenant_user_id
                                          inner join tenant t on tu.tenant_id = t.id
                                          inner join namespace n on t.id = n.tenant_id
                                          inner join user u on tu.user_id = u.id
                                          inner join user_permissions up
                                                     on up.namespace_id = n.id and up.user_id = u.id
                                 limit 2000;
    declare continue handler for not found set done = true;

    open thecursor;

    read_loop: loop
        # fetches each record one by one
        fetch thecursor into c_user_id, c_namespace_id, c_role, c_permissions;
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

        case (c_role)

            when ('USER') then
                update user_permissions set permissions = permissions + 1
                where user_id = c_user_id and namespace_id = c_namespace_id;
            when ('MODEL_CREATOR') then
                update user_permissions set permissions = permissions + 2
                where user_id = c_user_id and namespace_id = c_namespace_id;
            when ('MODEL_PROMOTER') then
                update user_permissions set permissions = permissions + 4
                where user_id = c_user_id and namespace_id = c_namespace_id;
            when ('MODEL_REVIEWER') then
                update user_permissions set permissions = permissions + 8
                where user_id = c_user_id and namespace_id = c_namespace_id;
            when ('MODEL_PUBLISHER') then
                update user_permissions set permissions = permissions + 16
                where user_id = c_user_id and namespace_id = c_namespace_id;
            when ('TENANT_ADMIN') then
                update user_permissions set permissions = permissions + 32
                where user_id = c_user_id and namespace_id = c_namespace_id;
            # no handling for different cases such as 'SYS_ADMIN'
            else begin end;

        end case;

    end loop;

    close thecursor;
end;

# 6) create_and_populate_sysadmins
drop procedure if exists create_and_populate_sysadmins;

create procedure create_and_populate_sysadmins()
begin
    declare sysadmins tinyint;
    # Checks if the namespace table already has a owner_user_id field
    set sysadmins = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'sysadmins'
    );
    if (sysadmins = 0) then
        create table sysadmins (
            user_id bigint not null primary key,
            foreign key (user_id) references user(id)
        );
        insert into sysadmins
        select u.id from user_role
            inner join tenant_user tu on user_role.tenant_user_id = tu.id
        inner join user u on tu.user_id = u.id
        where user_role.role = 'SYS_ADMIN';
    end if;
end;