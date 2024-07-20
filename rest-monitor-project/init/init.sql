DROP TABLE IF EXISTS user_data;

CREATE TABLE user_data (
    user_id integer,
    user_name TEXT,
    user_surname TEXT,
    user_email TEXT,
    user_address TEXT,
    PRIMARY KEY(user_id)
);