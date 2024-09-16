create table plans
(
    id       varchar           not null
        constraint plans_pk
            primary key,
    owner_id bigint            not null,
    version  integer default 1 not null,
    name     varchar           not null,
    start    date              not null,
    "end"    date              not null
);

create index plans_owner_id_index
    on plans (owner_id);
