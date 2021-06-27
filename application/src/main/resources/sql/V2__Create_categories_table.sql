create table categories
(
    id bigint auto_increment,
    name varchar(100) not null,
    icon varchar(50) not null,
    parent bigint null,
    constraint categories_pk
        primary key (id),
    constraint categories_categories_id_fk
        foreign key (parent) references categories (id)
);
