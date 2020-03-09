# This procedure modifies the namespace table by replacing the tenant foreign key with a foreign
# key pointing directly to the user who owns the namespace
drop procedure if exists refactor_namespace_ownership_reference;

create procedure refactor_namespace_ownership_reference()
begin
    declare user_in_namespace_count int;
    # Checks if the namespace table already has a user foreign key
    set user_in_namespace_count = (
        select count(*) from information_schema.COLUMNS where TABLE_NAME='namespace' and COLUMN_NAME='user'
    );
    # This does two things:
        # 1 Creates a user field in the namespace table with reference to the primary key in the user table
        # 2 Updates the values of user in namespace to match the user id referenced in tenant_user,
        # through a nested inner join
        # Note: the owner_id of the tenant table is unfortunately missing data, so it is not usable
        # (hence, the nexted inner join). Likely the historical reason is that the owner_id field
        # has been abandoned back then, in favor of the intermediary table tenant_user, which we are
        # ultimately going to remove again.
    if (user_in_namespace_count = 0) then
        ALTER TABLE namespace ADD COLUMN user bigint;
        ALTER TABLE namespace ADD CONSTRAINT user FOREIGN KEY (user) references user (id);
        UPDATE namespace n INNER JOIN tenant t ON t.id = n.tenant_id INNER JOIN tenant_user tu ON t.id = tu.tenant_id SET n.user = tu.user_id;

    end if;
end;

# calls the actual procedure
call refactor_namespace_ownership_reference();