-- we don't know how to generate root <with-no-name> (class Root) :(
create table carrier
(
    id   uuid    not null
        constraint carrier_pk
            primary key,
    cpf  varchar not null
        constraint carrier_unique_cpf
            unique,
    name text    not null
);

alter table carrier
    owner to postgres;

