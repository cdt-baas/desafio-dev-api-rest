create table public.carrier
(
    id   uuid    not null
        constraint carrier_pk
            primary key,
    cpf  varchar not null
        constraint carrier_unique_cpf
            unique,
    name text    not null
);

alter table public.carrier
    owner to postgres;

