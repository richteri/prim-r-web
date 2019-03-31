create table prime_number (
	id bigserial not null constraint prime_number_pk primary key,
	number bigint not null,
	prime bigint not null
);

create unique index prime_number_number_uindex
	on prime_number (number);

