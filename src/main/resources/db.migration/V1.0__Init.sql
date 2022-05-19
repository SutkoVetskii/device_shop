create table brand
(
    id serial,
    name varchar(20) not null
);

create unique index brand_id_uindex
    on brand (id);

alter table brand
    add constraint brand_pk
        primary key (id);