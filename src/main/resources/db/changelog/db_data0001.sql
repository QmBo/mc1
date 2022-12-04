create table if not exists messages(
    id serial primary key,
    session_id integer,
    mc1_timestamp timestamp,
    mc2_timestamp timestamp,
    mc3_timestamp timestamp,
    end_timestamp timestamp
);