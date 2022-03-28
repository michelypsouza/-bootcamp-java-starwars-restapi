create table if not exists jedis (
    id integer not null auto_increment,
    name varchar(128) not null,
    strength integer not null,
    version integer not null,
    primary key (id)
);