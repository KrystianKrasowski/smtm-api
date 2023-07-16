create table plan_sets (
    owner_id bigint primary key,
    version integer not null default 1,
    last_modified timestamp not null
);

create table plans (
    id bigint generated always as identity primary key,
    owner_id bigint not null,
    version integer not null default 1,
    name varchar(255) not null,
    start timestamp not null,
    "end" timestamp not null
);

alter table plans
    add constraint fk_plans_plan_sets
    foreign key (owner_id)
    references plan_sets (owner_id)
    on delete cascade;