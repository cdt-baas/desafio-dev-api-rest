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



create table account
(
    id             uuid             not null
        constraint account_pk
            primary key
        constraint account_carrier_id_fk
            references carrier,
    cpf            varchar          not null,
    carrier_id     uuid             not null,
    balance        numeric,
    status         bigint default 0 not null,
    agency         bigint           not null,
    account_number bigint           not null,
    constraint account_unique_agency_account_number
        unique (account_number, agency)
);

alter table account
    owner to postgres;

create table public.transaction
(
    id         uuid                     default gen_random_uuid() not null
        constraint transaction_pk
            primary key,
    "from"     uuid
        constraint transaction_from_account_fk
            references public.account,
    "to"       uuid
        constraint transaction_to_account_fk
            references public.account,
    value      numeric                                            not null,
    created_at timestamp with time zone default now()             not null
);

alter table public.transaction
    owner to postgres;

create index transaction_from_index
    on public.transaction ("from", created_at);

create index transaction_to_index
    on public.transaction ("to", created_at);

