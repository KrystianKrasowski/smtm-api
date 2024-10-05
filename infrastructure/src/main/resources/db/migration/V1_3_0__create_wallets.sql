CREATE TABLE wallet_sets
(
    owner_id varchar primary key,
    version  integer not null default 1
);

CREATE TABLE wallets
(
    id       varchar primary key,
    owner_id varchar       not null,
    name     varchar(255) not null,
    icon     varchar(32)  not null
);

CREATE UNIQUE INDEX uq_wallets_owner_id_name ON wallets (name);

ALTER TABLE wallets
    ADD CONSTRAINT fk_wallets_wallet_sets
        FOREIGN KEY (owner_id)
            REFERENCES wallet_sets (owner_id)
            ON DELETE CASCADE;
