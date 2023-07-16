create table plans (
    id bigint generated always as identity primary key,
    owner_id bigint not null,
    version integer not null default 1,
    name varchar(255) not null,
    start timestamp not null,
    "end" timestamp not null
);