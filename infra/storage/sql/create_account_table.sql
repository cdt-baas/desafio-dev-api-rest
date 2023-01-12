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

