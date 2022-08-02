create table bdr(
    id bigint auto_increment not null,
	sigla varchar(100) not null,
    created_at timestamp,
	constraint pk_bdr primary key(id)
)

