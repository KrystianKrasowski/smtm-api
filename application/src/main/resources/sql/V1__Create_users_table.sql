create table users
(
    id       int auto_increment,
    email    varchar(60) not null,
    password varchar(60) not null,
    constraint users_pk
        primary key (id)
);

create
unique index users_email_uindex
	on users (email);
