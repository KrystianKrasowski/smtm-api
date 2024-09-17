create table plan_entries
(
    id          bigint generated always as identity primary key,
    plan_id     varchar    not null,
    category_id varchar    not null,
    amount      integer    not null,
    currency    varchar(3) not null
);

alter table plan_entries
    add constraint fk_plan_entries_plans
        foreign key (plan_id)
            references plans (id)
            on delete cascade;

alter table plan_entries
    add constraint fk_plan_entries_categories
        foreign key (category_id)
            references categories (id)
            on delete cascade;
