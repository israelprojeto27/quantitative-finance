create table parametro(
    id bigint auto_increment not null,
	tipo_parametro varchar(100) not null,
	tipo_valor_parametro varchar(100) not null,
	valor varchar(100) not null,
	obs varchar(100) not null,
    created_at timestamp,
    updated_at timestamp,
	constraint pk_parametro primary key(id)
)
