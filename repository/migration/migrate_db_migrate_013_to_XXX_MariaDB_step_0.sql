/*
 This procedure is non-destructive.

 It performs the following operations:

    1) refactor_namespace_ownership_reference
    Modifies the namespace table by replacing the tenant foreign key with a foreign
    key pointing directly to the user who owns the namespace.

    2) flatten_user_roles
    Creates a new user_roles table (to ultimately replace user_role), with the following DDL:
    | Compound ID                                           | Roles as booleans...
    | user_id (FK user.id) + namespace_id (FK namespace.id) | model_user | model_creator | etc...

    ... then populates the table based on the contents of user_role

    This basically flattens the data from the current user_role table by a compound of user and
    namespace, with all applicable roles accessible in a single record as booleans.
    This should significantly decrease the read operations when querying for a user's roles in a
    namespace.
    This should also slightly decrease the number of write operations when either deleting a user or
    a namespace.
    SYS_ADMIN users will be removed from the table and managed in a separate table (see #3), since
    that role is not tied to a specific namespace, but rather system-wide.

    3) create_sysadmins
    Creates a sysadmins table by fetching data from the old user_role table where the role is
    SYS_ADMIN, inner joining the tenant_id on tenant_user and retrieving the user_id.
    The table only has one column expressing the user_id as both primary key and foreign key from
    the user table.

    Notes:

        a) There will still be some redundancy between the namespace.user field, which denotes
            ownership and therefore implies the TENANT_ADMIN role, and the actual TENANT_ADMIN
            role for that user in the user_roles table.
        b) When deleting a user account where the user's namespace(s) has other users with the
            TENANT_ADMIN role (which is the pre-condition to delete the account), the ownership of
            the namespace will be "orphaned". However, since the ownership does not differ practically
            from having the TENANT_ADMIN role (and there could be multiple other user with that role
            on the orphaned namespace), this should pose no issue.
        c) The TENANT_ADMIN role becomes namespace_admin (cosmetics)
 */

# refactor_namespace_ownership_reference
drop procedure if exists refactor_namespace_ownership_reference;

create procedure refactor_namespace_ownership_reference()
begin
    declare user_in_namespace_count tinyint;
    # Checks if the namespace table already has a user field
    set user_in_namespace_count = (
        select count(*) from information_schema.COLUMNS where TABLE_NAME='namespace' and COLUMN_NAME='user'
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
        alter table namespace ADD COLUMN user bigint;
        alter table namespace add constraint user foreign key (user) references user (id);
        update namespace n
            inner join tenant t on t.id = n.tenant_id
            inner join tenant_user tu on t.id = tu.tenant_id
        set n.user = tu.user_id;

    end if;
end;

# calls the actual procedure
call refactor_namespace_ownership_reference();

# flatten_user_roles
drop procedure if exists flatten_user_roles;

create procedure flatten_user_roles()
begin

    declare user_roles_table_count tinyint;
    # checks if the new user_roles table (not to be confused with the old user_role (singular) table
    # exists
    set user_roles_table_count = (
        select count(*) from information_schema.TABLES where TABLE_NAME = 'user_roles'
    );
    if (user_roles_table_count = 0) then
        # creates the new user_roles table with compound id user_id and namespace_id as foreign keys,
        # and roles flattened (except for sysadmin)
        create table user_roles
        (
            user_id         bigint not null,
            namespace_id    bigint not null,
            model_user      boolean not null default false,
            model_creator   boolean not null default false,
            model_promoter  boolean not null default false,
            model_reviewer  boolean not null default false,
            model_publisher boolean not null default false,
            namespace_admin boolean not null default false,
            primary key (user_id, namespace_id),
            foreign key (user_id) references user (id),
            foreign key (namespace_id) references namespace (id)
        );
        # fetches user and namespace compound keys from the namespace table, since it now features user id
        insert into user_roles (user_id, namespace_id) select user, id from namespace;
    end if;

end;