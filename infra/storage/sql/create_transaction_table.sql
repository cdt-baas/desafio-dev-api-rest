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

