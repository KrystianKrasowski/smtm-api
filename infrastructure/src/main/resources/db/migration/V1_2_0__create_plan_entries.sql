create table plan_entries
(
    plan_id     varchar    not null,
    category_id varchar    not null,
    amount      integer    not null,
    currency    varchar(3) not null,
    constraint plan_entries_pk
        primary key (plan_id, category_id)
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
