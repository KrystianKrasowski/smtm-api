create table refresh_tokens
(
    user_id bigint not null,
    token_id VARCHAR(36) not null,
    constraint refresh_tokens_pk
        primary key (user_id),
    constraint refresh_tokens_users_id_fk
        foreign key (user_id) references users (id)
);

