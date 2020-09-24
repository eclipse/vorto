drop procedure if exists create_and_populate_createdby;

create procedure create_and_populate_createdby()
begin
    # verifies column does not exist already - aborts if it does
    declare createdby tinyint;
    set createdby = (
        select count(*) from information_schema.COLUMNS where TABLE_NAME='user' and COLUMN_NAME='created_by'
    );
    if (createdby = 0) then
        # adds the column
        alter table user add column created_by bigint(20);
        # no FK as deleting a user should succeed if they have created technical users, and not
        # automatically cascade to them either
        # populates with default data: every user is "created by themselves" by default, including
        # technical users, since we cannot reliably verify that for already created users
        update user set user.created_by = user.id;
    end if;


end;

call create_and_populate_createdby();