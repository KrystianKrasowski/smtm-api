CREATE TABLE category_sets
(
    owner_id varchar primary key,
    version  integer not null default 1
);

CREATE TABLE categories
(
    id       varchar primary key,
    owner_id varchar       not null,
    name     varchar(255) not null,
    icon     varchar(32)  not null
);

CREATE UNIQUE INDEX uq_categories_owner_id_name ON categories (name);

ALTER TABLE categories
    ADD CONSTRAINT fk_categories_category_sets
        FOREIGN KEY (owner_id)
            REFERENCES category_sets (owner_id)
            ON DELETE CASCADE;
