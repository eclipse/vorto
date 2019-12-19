# drops the following procedure if it exists (for testing purposes)
drop procedure if exists update_table;

create procedure update_table()
begin

    declare technical_user_count int;
    declare authentication_provider_id_count int;
    declare subject_count int;

# checks whether is_technical_user boolean field exists, adding if it does not
    set technical_user_count = (
        select count(*) from information_schema.COLUMNS where TABLE_NAME='user' and COLUMN_NAME='is_technical_user'
    );
    if (technical_user_count = 0) then
        ALTER TABLE user ADD COLUMN is_technical_user boolean;
    end if;

# checks whether authentication_provider_id string field exists, adding if it does not
    set authentication_provider_id_count = (
        select count(*) from information_schema.COLUMNS where TABLE_NAME='user' and COLUMN_NAME='authentication_provider_id'
    );
    if (authentication_provider_id_count = 0) then
        ALTER TABLE user ADD COLUMN authentication_provider_id varchar(255);
    end if;

# checks whether subject string field exists, adding if it does not
    set subject_count = (
        select count(*) from information_schema.COLUMNS where TABLE_NAME='user' and COLUMN_NAME='subject'
    );
    if (subject_count = 0) then
        ALTER TABLE user ADD COLUMN subject varchar(255);
    end if;

# sets all authentication provider IDs to BOSCH-ID if a Bosch-Id-like username is matched
    UPDATE user SET authentication_provider_id = 'BOSCH-ID' WHERE username LIKE 'S-%-%-%-%-%-%-%';

# sets all authentication provider IDs to GITHUB if no Bosch-Id-like username is matched
    UPDATE user SET authentication_provider_id = 'GITHUB' WHERE username NOT LIKE 'S-%-%-%-%-%-%-%';

# sets all users to non-technical
    UPDATE user SET is_technical_user=FALSE;

end;

# calls the actual procedure
call update_table();