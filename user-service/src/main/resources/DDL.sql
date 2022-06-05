CREATE TABLE IF NOT EXISTS security.users
(
    username text,
    password text,
    enabled boolean,
    CONSTRAINT users_pk PRIMARY KEY (username)
)

CREATE TABLE IF NOT EXISTS security.authorities
(
    username text,
    authority text,
    CONSTRAINT authorities_pk PRIMARY KEY (username)
)