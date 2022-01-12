drop table if exists values;
create table if not exists values
(
    id    bigserial                NOT NULL,
    date  timestamp with time zone not null,
    value varchar(200)             not null,
    CONSTRAINT values_pkey PRIMARY KEY (id)
);
